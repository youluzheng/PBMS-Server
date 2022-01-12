package org.pbms.pbmsserver.util;

import cn.hutool.core.date.DateUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.pbms.pbmsserver.common.auth.TokenBean;
import org.pbms.pbmsserver.common.auth.TokenHandle;
import org.pbms.pbmsserver.common.exception.ServerException;
import org.pbms.pbmsserver.repository.enumeration.user.UserRoleEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TokenUtilTest {

    private static final Logger log = LoggerFactory.getLogger(TokenUtilTest.class);

    private TokenBean generateTokenBean() {
        return new TokenBean(1L, "张三", UserRoleEnum.ADMIN.transform());
    }

    private static Stream<Arguments> generateInvalidData_generateToken() {
        return Stream.of(
                Arguments.of(null, new HashMap<>(), 10L, TimeUnit.DAYS, ServerException.class),
                Arguments.of("", new HashMap<>(), 10L, TimeUnit.DAYS, ServerException.class),
                Arguments.of("123456", null, 10L, TimeUnit.DAYS, NullPointerException.class),
                Arguments.of("123456", new HashMap<>(), 0L, TimeUnit.DAYS, ServerException.class),
                Arguments.of("123456", new HashMap<>(), -10L, TimeUnit.DAYS, ServerException.class),
                Arguments.of("123456", new HashMap<>(), 10L, null, NullPointerException.class)
        );
    }

    @ParameterizedTest
    @MethodSource("generateInvalidData_generateToken")
    void generateToken_invalidParam(String secret, Map<String, Object> data, long expiration, TimeUnit timeUnit, Class<? extends Exception> exception) {
        assertThrows(exception, () -> TokenHandle.generateToken(secret, data, expiration, timeUnit));
    }

    @Test
    void generateToken_expiration() throws InterruptedException {
        String secret = "123456";
        String token = TokenHandle.generateToken(secret, new HashMap<>(), 3, TimeUnit.SECONDS);
        Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
        Thread.sleep(3500);
        JwtParser parser = Jwts.parser().setSigningKey(secret);
        assertThrows(ExpiredJwtException.class, () -> parser.parseClaimsJws(token));
    }

    @Test
    void generatorSecret() {
        Random random = new SecureRandom();
        IntStream.range(0, 10000).forEach(x -> {
                    String secret = String.format("%09d", random.nextInt(1000000000));
                    assertEquals(9, secret.length());
                }
        );
    }

    @Test
    void testSecret1() {
        System.out.println(DateUtil.date());
    }
}