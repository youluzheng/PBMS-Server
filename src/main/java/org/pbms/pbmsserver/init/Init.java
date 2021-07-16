package org.pbms.pbmsserver.init;

import java.io.File;

import org.pbms.pbmsserver.common.constant.ServerConstant;
import org.pbms.pbmsserver.common.constant.WaterMarkConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 创建初始文件夹
 *
 * @author 王俊
 * @Date 2021-06-21
 */
@Component
public class Init implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(Init.class);

    private void imageRepositoryInit() {
        log.info("检查{}是否存在...", ServerConstant.SERVER_ROOT_PATH);

        File rootPath = new File(ServerConstant.SERVER_ROOT_PATH);
        if (!rootPath.exists()) {
            log.warn("{}不存在, 创建中...", ServerConstant.SERVER_ROOT_PATH);
            rootPath.mkdirs();
            log.info("{}创建完成", ServerConstant.SERVER_ROOT_PATH);
            return;
        }
        log.info("{}已经存在", ServerConstant.SERVER_ROOT_PATH);
    }

    private void compressTempDirInit() {
        log.info("检查{}是否存在...", ServerConstant.SERVER_ROOT_PATH + File.separator + "temp");

        // 创建临时文件目录，用于存放一些临时文件，目前只有压缩需要，若有其他用途，请在该目录下再创目录
        File rootPath = new File(ServerConstant.SERVER_ROOT_PATH + File.separator + "temp");
        if (!rootPath.exists()) {
            log.warn("{}不存在, 创建中...", ServerConstant.SERVER_ROOT_PATH + File.separator + "temp");
            rootPath.mkdirs();
            log.info("{}创建完成", ServerConstant.SERVER_ROOT_PATH + File.separator + "temp");
            return;
        }
        log.info("{}已经存在", ServerConstant.SERVER_ROOT_PATH + File.separator + "temp");
    }

    private void waterMarkRepositoryInit() {
        log.info("检查{}是否存在...", WaterMarkConstant.WATER_MARK_LOGO_PATH);

        File logoPath = new File(WaterMarkConstant.WATER_MARK_LOGO_PATH);
        if (!logoPath.exists()) {
            log.warn("{}不存在, 创建中...", WaterMarkConstant.WATER_MARK_LOGO_PATH);
            logoPath.mkdirs();
            log.info("{}创建完成", WaterMarkConstant.WATER_MARK_LOGO_PATH);
            return;
        }
        log.info("{}已经存在", WaterMarkConstant.WATER_MARK_LOGO_PATH);
    }

    @Override
    public void run(String... args) throws Exception {
        this.imageRepositoryInit();
        this.waterMarkRepositoryInit();
        this.compressTempDirInit();
    }

}
