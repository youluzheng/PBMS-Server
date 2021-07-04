package org.pbms.pbmsserver.init;

import java.io.File;

import org.pbms.pbmsserver.common.constant.ServerConstant;
import org.pbms.pbmsserver.common.constant.WaterMaskConstant;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * 创建初始文件夹
 *
 * @author 王俊
 * @Date 2021-06-21
 */
@Slf4j
@Component
public class Init implements CommandLineRunner {

    private void imageRepositoryInit() {
        log.info("检查{}是否存在...", ServerConstant.SERVER_ROOT_PATH);

        File rootPath = new File(ServerConstant.SERVER_ROOT_PATH);
        if (!rootPath.exists()) {
            log.info("{}不存在, 创建中...", ServerConstant.SERVER_ROOT_PATH);
            rootPath.mkdirs();
            log.info("{}创建完成", ServerConstant.SERVER_ROOT_PATH);
            return;
        }
        log.info("{}已经存在", ServerConstant.SERVER_ROOT_PATH);
    }

    private void watermaskRepositoryInit() {
        log.info("检查{}是否存在...", WaterMaskConstant.WATER_MASK_LOGO_PATH);

        File logoPath = new File(WaterMaskConstant.WATER_MASK_LOGO_PATH);
        if (!logoPath.exists()) {
            log.info("{}不存在, 创建中...", WaterMaskConstant.WATER_MASK_LOGO_PATH);
            logoPath.mkdirs();
            log.info("{}创建完成", WaterMaskConstant.WATER_MASK_LOGO_PATH);
            return;
        }
        log.info("{}已经存在", WaterMaskConstant.WATER_MASK_LOGO_PATH);
    }

    @Override
    public void run(String... args) throws Exception {
        this.imageRepositoryInit();
        this.watermaskRepositoryInit();
    }

}
