package org.pbms.pbmsserver.init;

import java.io.File;

import org.pbms.pbmsserver.common.GlobalConstant;
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
        log.info("检查{}是否存在...", GlobalConstant.ROOT_PATH);

        File rootPath = new File(GlobalConstant.ROOT_PATH);
        if (!rootPath.exists()) {
            log.info("{}不存在, 创建中...", GlobalConstant.ROOT_PATH);
            rootPath.mkdirs();
            log.info("{}创建完成", GlobalConstant.ROOT_PATH);
            return;
        }
        log.info("{}已经存在", GlobalConstant.ROOT_PATH);
    }

    @Override
    public void run(String... args) throws Exception {
        this.imageRepositoryInit();
    }
}
