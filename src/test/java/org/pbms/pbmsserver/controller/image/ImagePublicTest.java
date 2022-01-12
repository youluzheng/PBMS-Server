package org.pbms.pbmsserver.controller.image;

import cn.hutool.core.date.DateUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pbms.pbmsserver.common.auth.TokenBean;
import org.pbms.pbmsserver.common.exception.BusinessStatus;
import org.pbms.pbmsserver.controller.BaseControllerTest;
import org.pbms.pbmsserver.repository.mapper.TempTokenInfoMapper;
import org.pbms.pbmsserver.repository.mapper.UserSettingsMapper;
import org.pbms.pbmsserver.repository.model.TempTokenInfo;
import org.pbms.pbmsserver.repository.model.UserInfo;
import org.pbms.pbmsserver.repository.model.UserSettings;
import org.pbms.pbmsserver.service.lifecycle.upload.SaveProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.pbms.pbmsserver.common.exception.BusinessStatus.TEMP_TOKEN_ERROR;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 图片上传测试类
 *
 * @author zqs
 */
class ImagePublicTest extends BaseControllerTest {

    private static final Logger log = LoggerFactory.getLogger(ImagePublicTest.class);

    private final MockMultipartFile image = new MockMultipartFile("image",
            "test",
            MediaType.IMAGE_JPEG.toString(),
            new byte[]{
                    -1, -40, -1, -32
            }
    );
    @SpyBean
    private SaveProcessor saveProcessor;
    @Autowired
    private TempTokenInfoMapper tempTokenInfoMapper;
    @Autowired
    private UserSettingsMapper userSettingsMapper;

    private UserInfo user;

    @Override
    protected TokenBean getTokenBean() {
        return null;
    }

    @BeforeEach
    private void setUp() {
        this.tempTokenInfoMapper.delete(c -> c);
        this.userSettingsMapper.delete(c -> c);
        this.userInfoMapper.delete(c -> c);

        this.user = this.insertDefaultAdmin();

        UserSettings userSettings = new UserSettings();
        userSettings.setUserId(this.user.getUserId());
        userSettings.setWatermarkLogoEnable(false);
        userSettings.setWatermarkLogoRepeat(false);
        userSettings.setWatermarkLogoGradient((byte) 0);
        userSettings.setWatermarkLogoAlpha((byte) 0);
        userSettings.setWatermarkTextEnable(false);
        userSettings.setWatermarkTextContent("unbelievable!");
        userSettings.setWatermarkTextAlpha((byte) 0);
        userSettings.setCompressScale((byte) 0);
        userSettings.setResponseReturnType("markdown");
        userSettingsMapper.insert(userSettings);

        doNothing().when(saveProcessor).save(any(), any(), any());
    }

    TempTokenInfo insertTempToken(String stringDate, int times) {
        TempTokenInfo tempTokenInfo = new TempTokenInfo();
        tempTokenInfo.setUserId(this.user.getUserId());
        tempTokenInfo.setUploadTimes(times);
        Date date = DateUtil.parse(stringDate, "yyyy-MM-dd");
        tempTokenInfo.setExpireTime(date);
        tempTokenInfo.setNote("随便写一些");
        tempTokenInfo.setSecretKey(UUID.randomUUID().toString().trim().replaceAll("-", ""));
        tempTokenInfoMapper.insert(tempTokenInfo);
        return tempTokenInfo;
    }

    // 正常情况,传输10张
    @Test
    void uploadImageByTempToken() throws Exception {
        String stringDate = (DateUtil.year(new Date()) + 1) + "-12-31";
        TempTokenInfo before = this.insertTempToken(stringDate, 100);
        ArrayList<MockMultipartFile> multipartFiles = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            multipartFiles.add(image);
        }
        this.filePost("/image/" + before.getSecretKey(), multipartFiles)
                .andExpect(status().isOk());
        TempTokenInfo after = tempTokenInfoMapper.selectByPrimaryKey(before.getId()).get();
        log.debug("上传后信息：{}", after);
        assertEquals(after.getUploadTimes() + 10, before.getUploadTimes());
    }

    // token过期
    @Test
    void uploadImageByTempToken_expireTimeError() throws Exception {
        TempTokenInfo before = this.insertTempToken("2000-01-01", 100);
        log.debug("上传前信息：{}", before);
        this.filePost("/image/" + before.getSecretKey(), image)
                .andExpect(status().is(403))
                .andExpect(content().string(BusinessStatus.TEMP_TOKEN_EXPIRE.toString()));
    }

    // 次数为0，无法再次上传
    @Test
    void uploadImageByTempToken_timeExceed1() throws Exception {
        String stringDate = (DateUtil.year(new Date()) + 1) + "-12-31";
        TempTokenInfo before = this.insertTempToken(stringDate, 0);
        log.debug("上传前信息：{}", before);
        this.filePost("/image/" + before.getSecretKey(), image)
                .andExpect(status().is(403))
                .andExpect(content().string(BusinessStatus.TEMP_TOKEN_UPLOAD_TIMES_EXCEED.toString()));
    }

    // 多张上传，次数不够
    @Test
    void uploadImageByTempToken_timeExceed2() throws Exception {
        String stringDate = (DateUtil.year(new Date()) + 1) + "-12-31";
        TempTokenInfo before = this.insertTempToken(stringDate, 5);
        log.debug("上传前信息：{}", before);
        ArrayList<MockMultipartFile> multipartFiles = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            multipartFiles.add(image);
        }
        this.filePost("/image/" + before.getSecretKey(), multipartFiles)
                .andExpect(status().is(403))
                .andExpect(content().string(BusinessStatus.TEMP_TOKEN_UPLOAD_TIMES_EXCEED.toString()));
        TempTokenInfo after = tempTokenInfoMapper.selectByPrimaryKey(before.getId()).get();
        log.debug("上传后信息：{}", after);
        assertEquals(after.getUploadTimes(), before.getUploadTimes());
    }

    // token错误
    @Test
    void uploadImageByTempToken_tokenError() throws Exception {
        String stringDate = (DateUtil.year(new Date()) + 1) + "-12-31";
        TempTokenInfo before = this.insertTempToken(stringDate, 5);
        log.debug("上传前信息：{}", before);
        this.filePost("/image/" + UUID.randomUUID().toString().trim().replaceAll("-", ""), image)
                .andExpect(status().is(403))
                .andExpect(content().string(TEMP_TOKEN_ERROR.toString()));
    }
}