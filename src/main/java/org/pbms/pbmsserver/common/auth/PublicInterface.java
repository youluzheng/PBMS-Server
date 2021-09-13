package org.pbms.pbmsserver.common.auth;

import java.lang.annotation.*;

/**
 * 该注解表示接口不需要token验证
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
@Inherited
public @interface PublicInterface {
    
}
