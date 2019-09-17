package app.miniprogram.api.translate;

import app.miniprogram.application.recognition.entity.RecognitionEntity;
import app.miniprogram.security.exception.AppException;
import app.miniprogram.utils.JsonUtil;
import com.app.utils.encrypt.EncryptUtil;
import com.app.utils.http.HttpClient;
import com.app.utils.http.HttpClientImpl;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author :wkh.
 * @date :2019/9/16.
 */
public class Translate {


    private String transApiHost;
    private String appId;
    private String securityKey;

    public Translate(String appId, String securityKey, String transApiHost) {
        this.appId = appId;
        this.securityKey = securityKey;
        this.transApiHost = transApiHost;
    }

    public TranslateDTO getTransResult(String query, String from, String to) throws IOException {
        String result = post(buildParams(query, from, to));
        return new JsonUtil().getCustomObjectMapper().readValue(result, new TypeReference<TranslateDTO>() {
        });
    }

    private Map<String, String> buildParams(String query, String from, String to) throws UnsupportedEncodingException {
        Map<String, String> params = new HashMap<>(8);
        params.put("q", EncryptUtil.urlEncoder(query, "UTF-8"));
        params.put("from", from);
        params.put("to", to);

        params.put("appid", appId);

        // 随机数
        String salt = String.valueOf(System.currentTimeMillis());
        params.put("salt", salt);

        // 签名
        String src = appId + query + salt + securityKey;
        params.put("sign", EncryptUtil.md5(src));

        return params;
    }

    private String post(Map<String, String> params) {
        try {
            HttpClient httpClient = new HttpClientImpl();
            return httpClient.get(transApiHost, params);
        } catch (Exception e) {
            throw new AppException("翻译失败");
        }
    }
}
