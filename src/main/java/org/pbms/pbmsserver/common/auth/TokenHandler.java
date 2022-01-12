package org.pbms.pbmsserver.common.auth;


import cn.hutool.json.JSONUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.pbms.pbmsserver.common.exception.ServerException;
import org.pbms.pbmsserver.common.exception.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * token处理
 *
 * @author zyl
 */
public final class TokenHandler {
    private static final Logger log = LoggerFactory.getLogger(TokenHandler.class);

    private static final Random random = new SecureRandom();
    private static final String SECRET = String.format("%09d", random.nextInt(1000000000));

    private static final long EXPIRATION = 1L;
    private static final TimeUnit TIME_UNIT = TimeUnit.DAYS;

    private static final String TOKEN_HEAD = "token";
    private static final String KEY = "user_info";

    /**
     * 根据给出的AppUser信息生成虚构的token
     *
     * @param tokenBean token中保存的信息
     * @return 用户信息生成的token
     */
    public static String generateToken(TokenBean tokenBean) {
        Objects.requireNonNull(tokenBean);
        Map<String, Object> data = new HashMap<>();
        data.put(TokenHandler.KEY, JSONUtil.toJsonStr(tokenBean));
        return TokenHandler.generateToken(TokenHandler.SECRET, data, TokenHandler.EXPIRATION, TokenHandler.TIME_UNIT);
    }

    public static String generateToken(String secret, Map<String, Object> data, long expiration, TimeUnit timeUnit) {
        if (secret == null || secret.isBlank()) {
            throw new ServerException("secret不能为空");
        }
        Objects.requireNonNull(data);
        Objects.requireNonNull(timeUnit);
        if (expiration <= 0) {
            log.debug("expiration:{}", expiration);
            throw new ServerException("expiration必须大于0");
        }
        return Jwts.builder()
                .setClaims(data)
                .setExpiration(new Date(System.currentTimeMillis() + timeUnit.toMillis(expiration)))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 检查Token是否正确
     */
    public static TokenBean checkToken() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String token = request.getHeader(TokenHandler.TOKEN_HEAD);
        Claims data;
        try {
            data = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException(UnauthorizedException.MessageEnum.AUTHORIZED_EXPIRED);
        } catch (Exception e) {
            log.warn("JWT格式验证失败:{}", token);
            throw new UnauthorizedException(UnauthorizedException.MessageEnum.UNAUTHORIZED);
        }
        if (data == null) {
            log.error("token解析异常，token:{}", token);
            throw new UnauthorizedException(UnauthorizedException.MessageEnum.UNAUTHORIZED);
        }
        return JSONUtil.toBean(data.get(TokenHandler.KEY, String.class), TokenBean.class);
    }

    public static void setTokenBean(TokenBean tokenBean) {
        Objects.requireNonNull(tokenBean);
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        request.setAttribute(TokenHandler.KEY, tokenBean);
    }

    /**
     * 获取请求中的tokenBean
     *
     * @return tokenBean
     */
    public static TokenBean getTokenBean() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return (TokenBean) request.getAttribute(TokenHandler.KEY);
    }

    /**
     * 获取当前用户的用户id
     *
     * @return 用户id
     */
    public static long getUserId() {
        return TokenHandler.getTokenBean().getUserId();
    }
}
