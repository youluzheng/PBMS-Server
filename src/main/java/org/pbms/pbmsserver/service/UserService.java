package org.pbms.pbmsserver.service;

import org.pbms.pbmsserver.common.auth.TokenBean;
import org.pbms.pbmsserver.common.exception.BusinessException;
import org.pbms.pbmsserver.common.exception.BusinessStatus;
import org.pbms.pbmsserver.common.exception.ResourceNotFoundException;
import org.pbms.pbmsserver.init.Init;
import org.pbms.pbmsserver.repository.enumeration.user.UserStatusEnum;
import org.pbms.pbmsserver.repository.mapper.UserInfoMapper;
import org.pbms.pbmsserver.repository.mapper.UserSettingsMapper;
import org.pbms.pbmsserver.repository.model.UserInfo;
import org.pbms.pbmsserver.repository.model.UserSettings;
import org.pbms.pbmsserver.util.EncryptUtil;
import org.pbms.pbmsserver.util.TokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;
import static org.pbms.pbmsserver.repository.mapper.UserInfoDynamicSqlSupport.userInfo;

/**
 * @author 王俊
 * @author zyl
 * @date 2021/9/5 14:03
 */
@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private UserSettingsMapper userSettingsMapper;

    @Transactional
    public void auditUser(long userId, int optional) {
        UserInfo user = this.userInfoMapper.selectOne(c -> c
                .where(userInfo.userId, isEqualTo(userId))
                .and(userInfo.status, isEqualTo(UserStatusEnum.WAIT_FOR_AUDIT.getCode()))
        ).orElseThrow(() -> new BusinessException(BusinessStatus.USER_NOT_FOUND));

        UserInfo updateUser = new UserInfo();
        updateUser.setUserId(userId);
        if (optional == 1) {
            updateUser.setStatus(UserStatusEnum.NORMAL.getCode());
        } else {
            updateUser.setStatus(UserStatusEnum.AUDIT_FAIL.getCode());
        }
        this.userInfoMapper.updateByPrimaryKeySelective(updateUser);

        if (optional == 1) {
            // 初始化默认设置
            this.initDefaultSettings();
            TokenBean tokenBean = new TokenBean();
            tokenBean.setUserId(userId);
            tokenBean.setUserName(user.getUserName());
            // 初始化存储路径
            Init.initAllRespectiveDir(tokenBean);
        }
    }

    public String login(String userName, String password) {
        UserInfo user = this.userInfoMapper.selectOne(c -> c
                .where(userInfo.userName, isEqualTo(userName))
                .and(userInfo.password, isEqualTo(EncryptUtil.sha512(password)))
        ).orElseThrow(
                () -> new BusinessException(BusinessStatus.USERNAME_OR_PASSWORD_ERROR)
        );
        TokenBean tokenBean = new TokenBean();
        tokenBean.setUserId(user.getUserId());
        tokenBean.setUserName(userName);
        return TokenUtil.generateToken(tokenBean);
    }

    public void initDefaultSettings() {
        UserSettings userSettings = new UserSettings();
        userSettings.setUserId(TokenUtil.getUserId());
        userSettings.setWatermarkLogoEnable(false);
        userSettings.setWatermarkTextEnable(false);
        userSettings.setCompressScale((byte) 0);
        userSettings.setResponseReturnType("markdown");
        this.userSettingsMapper.insert(userSettings);
    }

    public void deleteSettings() {
        this.userSettingsMapper.deleteByPrimaryKey(TokenUtil.getUserId());
    }

    public void modifySettings(UserSettings userSettings) {
        userSettings.setUserId(TokenUtil.getUserId());
        this.userSettingsMapper.updateByPrimaryKeySelective(userSettings);
    }

    public UserSettings getSettings() {
        return this.userSettingsMapper.selectByPrimaryKey(TokenUtil.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("用户不存在"));
    }
}
