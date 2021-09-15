package org.pbms.pbmsserver.service;

import org.pbms.pbmsserver.common.auth.TokenBean;
import org.pbms.pbmsserver.common.exception.BusinessException;
import org.pbms.pbmsserver.common.exception.BusinessStatus;
import org.pbms.pbmsserver.common.exception.ResourceNotFoundException;
import org.pbms.pbmsserver.dao.SystemDao;
import org.pbms.pbmsserver.dao.UserInfoDao;
import org.pbms.pbmsserver.dao.UserSettingsDao;
import org.pbms.pbmsserver.repository.enumeration.user.UserStatusEnum;
import org.pbms.pbmsserver.repository.mapper.UserInfoDynamicSqlSupport;
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
    private UserInfoDao userInfoDao;

    @Autowired
    private UserSettingsDao userSettingsDao;

    @Autowired
    private SystemDao systemDao;

    @Transactional
    public void auditUser(long userId, boolean optional) {
        if (this.userInfoDao.selectOne(c -> c
                .where(UserInfoDynamicSqlSupport.userInfo.userId, isEqualTo(userId))
                .and(UserInfoDynamicSqlSupport.userInfo.status, isEqualTo(UserStatusEnum.WAIT_FOR_AUDIT.getCode()))
        ).isEmpty()
        ) {
            throw new BusinessException(BusinessStatus.USER_NOT_FOUND);
        }

        UserInfo updateUser = new UserInfo();
        updateUser.setUserId(userId);
        if (optional) {
            updateUser.setStatus(UserStatusEnum.NORMAL.getCode());

            // 初始化默认设置
            this.initDefaultSettings(userId);

            // 初始化存储路径
            this.systemDao.initAllRespectiveDir(userId);
        } else {
            updateUser.setStatus(UserStatusEnum.AUDIT_FAIL.getCode());
        }
        this.userInfoDao.updateByPrimaryKeySelective(updateUser);
    }

    public String login(String userName, String password) {
        UserInfo user = this.userInfoDao.selectOne(c -> c
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

    public void initDefaultSettings(long userId) {
        UserSettings userSettings = new UserSettings();
        userSettings.setUserId(userId);
        userSettings.setWatermarkLogoEnable(false);
        userSettings.setWatermarkTextEnable(false);
        userSettings.setCompressScale((byte) 0);
        userSettings.setResponseReturnType("markdown");
        this.userSettingsDao.insert(userSettings);
    }

    public UserSettings getSettings() {
        return this.userSettingsDao.selectByPrimaryKey(TokenUtil.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("用户不存在"));
    }
}
