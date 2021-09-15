package org.pbms.pbmsserver.common.constant;

import org.pbms.pbmsserver.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ConfigReader {
    private static final Logger log = LoggerFactory.getLogger(ConfigReader.class);

    public static final ConfigReader configReader = new ConfigReader();

    private String serverRootPath;
    private String serverBaseurl;
    // 实际使用byte大小表示
    private long serverMaxSize;
    private List<String> serverSupportType;

    public String getServerRootPath() {
        return serverRootPath;
    }

    public String getServerBaseurl() {
        return serverBaseurl;
    }

    public long getServerMaxSize() {
        return serverMaxSize;
    }

    public List<String> getServerSupportType() {
        return serverSupportType;
    }

    @Value("${picturebed.rootpath}")
    public void setRootPath(String rootPath) {
        log.info("set rootPath:{}", rootPath);
        ConfigReader.configReader.serverRootPath = rootPath;
    }

    @Value("${picturebed.baseurl}")
    public void setBaseurl(String baseurl) {
        log.info("set baseurl:{}", baseurl);
        ConfigReader.configReader.serverBaseurl = baseurl;
    }


    @Value("${picturebed.max-size}")
    public void setMaxSize(String maxSize) {
        // 转换为大写
        maxSize = maxSize.toUpperCase();
        if (!StringUtil.isValidSizeFormatString(maxSize)) {
            log.warn("picturebed.max-size不支持配置项:{}, 默认位置为10MB", maxSize);
            maxSize = "10MB";
        }
        log.info("set maxSize:{}", maxSize);
        long baseSize = Long.parseLong(maxSize.substring(0, maxSize.length() - 2));
        if (maxSize.endsWith("GB")) {
            ConfigReader.configReader.serverMaxSize = baseSize * 1024 * 1024 * 1024;
        } else if (maxSize.endsWith("MB")) {
            ConfigReader.configReader.serverMaxSize = baseSize * 1024 * 1024;
        } else {
            ConfigReader.configReader.serverMaxSize = baseSize * 1024;
        }
    }

    @Value("${picturebed.support-type}")
    public void setSupportType(String supportType) {
        log.info("set supportType:[{}]", supportType);
        // 转换为列表, 并且去除前后空白字符
        ConfigReader.configReader.serverSupportType = Arrays.stream(supportType.split(",")).map(String::strip)
                .collect(Collectors.toList());
    }
}
