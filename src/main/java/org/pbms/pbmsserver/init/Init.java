package org.pbms.pbmsserver.init;

import org.pbms.pbmsserver.common.constant.ServerConstant;
import org.pbms.pbmsserver.common.exception.ServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;


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

    @Override
    public void run(String... args) {
        this.imageRepositoryInit();
    }
}
