package org.pbms.pbmsserver.service.lifecycle.before.decode;

public interface DecodeProcessor {
    String decode(String pattern);

    void registerDecoder(String pattern, Decoder decoder);

    // FIXME 重构
    default String scan(String targetString) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0, j; i < targetString.length(); i++) {
            if (targetString.charAt(i) == '$') {
                // 如果是最后一个字符
                if (i + 1 == targetString.length()) {
                    sb.append('$');
                    return sb.toString();
                }
                if (targetString.charAt(i + 1) == '{') {
                    for (j = i + 1; j < targetString.length(); j++) {
                        if (targetString.charAt(j) == '}') {
                            break;
                        }
                    }
                    // 找到了`}`
                    if (j < targetString.length()) {
                        sb.append(this.decode(targetString.substring(i, j + 1)));
                        i = j;

                        // 如果不是`}`结尾
                    } else {
                        sb.append(targetString.substring(i));
                        return sb.toString();
                    }
                } else {
                    sb.append(targetString.substring(i, i + 2));
                    i++;
                }
            } else {
                sb.append(targetString.charAt(i));
            }
        }
        return sb.toString();
    }

    String process();
}
