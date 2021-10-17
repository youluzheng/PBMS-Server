package org.pbms.pbmsserver.service;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import org.pbms.pbmsserver.common.auth.TokenBean;
import org.pbms.pbmsserver.common.constant.ServerConstant;
import org.pbms.pbmsserver.common.exception.BusinessException;
import org.pbms.pbmsserver.common.exception.BusinessStatus;
import org.pbms.pbmsserver.common.exception.ClientException;
import org.pbms.pbmsserver.common.exception.ResourceNotFoundException;
import org.pbms.pbmsserver.common.request.user.UserRegisterReq;
import org.pbms.pbmsserver.dao.UserInfoDao;
import org.pbms.pbmsserver.dao.UserSettingsDao;
import org.pbms.pbmsserver.repository.enumeration.user.UserRoleEnum;
import org.pbms.pbmsserver.repository.enumeration.user.UserStatusEnum;
import org.pbms.pbmsserver.repository.mapper.UserInfoDynamicSqlSupport;
import org.pbms.pbmsserver.repository.model.UserInfo;
import org.pbms.pbmsserver.repository.model.UserSettings;
import org.pbms.pbmsserver.service.common.MailService;
import org.pbms.pbmsserver.util.TokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;

import java.util.List;

import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;
import static org.pbms.pbmsserver.repository.mapper.UserInfoDynamicSqlSupport.*;

/**
 * @author 王俊
 * @author zyl
 * @date 2021/9/5 14:03
 */
