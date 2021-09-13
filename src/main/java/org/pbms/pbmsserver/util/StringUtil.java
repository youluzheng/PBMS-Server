package org.pbms.pbmsserver.util;

import java.util.Objects;

/**
 * 字符串工具类
 *
 * @author zyl
 * @date 2021/07/11 20:33:29
 */
public class StringUtil {
    /**
     * 判断字符串是否为表示存储大小格式, 支持<code>KB</code>, <code>MB</code>, <code>GB</code>判断
     * <p>
     * 正确的大小格式如<code>10KB</code>, <code>20MB</code>
     * <p>
     * 注意，严格区分大小写，<code>10kb -> false</code>, <code>10KB -> true</code>
     *
     * @param testString 待测试字符串, 不能为空
     * @return true 如果符合要求，否则false
     * @throws NullPointerException 如果<code>testString == null</code>
     */
    public static boolean isValidSizeFormatString(String testString) {
        Objects.requireNonNull(testString);
        return testString.matches("^[^0]\\d*((KB)|(MB)|(GB))$");
    }
}
