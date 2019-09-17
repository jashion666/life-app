package com.app.utils.encrypt;


import org.springframework.util.DigestUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.Objects;

/**
 * @author :wkh.
 * @date :2019/8/29.
 */
public class EncryptUtil {

    public static String base64(String str, String charset) throws UnsupportedEncodingException {
        return new String(Base64.getEncoder().encode(str.getBytes(charset)));
    }

    public static String urlEncoder(String str, String charset) throws UnsupportedEncodingException {
        return URLEncoder.encode(str, charset);
    }

    public static String md5(String str) {
        return DigestUtils.md5DigestAsHex(str.getBytes());
    }

    public static String encrypt(String content, String keyValue, String charset) throws Exception {
        if (keyValue != null) {
            return base64(md5(content + keyValue), charset);
        }
        return base64(md5(content), charset);
    }

    public static String imageToBase64Str(InputStream inputStream) {
        byte[] data = null;
        try {
            data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 加密
        return new String(Base64.getEncoder().encode(Objects.requireNonNull(data)));
    }
}
