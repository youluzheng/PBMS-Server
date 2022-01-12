package org.pbms.pbmsserver.util;

import org.pbms.pbmsserver.common.exception.ServerException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class EncryptUtil {
    private EncryptUtil() {
    }

    public static String sha512(String target) {
        if (target == null || target.isBlank()) {
            throw new ServerException("目标字符串不能为空");
        }
        return EncryptUtil.sha512(target.getBytes());
    }

    public static String sha512(MultipartFile target) {
        if (target == null || target.isEmpty()) {
            throw new ServerException("目标文件不能为空");
        }
        try {
            byte[] bytes = target.getBytes();
            return EncryptUtil.sha512(bytes);
        } catch (IOException e) {
            throw new ServerException("文件读取失败");
        }
    }

    private static String sha512(byte[] bytes) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
            messageDigest.update(bytes);
            byte[] byteBuffer = messageDigest.digest();

            StringBuilder strHexString = new StringBuilder();
            for (byte b : byteBuffer) {
                String hex = Integer.toHexString(0xff & b);
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
