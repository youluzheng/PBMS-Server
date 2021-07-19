package org.pbms.pbmsserver.common.constant;

import org.pbms.pbmsserver.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 上传图片路径、大小、格式配置
 *
 * @author zyl
 * @Date 2021-06-21 19:54:11
 */

@Component
public class ServerConstant {
    private static final Logger log = LoggerFactory.getLogger(ServerConstant.class);

    public static String SERVER_ROOT_PATH;

    @Value("${picturebed.rootpath:pictureBed}")
    public void setRootPath(String rootPath) {
        log.info("set rootPath:{}", rootPath);
        ServerConstant.SERVER_ROOT_PATH = rootPath;
    }

    public static String SERVER_BASEURL;

    @Value("${picturebed.baseurl:http://127.0.0.1:8080}")
    public void setBaseurl(String baseurl) {
        log.info("set baseurl:{}", baseurl);
        ServerConstant.SERVER_BASEURL = baseurl;
    }

    // 实际使用byte大小表示
    public static long SERVER_MAX_SIZE;

    @Value("${picturebed.maxSize:10MB}")
    public void setMaxSize(String maxSize) {
        // 转换为大写
        maxSize = maxSize.toUpperCase();
        if (!StringUtil.isValidSizeFormatString(maxSize)) {
            log.warn("picturebed.maxSize不支持配置项:{}, 默认位置为10MB", maxSize);
            maxSize = "10MB";
        }
        log.info("set maxSize:{}", maxSize);
        if (maxSize.endsWith("GB")) {
            ServerConstant.SERVER_MAX_SIZE = Long.parseLong(maxSize.substring(0, maxSize.length() - 2)) * 1024 * 1024
                    * 1024;
        } else if (maxSize.endsWith("MB")) {
            ServerConstant.SERVER_MAX_SIZE = Long.parseLong(maxSize.substring(0, maxSize.length() - 2)) * 1024 * 1024;
        } else {
            ServerConstant.SERVER_MAX_SIZE = Long.parseLong(maxSize.substring(0, maxSize.length() - 2)) * 1024;
        }
    }

    public static List<String> SERVER_SUPPORT_TYPE;

    @Value("${picturebed.supportType:png, jpg, gif}")

    public void setSupportType(String supportType) {
        log.info("set supportType:[{}]", supportType);
        // 转换为列表, 并且去除前后空白字符
        ServerConstant.SERVER_SUPPORT_TYPE = Arrays.asList(supportType.split(",")).stream().map(str -> str.strip())
                .collect(Collectors.toList());
    }

}