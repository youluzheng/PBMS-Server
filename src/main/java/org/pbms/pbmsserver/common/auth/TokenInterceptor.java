package org.pbms.pbmsserver.common.auth;

import org.pbms.pbmsserver.common.exception.UnauthorizedException;
import org.pbms.pbmsserver.repository.enumeration.user.UserStatusEnum;
import org.pbms.pbmsserver.repository.mapper.UserInfoMapper;
import org.pbms.pbmsserver.util.TokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;
import static org.pbms.pbmsserver.repository.mapper.UserInfoDynamicSqlSupport.userInfo;

public class TokenInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(TokenInterceptor.class);

    @Autowired
    private UserInfoMapper userInfoMapper;

    /**
     * 如果存在PublicInterface直接return true
     * 否者验证token是否正确
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.setContentType("text/html;charset=utf-8");
        if (!(handler instanceof HandlerMethod)) {
            if (request.getRequestURI().equals("/favicon.ico")) {
                return false;
            }
            response.setCharacterEncoding("utf-8");
            response.sendError(404, "url : [" + request.getRequestURI() + "], 类型:   [" + request.getMethod() + "] 接口不存在");
            return false;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        if (method.isAnnotationPresent(PublicInterface.class)) {
            return true;
        }
        TokenUtil.checkToken(request);
        long userId = TokenUtil.getUserId();
        // 如果用户不存在或不是正常状态
        if (this.userInfoMapper.selectOne(c -> c
                .where(userInfo.userId, isEqualTo(userId))
                .and(userInfo.status, isEqualTo(UserStatusEnum.NORMAL.getCode()))
        ).isEmpty()) {
            throw new UnauthorizedException(UnauthorizedException.MessageEnum.UNAUTHORIZED);
        }
        return true;
    }
}