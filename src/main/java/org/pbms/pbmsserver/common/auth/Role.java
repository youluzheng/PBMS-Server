package org.pbms.pbmsserver.common.auth;

import java.lang.annotation.*;

/**
 * 该注解表示接口需要token验证
 *
 * @author zyl
 */
@Retention(RetentionPolicy.RUNTIME)
// 表示该注解可在类或方法上使用
@Target({ElementType.METHOD, ElementType.TYPE})
@Documented
@Inherited
public @interface Role {
    // 默认匿名用户，表示不需要登录
    RoleEnum[] role() default RoleEnum.ANONYMITY;
}
