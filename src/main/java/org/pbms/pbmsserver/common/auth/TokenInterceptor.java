package org.pbms.pbmsserver.common.auth;

import org.pbms.pbmsserver.common.exception.BusinessException;
import org.pbms.pbmsserver.common.exception.BusinessStatus;
import org.pbms.pbmsserver.common.exception.UnauthorizedException;
import org.pbms.pbmsserver.dao.UserInfoDao;
import org.pbms.pbmsserver.repository.Tables;
import org.pbms.pbmsserver.repository.enumeration.user.UserRoleEnum;
import org.pbms.pbmsserver.repository.enumeration.user.UserStatusEnum;
import org.pbms.pbmsserver.repository.model.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;

import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

/**
 * @author zyl
 */
public class TokenInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(TokenInterceptor.class);

    @Autowired
    private UserInfoDao userInfoDao;

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
        Class<?> clazz = method.getDeclaringClass();

        log.debug("【request-start】:[url:{}, type:{}, method:{}]", request.getRequestURI(),
                request.getMethod(), method.getName());

        RoleEnum[] roles;
        // 如果方法上有注解，不考虑类上
        if (method.isAnnotationPresent(Role.class)) {
            roles = method.getAnnotation(Role.class).role();
        } else {
            // 如果方法上没有注解，类上有注解
            if (clazz.isAnnotationPresent(Role.class)) {
                roles = clazz.getAnnotation(Role.class).role();
            } else {
                // 方法和类上都没有注解，跳过token校验
                return true;
            }
        }

        // 如果注解包含匿名角色，跳过token校验
        if (Arrays.stream(roles).anyMatch(x -> x == RoleEnum.ANONYMITY)) {
            return true;
        }
        // 需要验证token
        TokenBean tokenBean = TokenHandler.checkToken();
        boolean isAllMatching = false;
        boolean isRoleMatching = false;
        for (RoleEnum role : roles) {
            // 不退出
            if (role == tokenBean.getUserRole()) {
                isRoleMatching = true;
            }
            // 退出
            if (role == RoleEnum.ALL_LOGGED_IN) {
                isAllMatching = true;
                // 置false, 避免role检查
                isRoleMatching = false;
                break;
            }
        }
        // 匹配任意一个
        if (isAllMatching || isRoleMatching) {
            long userId = tokenBean.getUserId();
            UserInfo user = this.userInfoDao.selectOne(c -> c
                    .where(Tables.userInfoTable.userId, isEqualTo(userId))
                    .and(Tables.userInfoTable.status, isEqualTo(UserStatusEnum.NORMAL.getCode()))
            ).orElseThrow(() -> new UnauthorizedException(UnauthorizedException.MessageEnum.UNAUTHORIZED));
            if (isRoleMatching) {
                RoleEnum userRole = UserRoleEnum.transform(user.getRole());
                // 角色不匹配，token中匹配，但是与数据库不一致
                if (userRole != tokenBean.getUserRole()) {
                    throw new BusinessException(BusinessStatus.PERMISSION_DENIED);
                }
            }
            TokenHandler.setTokenBean(tokenBean);
            return true;
        }
        // 已登录, 但是角色不匹配
        // 区别于上面的角色不匹配，上面主要是原原因是匹配但是被登录期间修改，token匹配，但是与数据库不匹配
        // 这个角色不匹配主要指调用了没有权限的接口，主要出现于前端接口调用错误，token不匹配
        log.error("接口[{}]调用错误，接口指定角色为:{}, 用户角色为:{}", request.getRequestURI(), roles, tokenBean.getUserRole());
        throw new BusinessException(BusinessStatus.PERMISSION_DENIED);
    }
}