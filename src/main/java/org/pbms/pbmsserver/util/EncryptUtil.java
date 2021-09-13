package org.pbms.pbmsserver.util;

import org.pbms.pbmsserver.common.exception.ParamNullException;
import org.pbms.pbmsserver.common.exception.ServerException;
import org.springframework.http.HttpStatus;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class EncryptUtil {
    public static String sha512(String target) {
        if (target == null || target.isBlank()) {
            throw new ParamNullException(HttpStatus.INTERNAL_SERVER_ERROR, "目标字符串不能为空");
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
            messageDigest.update(target.getBytes());
            byte byteBuffer[] = messageDigest.digest();

            StringBuffer strHexString = new StringBuffer();
            for (int i = 0; i < byteBuffer.length; i++) {
                String hex = Integer.toHexString(0xff & byteBuffer[i]);
                if (hex.length() == 1) {
                    strHexString.append('0');
                }
                strHexString.append(hex);
            }
            return strHexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new ServerException("加密算法不存在");
        }
    }
}
