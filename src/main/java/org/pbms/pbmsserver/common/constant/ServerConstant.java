package org.pbms.pbmsserver.common.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * @author zyl
 * @Date 2021-06-21
 */

@Component
@Slf4j
public class ServerConstant {

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

}