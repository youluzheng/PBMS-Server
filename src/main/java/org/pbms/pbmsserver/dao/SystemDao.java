package org.pbms.pbmsserver.dao;

import cn.hutool.core.io.FileUtil;
import org.pbms.pbmsserver.common.constant.ServerConstant;
import org.pbms.pbmsserver.common.exception.BusinessException;
import org.pbms.pbmsserver.common.exception.BusinessStatus;
import org.pbms.pbmsserver.common.exception.ServerException;
import org.pbms.pbmsserver.repository.model.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class SystemDao {

    private static final Logger log = LoggerFactory.getLogger(SystemDao.class);

    @Autowired
    private UserInfoDao userInfoDao;

    public void initAllRespectiveDir(UserInfo user) {
        log.info("初始化{} [temp] 目录", user.getUserName());
        FileUtil.mkdir(ServerConstant.getAbsoluteTempPath(user));
        log.info("初始化{} [logo] 目录", user.getUserName());
        FileUtil.mkdir(ServerConstant.getAbsoluteLogoPath(user));
        log.info("初始化{} [upload] 目录", user.getUserName());
        FileUtil.mkdir(ServerConstant.getAbsoluteUploadPath(user));
    }

    public void removeAllRespectiveDir(long userId) {
        UserInfo user = this.userInfoDao.selectByPrimaryKey(userId)
                .orElseThrow(() -> new BusinessException(BusinessStatus.USER_NOT_FOUND));
        if (!FileUtil.del(ServerConstant.getAbsolutePath(user))) {
            log.warn("文件夹删除失败:{}", ServerConstant.getAbsolutePath(user));
        }
    }
}
