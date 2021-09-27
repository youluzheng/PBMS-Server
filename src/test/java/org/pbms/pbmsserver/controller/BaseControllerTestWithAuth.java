package org.pbms.pbmsserver.controller;

import cn.hutool.json.JSONUtil;
import org.junit.jupiter.api.BeforeEach;
import org.pbms.pbmsserver.common.auth.TokenBean;
import org.pbms.pbmsserver.repository.enumeration.user.UserStatusEnum;
import org.pbms.pbmsserver.repository.mapper.UserInfoMapper;
import org.pbms.pbmsserver.repository.model.UserInfo;
import org.pbms.pbmsserver.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 基本的调用类，可以在这里写一些我们常用的调用方法
 *
 * @author zqs
 * @author zyl
 */

@SpringBootTest
@AutoConfigureMockMvc
public abstract class BaseControllerTestWithAuth {

    @Autowired
    public MockMvc mockMvc;
    private final String tokenHeader = "token";
    private String testToken;

    protected UserInfo userInfo;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @BeforeEach
    protected void setupInner() {
        this.setup();
        if (this.userInfo != null) {
            this.userInfoMapper.deleteByPrimaryKey(this.userInfo.getUserId());
        }
        this.userInfo = new UserInfo();
        this.userInfo.setPassword("xxxxxxx");
        this.userInfo.setUserName("admin");
        this.userInfo.setEmail("zyl@965.life");
        this.userInfo.setCreateTime(new Date());
        this.userInfo.setStatus(UserStatusEnum.NORMAL.getCode());
        this.userInfoMapper.insert(userInfo);
        this.testToken = TokenUtil.generateToken(new TokenBean(this.userInfo.getUserId(), "admin"));
    }

    protected abstract void setup();

    protected ResultActions get(String url, Map<String, String> params, Map<String, Object> headers) throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get(url);
        builder.header(this.tokenHeader, this.testToken);
        if (null != params) {
            params.forEach(builder::param);
        }
        if (null != headers) {
            headers.forEach(builder::header);
        }
        return this.mockMvc.perform(builder);
    }

    protected ResultActions get(String url, String paramKey1, String paramValue1) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put(paramKey1, paramValue1);
        return get(url, params, null);
    }

    protected ResultActions get(String url, String paramKey1, String paramValue1, String paramKey2, String paramValue2) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put(paramKey1, paramValue1);
        params.put(paramKey2, paramValue2);
        return get(url, params, null);
    }

    protected ResultActions get(String url, Map<String, String> params) throws Exception {
        return get(url, params, null);
    }


    protected ResultActions get(String url) throws Exception {
        return get(url, new HashMap<>(), null);
    }

    protected ResultActions delete(String url, Map<String, String> params, Map<String, Object> headers) throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.delete(url);
        builder.header(this.tokenHeader, this.testToken);
        if (null != params) {
            params.forEach(builder::param);
        }
        if (null != headers) {
            headers.forEach(builder::header);
        }
        return this.mockMvc.perform(builder);
    }

    protected ResultActions delete(String url, String paramKey1, String paramValue1) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put(paramKey1, paramValue1);
        return delete(url, params, null);
    }

    protected ResultActions delete(String url, String paramKey1, String paramValue1, String paramKey2, String paramValue2) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put(paramKey1, paramValue1);
        params.put(paramKey2, paramValue2);
        return delete(url, params, null);
    }

    protected ResultActions delete(String url, Map<String, String> params) throws Exception {
        return delete(url, params, null);
    }


    protected ResultActions delete(String url) throws Exception {
        return delete(url, new HashMap<>(), new HashMap<>());
    }

    protected ResultActions post(String url, Map<String, String> params, Map<String, Object> headers, Object data, MediaType mediaType) throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post(url);
        builder.header(this.tokenHeader, this.testToken);
        if (null != params) {
            params.forEach(builder::param);
        }
        if (null != headers) {
            headers.forEach(builder::header);
        }
        return this.mockMvc.perform(builder.content(JSONUtil.toJsonStr(data)).contentType(mediaType));
    }

    protected ResultActions post(String url, String paramKey1, String paramValue1, Object data) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put(paramKey1, paramValue1);
        return post(url, params, null, data, MediaType.APPLICATION_JSON);
    }

    protected ResultActions post(String url, Map<String, String> params, Object data) throws Exception {
        return post(url, params, null, data, MediaType.APPLICATION_JSON);
    }

    protected ResultActions post(String url, Object data, MediaType mediaType) throws Exception {
        return post(url, null, null, data, mediaType);
    }

    protected ResultActions post(String url, Object data) throws Exception {
        return post(url, null, null, data, MediaType.APPLICATION_JSON);
    }

    protected ResultActions put(String url, Map<String, String> params, Map<String, Object> headers, Object data, MediaType mediaType) throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.put(url);
        builder.header(this.tokenHeader, this.testToken);
        if (null != params) {
            params.forEach(builder::param);
        }
        if (null != headers) {
            headers.forEach(builder::header);
        }
        return this.mockMvc.perform(builder.content(JSONUtil.toJsonStr(data)).contentType(mediaType));
    }

    protected ResultActions put(String url, String paramKey1, String paramValue1, Object data) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put(paramKey1, paramValue1);
        return put(url, params, null, data, MediaType.APPLICATION_JSON);
    }

    protected ResultActions put(String url, Map<String, String> params, Object data) throws Exception {
        return put(url, params, null, data, MediaType.APPLICATION_JSON);
    }

    protected ResultActions put(String url, Object data, MediaType mediaType) throws Exception {
        return put(url, null, null, data, mediaType);
    }

    protected ResultActions put(String url, Object data) throws Exception {
        return put(url, null, null, data, MediaType.APPLICATION_JSON);
    }

    protected ResultActions patch(String url, Map<String, String> params, Map<String, Object> headers, Object data, MediaType mediaType) throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.patch(url);
        builder.header(this.tokenHeader, this.testToken);
        if (null != params) {
            params.forEach(builder::param);
        }
        if (null != headers) {
            headers.forEach(builder::header);
        }
        return this.mockMvc.perform(builder.content(JSONUtil.toJsonStr(data)).contentType(mediaType));
    }

    protected ResultActions patch(String url, String paramKey1, String paramValue1, Object data) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put(paramKey1, paramValue1);
        return patch(url, params, null, data, MediaType.APPLICATION_JSON);
    }

    protected ResultActions patch(String url, Map<String, String> params, Object data) throws Exception {
        return patch(url, params, null, data, MediaType.APPLICATION_JSON);
    }

    protected ResultActions patch(String url, Object data, MediaType mediaType) throws Exception {
        return patch(url, null, null, data, mediaType);
    }

    protected ResultActions patch(String url, Object data) throws Exception {
        return patch(url, null, null, data, MediaType.APPLICATION_JSON);
    }

}