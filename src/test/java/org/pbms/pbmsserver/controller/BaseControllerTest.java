package org.pbms.pbmsserver.controller;

import cn.hutool.json.JSONUtil;
import org.pbms.pbmsserver.common.auth.TokenBean;
import org.pbms.pbmsserver.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

/**
 * 基本的调用类，可以在这里写一些我们常用的调用方法（未完成）
 *
 * @author zqs
 */

@SpringBootTest
@AutoConfigureMockMvc
public class BaseControllerTest {

    @Autowired
    public MockMvc mockMvc;
    private static final String tokenHeader = "token";
    private static final String testToken = TokenUtil.generateToken(new TokenBean(1L, "admin"));

    ResultActions get(String url, Map<String, String> params, Map<String, Object> headers) throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get(url);
        if (null != params) {
            params.forEach((key, value) -> builder.param(key, value));
        }
        if (null != headers) {
            headers.forEach(builder::header);
        }
        return this.mockMvc.perform(builder);
    }

    ResultActions getByParams(String url, Map<String, String> params) throws Exception {
        return get(url, params, new HashMap<>());
    }

    ResultActions getByHeaders(String url) throws Exception {
        return get(url, new HashMap<>(), new HashMap<>());
    }

    ResultActions get(String url) throws Exception {
        return get(url, new HashMap<>(), new HashMap<>());
    }

    ResultActions deleteMockMvc(String url, Map<String, String> params, Map<String, Object> headers) throws Exception {
        MockHttpServletRequestBuilder builder = delete(url);
        params.forEach((key, value) -> {
            builder.param(key, value);
        });
        headers.forEach((key, value) -> {
            builder.header(key, value);
        });
        return this.mockMvc.perform(builder);
    }

    ResultActions post(String url, Map<String, String> params, Map<String, Object> headers, Object data) throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post(url);
        params.forEach((key, value) -> {
            builder.param(key, value);
        });
        headers.forEach((key, value) -> {
            builder.header(key, value);
        });
        return this.mockMvc.perform(builder.content(JSONUtil.toJsonStr(data)).contentType(MediaType.APPLICATION_JSON));
    }

    ResultActions post(String url, Object data) throws Exception {
        return post(url, new HashMap<>(), new HashMap<>(), data);
    }

    ResultActions putMockMvc(String url, Map<String, String> params, Map<String, Object> headers, Object data) throws Exception {
        MockHttpServletRequestBuilder builder = put(url);
        params.forEach((key, value) -> {
            builder.param(key, value);
        });
        headers.forEach((key, value) -> {
            builder.header(key, value);
        });
        return this.mockMvc.perform(builder.content(JSONUtil.toJsonStr(data)).contentType(MediaType.APPLICATION_JSON));
    }

    ResultActions patchMockMvc(String url, Map<String, String> params, Map<String, Object> headers, Object data) throws Exception {
        MockHttpServletRequestBuilder builder = patch(url);
        params.forEach((key, value) -> {
            builder.param(key, value);
        });
        headers.forEach((key, value) -> {
            builder.header(key, value);
        });
        return this.mockMvc.perform(builder.content(JSONUtil.toJsonStr(data)).contentType(MediaType.APPLICATION_JSON));
    }

}