package org.pbms.pbmsserver.service.lifecycle.before;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.name.Rename;
import org.pbms.pbmsserver.common.auth.TokenBean;
import org.pbms.pbmsserver.common.exception.ServerException;
import org.pbms.pbmsserver.init.Init;
import org.pbms.pbmsserver.repository.model.UserSettings;
import org.pbms.pbmsserver.service.UserService;
import org.pbms.pbmsserver.util.MultipartFileUtil;
import org.pbms.pbmsserver.util.TokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 图片压缩业务处理
 *
 * @author 王俊
 * @date 2021/7/10 14:31
 */
@Component
public class CompressProcessor {

    @Autowired
    private UserService userService;

    private static final Logger log = LoggerFactory.getLogger(CompressProcessor.class);

    /**
     * @param srcImg 原始图片
     * @return 压缩后图片，类型为MultipartFile
     */
    public MultipartFile compress(MultipartFile srcImg) {
        TokenBean tokenBean = TokenUtil.getTokenBean();
        UserSettings userSettings = this.userService.getSettings();
        if (userSettings.getCompressScale() == 0) {
            return srcImg;
        }
        File tempFile = new File(Init.getRespectiveAbsoluteTempPath(tokenBean) + File.separator
                + srcImg.getOriginalFilename());
        try {
            srcImg.transferTo(tempFile);
        } catch (IOException e) {
            log.error("文件转换处理异常, {}", e.getMessage());
            throw new ServerException("文件转换处理异常");
        }
        String compressPath = generateThumbnail2Directory(userSettings.getCompressScale() / 100D, tempFile.getParent(),
                tempFile.getAbsolutePath()).get(0);
        File compressImg = new File(compressPath);
        // 文件类型转换，方便后续处理
        MultipartFile result = MultipartFileUtil.fileToMultipartFile(compressImg);
        // 删除临时文件
        try {
            Files.delete(compressImg.toPath());
            Files.delete(tempFile.toPath());
        } catch (Exception e) {
            log.warn("文件删除失败, {}", e.getMessage());
        }
        return result;
    }

    /**
     * 生成缩略图到指定的目录
     *
     * @param scale    图片缩放率
     * @param pathname 缩略图保存目录
     * @param files    要生成缩略图的文件列表
     */
    private List<String> generateThumbnail2Directory(double scale, String pathname, String... files) {
        try {
            Thumbnails.of(files)
                    // 图片缩放率，不能和size()一起使用
                    .scale(scale)
                    // 缩略图保存目录,该目录需存在，否则报错
                    .toFiles(new File(pathname), Rename.NO_CHANGE);
        } catch (IOException e) {
            log.error("文件压缩处理异常, {}", e.getMessage());
            throw new ServerException("文件压缩处理异常");
        }
        List<String> list = new ArrayList<>(files.length);
        list.addAll(Arrays.asList(files));
        return list;
    }
}
