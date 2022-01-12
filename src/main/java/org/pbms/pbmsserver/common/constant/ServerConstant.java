package org.pbms.pbmsserver.common.constant;

import cn.hutool.crypto.digest.DigestUtil;
import org.pbms.pbmsserver.common.auth.TokenBean;
import org.pbms.pbmsserver.common.auth.TokenHandle;
import org.pbms.pbmsserver.repository.model.UserInfo;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * 上传图片路径、大小、格式配置
 *
 * @author zyl
 */
public final class ServerConstant {
    private ServerConstant() {
    }

    public static final String REGISTER_SUBJECT = "pmbs用户注册验证邮件";
    public static final String ACCEPT_SUBJECT = "pmbs用户审核通过邮件";
    public static final String NEW_USER_SUBJECT = "pmbs新用户注册邮件";
    public static final String CHANGE_PASSWORD_SUBJECT = "pmbs忘记密码邮件";

    public static final String CHANGE_PASSWORD_TEMPLATE = "chPasswordTemplate";
    public static final String REGISTER_TEMPLATE = "registerTemplate";
    public static final String ACCEPT_TEMPLATE = "acceptTemplate";
    public static final String NEW_USER_TEMPLATE = "newUserTemplate";

    public static final String SERVER_ROOT_PATH = ConfigReader.configReader.getServerRootPath();
    public static final String SERVER_BASEURL = ConfigReader.configReader.getServerBaseurl();
    // 实际使用byte大小表示
    public static final long SERVER_MAX_SIZE = ConfigReader.configReader.getServerMaxSize();
    public static final List<String> SERVER_SUPPORT_TYPE = ConfigReader.configReader.getServerSupportType();

    // 获取当前用户根路径
    public static String getAbsolutePath() {
        TokenBean tokenBean = TokenHandle.getTokenBean();
        return ServerConstant.SERVER_ROOT_PATH + File.separator + tokenBean.getUserName();
    }

    // 获取当前用户图片保存路径
    public static String getAbsoluteUploadPath() {
        TokenBean tokenBean = TokenHandle.getTokenBean();
        return ServerConstant.SERVER_ROOT_PATH + File.separator + tokenBean.getUserName() + File.separator + "upload";
    }

    // 获取当前用户logo存储路径
    public static String getAbsoluteLogoPath() {
        TokenBean tokenBean = TokenHandle.getTokenBean();
        return ServerConstant.SERVER_ROOT_PATH + File.separator + tokenBean.getUserName() + File.separator + "logo";
    }

    // 获取当前用户临时文件路径
    public static String getAbsoluteTempPath() {
        TokenBean tokenBean = TokenHandle.getTokenBean();
        return ServerConstant.SERVER_ROOT_PATH + File.separator + tokenBean.getUserName() + File.separator + "temp";
    }

    // 获取当前用户图片保存路径
    public static String getAbsoluteURLUploadPath() {
        TokenBean tokenBean = TokenHandle.getTokenBean();
        return ServerConstant.SERVER_BASEURL + "/image/" + tokenBean.getUserName() + "/upload";
    }

    // 获取指定用户根路径
    public static String getAbsolutePath(UserInfo userInfo) {
        Objects.requireNonNull(userInfo.getUserName());
        return ServerConstant.SERVER_ROOT_PATH + File.separator + userInfo.getUserName();
    }

    // 获取指定用户图片保存路径
    public static String getAbsoluteUploadPath(UserInfo userInfo) {
        Objects.requireNonNull(userInfo.getUserName());
        return ServerConstant.SERVER_ROOT_PATH + File.separator + userInfo.getUserName() + File.separator + "upload";
    }

    // 获取指定用户logo存储路径
    public static String getAbsoluteLogoPath(UserInfo userInfo) {
        Objects.requireNonNull(userInfo.getUserName());
        return ServerConstant.SERVER_ROOT_PATH + File.separator + userInfo.getUserName() + File.separator + "logo";
    }

    // 获取指定用户临时文件路径
    public static String getAbsoluteTempPath(UserInfo userInfo) {
        Objects.requireNonNull(userInfo.getUserName());
        return ServerConstant.SERVER_ROOT_PATH + File.separator + userInfo.getUserName() + File.separator + "temp";
    }

    public static final Function<String, String> HASH_METHOD = DigestUtil::sha256Hex;
}