package app.miniprogram.application.translate.service;

import app.miniprogram.application.translate.entity.TranslateEntity;

/**
 * @author :wkh.
 * @date :2019/9/16.
 */
public interface TranslateService {
    /**
     * 翻译
     *
     * @param query 待翻译文本
     * @param from  form
     * @param to    to
     * @return 翻译结果
     * @throws Exception e
     */
    TranslateEntity translate(String query, String from, String to) throws Exception;
}
