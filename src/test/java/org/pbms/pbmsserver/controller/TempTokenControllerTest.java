package org.pbms.pbmsserver.controller;

import cn.hutool.core.date.DateUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.pbms.pbmsserver.common.request.tempToken.TempTokenAddReq;
import org.pbms.pbmsserver.repository.mapper.TempTokenInfoMapper;
import org.pbms.pbmsserver.repository.mapper.UserInfoMapper;
import org.pbms.pbmsserver.repository.model.TempTokenInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TempTokenControllerTest extends BaseControllerTestWithAuth {

    private static final Logger log = LoggerFactory.getLogger(TempTokenControllerTest.class);

    @Autowired
    TempTokenInfoMapper tempTokenInfoMapper;
    @Autowired
    UserInfoMapper userInfoMapper;

    @Override
    protected void setup() {
        this.userInfoMapper.delete(c -> c);
        this.tempTokenInfoMapper.delete(c -> c);
    }

    private static Stream<Arguments> addTempToken_invalidValues() {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of(null, 0),
                Arguments.of(null, 1),
                Arguments.of(new Date(), null),
                Arguments.of(new Date(), 0)
        );
    }

    // 参数注解校验
    @ParameterizedTest
    @MethodSource("addTempToken_invalidValues")
    void addTempToken(Date expireDays, Integer uploadTimes) throws Exception {
        TempTokenAddReq addTempTokenReq = new TempTokenAddReq();
        addTempTokenReq.setUploadTimes(uploadTimes);
        addTempTokenReq.setExpireTime(expireDays);
        this.post("/tempToken", addTempTokenReq).andExpect(status().isBadRequest());
    }

    // 返回校验
    @Test
    void addTempTokenInfo() throws Exception {
        Date now = new Date();
        String dateStr = DateUtil.year(now) + "-12-31";
        Date date = DateUtil.parse(dateStr, "yyyy-MM-dd");
        TempTokenAddReq req = new TempTokenAddReq();
        req.setUploadTimes(100);
        req.setNote("给大家用用");
        req.setExpireTime(date);
        this.post("/tempToken", req).andExpect(status().isOk());
    }

    private TempTokenInfo insertTempToken() {
        TempTokenInfo tempTokenInfo = new TempTokenInfo();
        tempTokenInfo.setUserId(1L);
        tempTokenInfo.setUploadTimes(100);
        tempTokenInfo.setNote("随便写一些");
        Date now = new Date();
        String dateStr = DateUtil.year(now) + "-12-31";
        Date date = DateUtil.parse(dateStr, "yyyy-MM-dd");
        tempTokenInfo.setExpireTime(date);
        tempTokenInfo.setSecretKey(UUID.randomUUID().toString().trim().replaceAll("-", ""));
        tempTokenInfoMapper.insert(tempTokenInfo);
        return tempTokenInfo;
    }

    @Test
    void deleteTempToken() throws Exception {
        TempTokenInfo tempTokenInfo = this.insertTempToken();
        this.delete("/tempToken/" + tempTokenInfo.getId()).andExpect(status().isOk()).andReturn();
    }

}