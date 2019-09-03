package app.miniprogram.application.recognition.service;

import app.miniprogram.application.recognition.entity.RecognitionEntity;

import java.io.InputStream;

/**
 * @author :wkh.
 * @date :2019/9/3.
 */
public interface RecognitionService {

    /**
     * 百度AIP开放平台 accesss_token获取(token有效期为30天)
     *
     * @return token
     * @throws Exception e
     */
    String getAccessToken() throws Exception;

    /**
     * 提取图像文字
     *
     * @param inputStream  图像流
     * @param languageType 语种（默认中英文混合）
     * @throws Exception e
     * @return 图片实体类
     */
    RecognitionEntity extractText(InputStream inputStream, String languageType) throws Exception;
}
