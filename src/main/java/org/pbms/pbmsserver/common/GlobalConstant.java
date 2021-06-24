package org.pbms.pbmsserver.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * @author zyl
 * @Date 2021-06-21
 */

@Component
@Slf4j
public class GlobalConstant {

    public static String ROOT_PATH;

    @Value("${picturebed.rootpath:pictureBed}")
    public void setRootPath(String rootPath) {
        log.info("set rootPath:{}", rootPath);
        GlobalConstant.ROOT_PATH = rootPath;
    }

    public static String BASEURL;

    @Value("${picturebed.baseurl:http://127.0.0.1:8080}")
    public void setBaseurl(String baseurl) {
        log.info("set baseurl:{}", baseurl);
        GlobalConstant.BASEURL = baseurl;
    }
}
