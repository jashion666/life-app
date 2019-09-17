package app.miniprogram.application.translate.service.impl;

import app.miniprogram.api.translate.Translate;
import app.miniprogram.api.translate.TranslateDTO;
import app.miniprogram.application.translate.entity.TranslateEntity;
import app.miniprogram.application.translate.service.TranslateService;
import app.miniprogram.security.exception.AppException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.stream.Collectors;

/**
 * @author :wkh.
 * @date :2019/9/16.
 */
@Service
public class TranslateServiceImpl implements TranslateService {

    @Value("${translate.api.url}")
    private String translateApiUrl;

    @Value("${translate.api.app.id}")
    private String translateApiAppId;

    @Value("${translate.api.security.key}")
    private String translateApiSecurityKey;

    @Override
    public TranslateEntity translate(String query, String from, String to) throws Exception {
        Translate tran = new Translate(translateApiAppId, translateApiSecurityKey, translateApiUrl);
        TranslateDTO result = tran.getTransResult(query, from, to);
        checkTranslateResult(result);

        TranslateEntity translateEntity = new TranslateEntity();
        String translateResult = result.getTrans_result().stream().map(TranslateDTO.TransResultBean::getDst).collect(Collectors.joining("\n"));
        translateEntity.setTranslateResult(translateResult);
        return translateEntity;
    }


    private void checkTranslateResult(TranslateDTO result) {
        if (!StringUtils.isEmpty(result.getError_code())) {
            throw new AppException("翻译失败，请重试");
        }
        if (result.getTrans_result() == null) {
            throw new AppException("翻译失败了--！");
        }
    }
}
