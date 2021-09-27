package org.pbms.pbmsserver.common.constant;

import org.pbms.pbmsserver.common.auth.TokenBean;
import org.pbms.pbmsserver.util.TokenUtil;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

/**
 * 上传图片路径、大小、格式配置
 *
 * @author zyl
 */

@Component
public final class ServerConstant {
    private ServerConstant() {
    }

    public static final String SERVER_ROOT_PATH = ConfigReader.configReader.getServerRootPath();
    public static final String SERVER_BASEURL = ConfigReader.configReader.getServerBaseurl();
    // 实际使用byte大小表示
    public static final long SERVER_MAX_SIZE = ConfigReader.configReader.getServerMaxSize();
    public static final List<String> SERVER_SUPPORT_TYPE = ConfigReader.configReader.getServerSupportType();

    // 获取当前用户图片保存路径
    public static String getAbsoluteUploadPath() {
        TokenBean tokenBean = TokenUtil.getTokenBean();
        return ServerConstant.SERVER_ROOT_PATH + File.separator + tokenBean.getUserName() + File.separator + "upload";
    }

    // 获取当前用户logo存储路径
    public static String getAbsoluteLogoPath() {
        TokenBean tokenBean = TokenUtil.getTokenBean();
        return ServerConstant.SERVER_ROOT_PATH + File.separator + tokenBean.getUserName() + File.separator + "logo";
    }

    // 获取当前用户临时文件路径
    public static String getAbsoluteTempPath() {
        TokenBean tokenBean = TokenUtil.getTokenBean();
        return ServerConstant.SERVER_ROOT_PATH + File.separator + tokenBean.getUserName() + File.separator + "temp";
    }

    // 获取当前用户图片保存路径
    public static String getAbsoluteURLUploadPath() {
        TokenBean tokenBean = TokenUtil.getTokenBean();
        return ServerConstant.SERVER_BASEURL + "/image/" + tokenBean.getUserName() + "/upload";
    }
}