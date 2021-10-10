package org.pbms.pbmsserver.common.auth;

import java.lang.annotation.*;

/**
 * 注解表示需要管理员权限
 *
 * @author wangjun
 * @date 2021/9/28 21:09
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
@Inherited
public @interface AdminInterface {
}
