package org.pbms.pbmsserver.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.pbms.pbmsserver.common.auth.TokenBean;
import org.pbms.pbmsserver.common.request.tempToken.TempTokenAddDTO;
import org.pbms.pbmsserver.repository.enumeration.user.UserRoleEnum;
import org.pbms.pbmsserver.repository.mapper.TempTokenInfoMapper;
import org.pbms.pbmsserver.repository.model.TempTokenInfo;
import org.pbms.pbmsserver.repository.model.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TempTokenControllerTest extends BaseControllerTest {

    private static final Logger log = LoggerFactory.getLogger(TempTokenControllerTest.class);

    @Autowired
    TempTokenInfoMapper tempTokenInfoMapper;

    private UserInfo admin;

    @Override
    protected TokenBean getTokenBean() {
        return new TokenBean(this.admin.getUserId(), this.admin.getUserName(), UserRoleEnum.transform(this.admin.getRole()));
    }

    @BeforeEach
    void setup() {
        this.tempTokenInfoMapper.delete(c -> c);
        this.userInfoMapper.delete(c -> c);

        this.admin = this.insertDefaultAdmin();
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
        TempTokenAddDTO addTempTokenReq = new TempTokenAddDTO();
        addTempTokenReq.setUploadTimes(uploadTimes);
        addTempTokenReq.setExpireTime(expireDays);
        this.post("/tempToken", addTempTokenReq).andExpect(status().isBadRequest());
    }

    // 新增addTempToken正确返回
    @Test
    void addTempTokenInfo() throws Exception {
        Date now = new Date();
        String dateStr = DateUtil.year(now) + "-12-31";
        Date date = DateUtil.parse(dateStr, "yyyy-MM-dd");
        TempTokenAddDTO req = new TempTokenAddDTO();
        req.setUploadTimes(100);
        req.setNote("给大家用用");
        req.setExpireTime(date);
        this.post("/tempToken", req).andExpect(status().isOk());
        log.debug("{}", tempTokenInfoMapper.select(c -> c));
    }

    private TempTokenInfo insertTempToken() {
        TempTokenInfo tempTokenInfo = new TempTokenInfo();
        tempTokenInfo.setUserId(this.admin.getUserId());
        tempTokenInfo.setUploadTimes(100);
        tempTokenInfo.setNote("随便写一些");
        Date now = new Date();
        String dateStr = (DateUtil.year(now) + 1) + "-12-31";
        Date date = DateUtil.parse(dateStr, "yyyy-MM-dd");
        tempTokenInfo.setExpireTime(date);
        tempTokenInfo.setSecretKey(UUID.randomUUID().toString().trim().replaceAll("-", ""));
        tempTokenInfoMapper.insert(tempTokenInfo);
        return tempTokenInfo;
    }

    private TempTokenInfo copyOneFromOtherUser() {
        TempTokenInfo tempTokenInfo = this.insertTempToken();
        tempTokenInfo.setUserId(tempTokenInfo.getUserId() + 1);
        tempTokenInfoMapper.insert(tempTokenInfo);
        return tempTokenInfo;
    }

    // 要求： 无法删除其他用户创建的临时token
    @Test
    void deleteTempToken_notMyToken() throws Exception {
        TempTokenInfo tempTokenInfo = this.copyOneFromOtherUser();
        List<TempTokenInfo> before = tempTokenInfoMapper.select(c -> c);
        this.delete("/tempToken/" + tempTokenInfo.getId()).andExpect(status().isOk()).andReturn();
        List<TempTokenInfo> after = tempTokenInfoMapper.select(c -> c);
        assertEquals(before.size(), after.size());
    }

    // 删除非正常id的token
    @Test
    void deleteTempToken_invalidTokenId() throws Exception {
        this.copyOneFromOtherUser();
        List<TempTokenInfo> before = tempTokenInfoMapper.select(c -> c);
        this.delete("/tempToken/0").andExpect(status().isOk()).andReturn();
        List<TempTokenInfo> after = tempTokenInfoMapper.select(c -> c);
        assertEquals(before.size(), after.size());
    }

    //
    @Test
    void deleteTempToken() throws Exception {
        TempTokenInfo tempTokenInfo = this.insertTempToken();
        List<TempTokenInfo> before = tempTokenInfoMapper.select(c -> c);
        this.delete("/tempToken/" + tempTokenInfo.getId()).andExpect(status().isOk()).andReturn();
        List<TempTokenInfo> after = tempTokenInfoMapper.select(c -> c);
        assertEquals(before.size() - 1, after.size());
    }

    @Test
    void listTempToken_empty() throws Exception {
        this.get("/tempToken").andExpect(status().isOk()).andExpect(content().json(JSONUtil.toJsonStr(Lists.emptyList())));
    }

    @Test
    void listTempToken() throws Exception {
        TempTokenInfo tempTokenInfo = this.insertTempToken();
        this.get("/tempToken").andExpect(status().isOk()).andExpect(content().json(JSONUtil.toJsonStr(Lists.list(tempTokenInfo))));
    }

}