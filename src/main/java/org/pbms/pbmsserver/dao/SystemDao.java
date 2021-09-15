package org.pbms.pbmsserver.dao;

import org.pbms.pbmsserver.common.constant.ServerConstant;
import org.pbms.pbmsserver.common.exception.BusinessException;
import org.pbms.pbmsserver.common.exception.BusinessStatus;
import org.pbms.pbmsserver.common.exception.ServerException;
import org.pbms.pbmsserver.repository.model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class SystemDao {

    @Autowired
    private UserInfoDao userInfoDao;

    private UserInfo getUserInfo(long userId) {
        return this.userInfoDao.selectByPrimaryKey(userId)
                .orElseThrow(() -> new BusinessException(BusinessStatus.USER_NOT_FOUND));
    }

    // 获取当前用户图片保存路径
    private String getAbsoluteUploadPath(long userId) {
        return ServerConstant.SERVER_ROOT_PATH + File.separator + this.getUserInfo(userId).getUserName() + File.separator + "upload";
    }

    // 获取当前用户logo存储路径
    private String getAbsoluteLogoPath(long userId) {
        return ServerConstant.SERVER_ROOT_PATH + File.separator + this.getUserInfo(userId).getUserName() + File.separator + "logo";
    }

    // 获取当前用户临时文件路径
    private String getAbsoluteTempPath(long userId) {
        return ServerConstant.SERVER_ROOT_PATH + File.separator + this.getUserInfo(userId).getUserName() + File.separator + "temp";
    }

    public void initAllRespectiveDir(long userId) {
        File tempDir = new File(this.getAbsoluteTempPath(userId));
        if (!tempDir.exists() && !tempDir.mkdirs()) {
            throw new ServerException("用户temp文件存储文件夹创建失败");
        }
        File logoDir = new File(this.getAbsoluteLogoPath(userId));
        if (!logoDir.exists() && !logoDir.mkdirs()) {
            throw new ServerException("用户logo文件存储文件夹创建失败");
        }
        File uploadDir = new File(this.getAbsoluteUploadPath(userId));
        if (!uploadDir.exists() && !uploadDir.mkdirs()) {
            throw new ServerException("用户upload文件存储文件夹创建失败");
        }
    }
}
