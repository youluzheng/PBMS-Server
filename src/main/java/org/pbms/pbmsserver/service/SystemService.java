package org.pbms.pbmsserver.service;


import org.pbms.pbmsserver.common.constant.ServerConstant;
import org.pbms.pbmsserver.common.exception.ClientException;
import org.pbms.pbmsserver.common.request.user.UserRegisterDTO;
import org.pbms.pbmsserver.dao.SystemDao;
import org.pbms.pbmsserver.dao.UserInfoDao;
import org.pbms.pbmsserver.repository.enumeration.user.UserRoleEnum;
import org.pbms.pbmsserver.repository.enumeration.user.UserStatusEnum;
import org.pbms.pbmsserver.repository.mapper.UserInfoDynamicSqlSupport;
import org.pbms.pbmsserver.repository.model.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

/**
 * @author 王俊
 * @author zyl
 * @since 0.3.0
 */
@Service
public class SystemService {
    private static final Logger log = LoggerFactory.getLogger(SystemService.class);

    @Autowired
    private UserInfoDao userInfoDao;
    @Autowired
    private UserManageService userManageService;
    @Autowired
    private SystemDao systemDao;

    /**
     * 初始化首个管理员用户
     *
     * @param req 用户信息
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public void initAdmin(UserRegisterDTO req) {
        if (isInit()) {
            throw new ClientException("初始用户已建立，接口调用错误！");
        }
        UserInfo admin = req.transform();
        admin.setPassword(ServerConstant.HASH_METHOD.apply(admin.getPassword()));
        admin.setStatus(UserStatusEnum.NORMAL.getCode());
        admin.setCreateTime(new Date());
        admin.setRole(UserRoleEnum.ADMIN.getCode());
        log.info("创建初始管理员用户:{}中", admin.getUserName());
        userInfoDao.insert(admin);
        log.info("初始化管理员用户默认配置");
        this.userManageService.initDefaultSettings(admin.getUserId());
        log.info("初始化管理员存储目录");
        this.systemDao.initAllRespectiveDir(admin);
    }

    public boolean isInit() {
        return userInfoDao.count(c -> c
                .where(UserInfoDynamicSqlSupport.role, isEqualTo(UserRoleEnum.ADMIN.getCode()))) > 0;
    }
}
