package app.miniprogram.application.recognition.service.impl;

import app.miniprogram.application.constant.RedisKeyConstants;
import app.miniprogram.application.recognition.entity.RecognitionEntity;
import app.miniprogram.application.recognition.service.RecognitionService;
import app.miniprogram.utils.JsonUtil;
import com.app.redis.RedisClient;
import com.app.utils.encrypt.EncryptUtil;
import com.app.utils.http.HttpClient;
import com.app.utils.http.HttpClientImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author :wkh.
 * @date :2019/9/3.
 */
@Service
@Slf4j
public class RecognitionServiceImpl implements RecognitionService {

    @Value("${img.recogition.client.id}")
    private String clientId;

    @Value("${img.recogition.client.secret}")
    private String clientSecret;

    @Value("${baidubce.oauth.token.api.url}")
    private String tokenApiUrl;

    @Value("${img.general.basic.api.url}")
    private String imgGeneralBasicApiUrl;

    @Autowired
    private RedisClient redisClient;

    @Override
    public String getAccessToken() throws Exception {
        return getToken(clientId, clientSecret);
    }

    @Override
    public RecognitionEntity extractText(InputStream inputStream, String languageType) throws Exception {
        String imgBase64Text = EncryptUtil.imageToBase64Str(inputStream);
        RecognitionEntity result = doExtract(imgBase64Text, languageType);
        log.debug("识别结果为" + result.toString());
        return result;
    }

    private RecognitionEntity doExtract(String img, String languageType) throws Exception {
        Map<String, String> param = new HashMap<>(4);
        param.put("access_token", getAccessTokenInRedis());
        param.put("image", img);
        if (!StringUtils.isEmpty(languageType)) {
            param.put("language_type", languageType);
        }
        Map<String, String> headers = new HashMap<>(2);
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        HttpClient httpClient = new HttpClientImpl();
        String result = httpClient.post(imgGeneralBasicApiUrl, param, headers);
        return new JsonUtil().getCustomObjectMapper().readValue(result, new TypeReference<RecognitionEntity>() {
        });
    }

    private String getAccessTokenInRedis() throws Exception {
        String ret;
        Object key = redisClient.get(RedisKeyConstants.BAIDU_API_ACCESS_TOKEN);
        if (ObjectUtils.isEmpty(key)) {
            ret = getAccessToken();
            redisClient.set(RedisKeyConstants.BAIDU_API_ACCESS_TOKEN, ret);
        } else {
            ret = (String) key;
        }
        return ret;
    }

    /**
     * 获取API访问token
     * 该token有一定的有效期，需要自行管理，当失效时需重新获取.
     *
     * @param ak - 百度云官网获取的 API Key
     * @param sk - 百度云官网获取的 Securet Key
     * @return assess_token ：
     */
    private String getToken(String ak, String sk) throws Exception {
        log.info("获取百度api Access Token");
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
            log.error("获取token失败！");
            throw new Exception("获取token失败！");
        }
    }
}
