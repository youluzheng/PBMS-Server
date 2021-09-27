package org.pbms.pbmsserver.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.pbms.pbmsserver.common.auth.TokenBean;
import org.pbms.pbmsserver.common.request.tempToken.TempTokenAddReq;
import org.pbms.pbmsserver.dao.TempTokenInfoDao;
import org.pbms.pbmsserver.dao.UserInfoDao;
import org.pbms.pbmsserver.repository.enumeration.user.UserStatusEnum;
import org.pbms.pbmsserver.repository.model.TempTokenInfo;
import org.pbms.pbmsserver.repository.model.UserInfo;
import org.pbms.pbmsserver.util.EncryptUtil;
import org.pbms.pbmsserver.util.TokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TempTokenControllerTest {

    private static final Logger log = LoggerFactory.getLogger(TempTokenControllerTest.class);

    @Autowired
    private TempTokenInfoDao tempTokenInfoDao;
    @Autowired
    UserInfoDao userInfoDao;
    @Autowired
    MockMvc mockMvc;
    private static final String tokenHeader = "token";
    private static final String testToken = TokenUtil.generateToken(new TokenBean(1L, "admin"));

    @BeforeEach
    void setUp() {
        this.userInfoDao.delete(c -> c);
        this.tempTokenInfoDao.delete(c -> c);
        this.insertUser();
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

    private UserInfo insertUser() {
        UserInfo user = new UserInfo();
        user.setUserName("admin");
        user.setPassword(EncryptUtil.sha512("123456"));
        user.setEmail("zyl@965.life");
        user.setCreateTime(new Date());
        user.setStatus(UserStatusEnum.NORMAL.getCode());
        this.userInfoDao.insert(user);
        return user;
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
        tempTokenInfoDao.insert(tempTokenInfo);
        return tempTokenInfo;
    }

    // 参数注解校验
    @ParameterizedTest
    @MethodSource("addTempToken_invalidValues")
    void addTempToken(Date expireDays, Integer uploadTimes) throws Exception {
        TempTokenAddReq addTempTokenReq = new TempTokenAddReq();
        addTempTokenReq.setUploadTimes(uploadTimes);
        addTempTokenReq.setExpireTime(expireDays);
        this.mockMvc.perform(post("/tempToken")
                .content(JSONUtil.toJsonStr(addTempTokenReq))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is4xxClientError());
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
        MvcResult mvcResult = mockMvc.perform(post("/tempToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSONUtil.toJsonStr(req))
                .header(tokenHeader, testToken)
                .header("Content-Type","charset=utf-8")
        ).andExpect(status().isOk()).andReturn();
        log.debug("{}", mvcResult.getResponse().getContentAsString());
    }

    @Test
    void deleteTempToken() throws Exception {
        this.insertTempToken();
        MvcResult mvcResult = mockMvc.perform(delete("/tempToken")
                .param("tokenId", "1")
                .header(tokenHeader, testToken)
        ).andExpect(status().isOk()).andReturn();
        log.debug("{}", mvcResult.getResponse());
    }

}