@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    /**
     * 目前默认过期时间为10分钟
     */
    private final TimedCache<Long, String> codeMap = CacheUtil.newTimedCache(10 * 1000L * 60L);

    @Autowired
    private UserInfoDao userInfoDao;

    @Autowired
    private UserSettingsDao userSettingsDao;

    @Autowired
    private MailService mailService;

    public String login(String userName, String password) {
        UserInfo user = this.userInfoDao.selectOne(c -> c
                .where(userInfo.userName, isEqualTo(userName))
                .and(userInfo.password, isEqualTo(ServerConstant.HASH_METHOD.apply(password)))
                .and(userInfo.status, isEqualTo(UserStatusEnum.NORMAL.getCode()))
        ).orElseThrow(
                () -> new BusinessException(BusinessStatus.USERNAME_OR_PASSWORD_ERROR)
        );
        return TokenUtil.generateToken(new TokenBean(user.getUserId(), userName, user.getRole()));
    }

    public UserSettings getSettings() {
        return this.userSettingsDao.selectByPrimaryKey(TokenUtil.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("用户不存在"));
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public void register(UserRegisterReq req) {
        List<UserInfo> userInfos = this.userInfoDao.select(c -> c
                .where(userName, isEqualTo(req.getUserName()))
                .or(email, isEqualTo(req.getEmail())));
        if (userInfos.size() > 1) {
            throw new BusinessException(BusinessStatus.USERNAME_ALREADY_EXISTS);
        }
        UserInfo userInfo = userInfos.size() == 1 ? userInfos.get(0) : null;
        if (userInfo != null) {
            // 在有效期内的未确认邮件用户也无法重复
            if (userInfo.getStatus() == UserStatusEnum.UNCHECKED.getCode()) {
                if (codeMap.containsKey(userInfo.getUserId())) {
                    throw new BusinessException(BusinessStatus.EMAIL_SENT);
                } else {
                    // 删除库中为确认邮箱的用户
                    userInfoDao.deleteByPrimaryKey(userInfo.getUserId());
                }
            } else {
                // 新用户名不能与已被禁用的用户,正常用户名,等待审核用户名一致
                if (req.getUserName().equals(userInfo.getUserName())) {
                    throw new BusinessException(BusinessStatus.USERNAME_ALREADY_EXISTS);
                }
                if (req.getEmail().equals(userInfo.getEmail())) {
                    throw new BusinessException(BusinessStatus.EMAIL_REGISTERED);
                }
            }
        }

        UserInfo newUser = req.transform();
        newUser.setPassword(ServerConstant.HASH_METHOD.apply(newUser.getPassword()).get());
        newUser.setCreateTime(DateUtil.date());
        newUser.setRole(UserRoleEnum.NORMAL.getCode());
        newUser.setStatus(UserStatusEnum.UNCHECKED.getCode());
        userInfoDao.insert(newUser);
        // 异步发送邮件，提交异步任务后直接返回
        Context context = new Context();
        // 生成临时url的code
        String code = RandomUtil.randomString(32);
        codeMap.put(newUser.getUserId(), code);
        context.setVariable("name", newUser.getUserName());
        context.setVariable("baseUrl", ServerConstant.SERVER_BASEURL);
        context.setVariable("userId", newUser.getUserId());
        context.setVariable("code", code);
        mailService.sendMail(ServerConstant.REGISTER_SUBJECT, newUser.getEmail(), ServerConstant.REGISTER_TEMPLATE, context);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public void checkRegisterLink(long userId, String code) {
        UserInfo userInfo = checkCode(userId, code);
        if (!userInfo.getStatus().equals(UserStatusEnum.UNCHECKED.getCode())) {
            throw new ClientException("操作失败");
        }
        userInfo.setStatus(UserStatusEnum.WAIT_FOR_AUDIT.getCode());
        userInfoDao.updateByPrimaryKeySelective(userInfo);
        // 发送给管理员邮件
        userInfoDao.select(c -> c.where(UserInfoDynamicSqlSupport.userInfo.role, isEqualTo(UserRoleEnum.ADMIN.getCode()))).forEach(a -> {
            Context context = new Context();
            context.setVariable("name", a.getUserName());
            mailService.sendMail(ServerConstant.NEW_USER_SUBJECT, a.getEmail(), ServerConstant.NEW_USER_TEMPLATE, context);
        });
    }

    public void getChangePasswordEmailCaptcha(String email, String userName) {
        UserInfo userInfo = userInfoDao.selectOne(c ->
                c.where(UserInfoDynamicSqlSupport.userInfo.userName, isEqualTo(userName))
                        .and(UserInfoDynamicSqlSupport.status, isEqualTo(UserStatusEnum.NORMAL.getCode()))
                        .and(UserInfoDynamicSqlSupport.email, isEqualTo(email))).orElseThrow(() -> new BusinessException(BusinessStatus.USER_NOT_FOUND));
        if (codeMap.get(userInfo.getUserId()) != null) {
            throw new ClientException("邮件已发送，稍后再试");
        }
        // 发送验证邮件
        String s = RandomUtil.randomString(32);
        codeMap.put(userInfo.getUserId(), s);
        Context context = new Context();
        context.setVariable("name", userInfo.getUserName());
        context.setVariable("userId", userInfo.getUserId());
        context.setVariable("baseUrl", ServerConstant.SERVER_BASEURL);
        context.setVariable("code", s);
        mailService.sendMail(ServerConstant.CHANGE_PASSWORD_SUBJECT, userInfo.getEmail(), ServerConstant.CHANGE_PASSWORD_TEMPLATE, context);
    }

    public String checkChangePasswordMail(long userId, String code) {
        UserInfo user = checkCode(userId, code);
        TokenBean tokenBean = new TokenBean(user.getUserId(), user.getUserName(), user.getRole());
        return TokenUtil.generateToken(tokenBean);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public void changePassword(String password) {
        long userId = TokenUtil.getUserId();
        UserInfo user = userInfoDao.selectOne(c ->
                c.where(userInfo.userId, isEqualTo(userId))).orElseThrow(() -> new ClientException("用户错误"));
        user.setPassword(ServerConstant.HASH_METHOD.apply(password).get());
        userInfoDao.updateByPrimaryKeySelective(user);
    }

    public UserInfo getUserInfo(long userId) {
        return this.userInfoDao.selectByPrimaryKey(userId)
                .orElseThrow(() -> new ResourceNotFoundException("用户不存在"));
    }

    public UserInfo checkCode(long userId, String code) {
        log.debug("code:{}", code);
        if (codeMap.get(userId) == null) {
            throw new BusinessException(BusinessStatus.INVALID_LINK);
        }
        if (!code.equals(codeMap.get(userId))) {
            // code必然被修改过
            throw new ClientException("无效链接");
        }
        codeMap.remove(userId);
        return userInfoDao.selectByPrimaryKey(userId).orElseThrow(() -> new ClientException("用户错误"));
    }
}