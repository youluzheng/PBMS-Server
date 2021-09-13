package org.pbms.pbmsserver.init;

import org.pbms.pbmsserver.common.auth.TokenBean;
import org.pbms.pbmsserver.common.constant.ServerConstant;
import org.pbms.pbmsserver.common.exception.ServerException;
import org.pbms.pbmsserver.repository.mapper.UserSettingsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Objects;


/**
 * 创建初始文件夹
 *
 * @author 王俊
 * @author zyl
 * @date 2021-06-21
 */
@Component
public class Init implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(Init.class);
    @Autowired
    private UserSettingsMapper userSettingsMapper;

    private void imageRepositoryInit() {
        log.info("检查{}是否存在...", ServerConstant.SERVER_ROOT_PATH);

        File rootPath = new File(ServerConstant.SERVER_ROOT_PATH);
        if (!rootPath.exists()) {
            log.warn("{}不存在, 创建中...", ServerConstant.SERVER_ROOT_PATH);
            if (!rootPath.mkdirs()) {
                throw new ServerException("{}创建失败" + ServerConstant.SERVER_ROOT_PATH);
            }
            log.info("{}创建完成", ServerConstant.SERVER_ROOT_PATH);
            return;
        }
        log.info("{}已经存在", ServerConstant.SERVER_ROOT_PATH);
    }

    // 获取各自图片保存路径
    public static String getRespectiveAbsoluteUploadPath(TokenBean tokenBean) {
        Objects.requireNonNull(tokenBean);
        return ServerConstant.SERVER_ROOT_PATH + File.separator + tokenBean.getUserName() + File.separator + "upload";
    }

    // 获取各自的logo存储路径
    public static String getRespectiveAbsoluteLogoPath(TokenBean tokenBean) {
        Objects.requireNonNull(tokenBean);
        return ServerConstant.SERVER_ROOT_PATH + File.separator + tokenBean.getUserName() + File.separator + "logo";
    }

    // 获取各自的临时文件路径
    public static String getRespectiveAbsoluteTempPath(TokenBean tokenBean) {
        Objects.requireNonNull(tokenBean);
        return ServerConstant.SERVER_ROOT_PATH + File.separator + tokenBean.getUserName() + File.separator + "temp";
    }

    // 获取各自图片保存路径
    public static String getRespectiveRelativeURLUploadPath(TokenBean tokenBean) {
        Objects.requireNonNull(tokenBean);
        return tokenBean.getUserName() + "/upload";
    }

    public static void initAllRespectiveDir(TokenBean tokenBean) {
        File tempDir = new File(Init.getRespectiveAbsoluteTempPath(tokenBean));
        if (!tempDir.exists() && !tempDir.mkdirs()) {
            throw new ServerException("用户temp文件存储文件夹创建失败");
        }
        File logoDir = new File(Init.getRespectiveAbsoluteLogoPath(tokenBean));
        if (!logoDir.exists() && !logoDir.mkdirs()) {
            throw new ServerException("用户logo文件存储文件夹创建失败");
        }
        File uploadDir = new File(Init.getRespectiveAbsoluteUploadPath(tokenBean));
        if (!uploadDir.exists() && !uploadDir.mkdirs()) {
            throw new ServerException("用户upload文件存储文件夹创建失败");
        }
    }

    @Override
    public void run(String... args) {
        this.imageRepositoryInit();
    }
}
