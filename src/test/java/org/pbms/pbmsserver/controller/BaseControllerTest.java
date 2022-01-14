package org.pbms.pbmsserver.controller;

import cn.hutool.json.JSONUtil;
import org.assertj.core.util.Lists;
import org.pbms.pbmsserver.common.auth.TokenBean;
import org.pbms.pbmsserver.common.auth.TokenHandler;
import org.pbms.pbmsserver.common.constant.ServerConstant;
import org.pbms.pbmsserver.common.exception.ServerException;
import org.pbms.pbmsserver.repository.enumeration.user.UserRoleEnum;
import org.pbms.pbmsserver.repository.enumeration.user.UserStatusEnum;
import org.pbms.pbmsserver.repository.mapper.UserInfoMapper;
import org.pbms.pbmsserver.repository.model.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * 基本的调用类，可以在这里写一些我们常用的调用方法
 *
 * @author zqs
 * @author zyl
 */

@SpringBootTest
@AutoConfigureMockMvc
public abstract class BaseControllerTest {

    private static final Logger log = LoggerFactory.getLogger(BaseControllerTest.class);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    protected UserInfoMapper userInfoMapper;

    /**
     * 通过重写此方法判断是否需要认证，null表示不需要
     *
     * @return tokenBean
     */
    protected abstract TokenBean getTokenBean();

    /**
     * 插入管理员
     *
     * @return 管理员
     */
    public UserInfo insertDefaultAdmin() {
        UserInfo userInfo = new UserInfo();
        userInfo.setPassword(ServerConstant.HASH_METHOD.apply("123456"));
        userInfo.setUserName("admin");
        userInfo.setEmail("zyl@965.life");
        userInfo.setCreateTime(new Date());
        userInfo.setStatus(UserStatusEnum.NORMAL.getCode());
        userInfo.setRole(UserRoleEnum.ADMIN.getCode());
        this.userInfoMapper.insert(userInfo);
        return userInfo;
    }

    /**
     * 插入普通用户
     *
     * @return 普通用户
     */
    public UserInfo insertDefaultUser() {
        UserInfo userInfo = new UserInfo();
        userInfo.setPassword(ServerConstant.HASH_METHOD.apply("123456"));
        userInfo.setUserName("user");
        userInfo.setEmail("zyl@965.life");
        userInfo.setCreateTime(new Date());
        userInfo.setStatus(UserStatusEnum.NORMAL.getCode());
        userInfo.setRole(UserRoleEnum.NORMAL.getCode());
        userInfoMapper.insert(userInfo);
        return userInfo;
    }

    /**
     * 重写改方法，实现自动添加baseurl
     *
     * @param url 目标url
     * @return 处理后的url
     */
    protected String setUrl(String url) {
        return url;
    }

    private MockHttpServletRequestBuilder prepare(String url, Function<String, MockHttpServletRequestBuilder> method) {
        MockHttpServletRequestBuilder builder = method.apply(this.setUrl(url)).servletPath(this.setUrl(url));
        TokenBean tokenBean = this.getTokenBean();
        if (tokenBean != null) {
            builder.header("token", TokenHandler.generateToken(tokenBean));
        }
        return builder;
    }

    private void setParams(MockHttpServletRequestBuilder builder, Map<String, String> params) {
        if (null != params) {
            params.forEach(builder::param);
        }
    }

    private void setData(MockHttpServletRequestBuilder builder, Object data) {
        if (null != data) {
            builder.content(JSONUtil.toJsonStr(data));
            builder.contentType(MediaType.APPLICATION_JSON);
        }
    }

    private void setFiles(MockMultipartHttpServletRequestBuilder builder, List<MockMultipartFile> files) {
        if (files != null) {
            files.forEach(builder::file);
        }
    }

    private ResultActions perform(MockHttpServletRequestBuilder builder) {
        try {
            return this.mockMvc.perform(builder);
        } catch (Exception e) {
            throw new ServerException(e.getMessage());
        }
    }

    protected ResultActions get(String url, Map<String, String> params) {
        MockHttpServletRequestBuilder builder = this.prepare(url, MockMvcRequestBuilders::get);
        setParams(builder, params);
        return this.perform(builder);
    }

    protected ResultActions get(String url, String paramKey1, String paramValue1) {
        Map<String, String> params = new HashMap<>();
        params.put(paramKey1, paramValue1);
        return get(url, params);
    }

    protected ResultActions get(String url, String paramKey1, String paramValue1, String paramKey2, String paramValue2) {
        Map<String, String> params = new HashMap<>();
        params.put(paramKey1, paramValue1);
        params.put(paramKey2, paramValue2);
        return get(url, params);
    }

    protected ResultActions get(String url) {
        return get(url, null);
    }

    protected ResultActions delete(String url, Map<String, String> params) {
        MockHttpServletRequestBuilder builder = this.prepare(url, MockMvcRequestBuilders::delete);
        setParams(builder, params);
        return this.perform(builder);
    }

    protected ResultActions delete(String url, String paramKey1, String paramValue1) {
        Map<String, String> params = new HashMap<>();
        params.put(paramKey1, paramValue1);
        return delete(url, params);
    }

    protected ResultActions delete(String url) {
        return delete(url, null);
    }

    protected ResultActions post(String url, Map<String, String> params, Object data) {
        MockHttpServletRequestBuilder builder = this.prepare(url, MockMvcRequestBuilders::post);
        setParams(builder, params);
        setData(builder, data);
        return this.perform(builder);
    }

    protected ResultActions post(String url, Object data) {
        return post(url, null, data);
    }

    protected ResultActions post(String url) {
        return post(url, null, null);
    }

    protected ResultActions filePost(String url, Map<String, String> params, List<MockMultipartFile> files) {
        MockMultipartHttpServletRequestBuilder builder = (MockMultipartHttpServletRequestBuilder) this.prepare(url, MockMvcRequestBuilders::multipart);
        setParams(builder, params);
        setFiles(builder, files);
        return this.perform(builder);
    }

    protected ResultActions filePost(String url, List<MockMultipartFile> files) {
        return this.filePost(url, null, files);
    }

    protected ResultActions filePost(String url, MockMultipartFile file) {
        return this.filePost(url, null, Lists.list(file));
    }

    protected ResultActions put(String url, Map<String, String> params, Object data) {
        MockHttpServletRequestBuilder builder = this.prepare(url, MockMvcRequestBuilders::put);
        setParams(builder, params);
        setData(builder, data);
        return this.perform(builder);
    }

    protected ResultActions put(String url, Object data) {
        return put(url, null, data);
    }

    protected ResultActions put(String url) {
        return put(url, null, null);
    }


    protected ResultActions patch(String url, Map<String, String> params, Object data) {
        MockHttpServletRequestBuilder builder = this.prepare(url, MockMvcRequestBuilders::patch);
        setParams(builder, params);
        setData(builder, data);
        return this.perform(builder);
    }

    protected ResultActions patch(String url, Map<String, String> params) {
        return patch(url, params, null);
    }

    protected ResultActions patch(String url, Object data) {
        return patch(url, null, data);
    }

    protected ResultActions patch(String url) {
        return patch(url, null, null);
    }
}