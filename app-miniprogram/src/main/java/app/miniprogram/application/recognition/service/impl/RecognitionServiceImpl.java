package app.miniprogram.application.recognition.service.impl;

import app.miniprogram.api.ocr.GeneralBasic;
import app.miniprogram.application.constant.RedisKeyConstants;
import app.miniprogram.application.recognition.entity.RecognitionEntity;
import app.miniprogram.application.recognition.service.RecognitionService;
import app.miniprogram.security.exception.AppException;
import com.app.redis.RedisClient;
import com.app.utils.encrypt.EncryptUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.InputStream;
import java.util.stream.Collectors;

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

    @Value("${img.recogition.universal.recognition.url}")
    private String imgGeneralBasicApiUrl;

    private final RedisClient redisClient;

    @Autowired
    public RecognitionServiceImpl(RedisClient redisClient) {
        this.redisClient = redisClient;
    }

    @Override
    public String getAccessToken() throws Exception {
        log.info("获取百度api Access Token");
        GeneralBasic generalBasic = new GeneralBasic();
        return generalBasic.getToken(clientId, clientSecret, tokenApiUrl);
    }

    @Override
    public RecognitionEntity extractText(InputStream inputStream, String languageType) throws Exception {
        String imgBase64Text = EncryptUtil.imageToBase64Str(inputStream);

        GeneralBasic generalBasic = new GeneralBasic();

        RecognitionEntity result = generalBasic.doExtract(imgBase64Text, languageType, getAccessTokenInRedis(), imgGeneralBasicApiUrl);
        checkResult(result);
        String wordsResult = result.getWords_result()
                .stream()
                .map(RecognitionEntity.WordsResultBean::getWords)
                .collect(Collectors.joining("\n"));

        result.setWordsResult(wordsResult);
        result.setCount(wordsResult.replaceAll("\n", "").length());
        result.setWords_result(null);
        return result;
    }

    private void checkResult(RecognitionEntity result) {
        if (result.getWords_result() == null || result.getWords_result().size() == 0) {
            throw new AppException("该图片未识别到文字");
        }
    }

    private String getAccessTokenInRedis() throws Exception {
        String ret;
        Object key = redisClient.get(RedisKeyConstants.BAIDU_API_ACCESS_TOKEN);
        if (ObjectUtils.isEmpty(key)) {
            ret = getAccessToken();
            // 10天获取一次key
            redisClient.set(RedisKeyConstants.BAIDU_API_ACCESS_TOKEN, ret, 864000L);
        } else {
            ret = (String) key;
        }
        return ret;
    }

}
