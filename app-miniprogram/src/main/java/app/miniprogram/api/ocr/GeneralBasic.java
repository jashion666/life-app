package app.miniprogram.api.ocr;

import app.miniprogram.application.recognition.entity.RecognitionEntity;
import app.miniprogram.utils.JsonUtil;
import com.app.utils.http.HttpClient;
import com.app.utils.http.HttpClientImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author :wkh.
 * @date :2019/9/16.
 */
public class GeneralBasic {

    /**
     * 获取API访问token
     * 该token有一定的有效期，需要自行管理，当失效时需重新获取.
     *
     * @param ak - 百度云官网获取的 API Key
     * @param sk - 百度云官网获取的 Securet Key
     * @return assess_token ：
     */
    public String getToken(String ak, String sk, String tokenApiUrl) throws Exception {
        Map<String, String> param = new HashMap<>(4);
        param.put("grant_type", "client_credentials");
        param.put("client_id", ak);
        param.put("client_secret", sk);
        try {
            HttpClient httpClient = new HttpClientImpl();
            String result = httpClient.get(tokenApiUrl, param);
            Map<String, String> resMap = new JsonUtil().getCustomObjectMapper().readValue(result, new TypeReference<Map<String, String>>() {
            });
            return resMap.get("access_token");
        } catch (Exception e) {
            throw new Exception("获取token失败！");
        }
    }

    /**
     * 提取文字
     */
    public RecognitionEntity doExtract(String img, String languageType, String accessToken, String basicApiUrl) throws Exception {
        Map<String, String> param = new HashMap<>(4);
        param.put("access_token", accessToken);
        param.put("image", img);
        if (!StringUtils.isEmpty(languageType)) {
            param.put("language_type", languageType);
        }
        Map<String, String> headers = new HashMap<>(2);
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        HttpClient httpClient = new HttpClientImpl();
        String result = httpClient.post(basicApiUrl, param, headers);
        return new JsonUtil().getCustomObjectMapper().readValue(result, new TypeReference<RecognitionEntity>() {
        });
    }
}
