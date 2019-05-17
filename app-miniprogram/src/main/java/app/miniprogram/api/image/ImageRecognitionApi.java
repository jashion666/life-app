package app.miniprogram.api.image;

import app.miniprogram.utils.JsonUtils;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.fluent.Request;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;


/**
 * @author :wkh.
 * @date :2019/5/16.
 */
public class ImageRecognitionApi {

    private static final String IMAGE_RECOGNITION_API_URL = "https://aip.baidubce.com/rest/2.0/ocr/v1/general_basic?";
    private static final String LANGUAGE_TYPE = "CHN_ENG";

    public Map<String, Object> getImgText(String imgUrl) {

        return getImgValue(imageToBase64Str(imgUrl));
    }

    private Map<String, Object> getImgValue(String img) {
        Map<String, Object> resMap = new HashMap<>();
        try {
            String url = IMAGE_RECOGNITION_API_URL + "access_token=24.5851b753d2060a85ab70962ae4e71cfb.2592000.1560649112.282335-16263178";
            List<NameValuePair> paramList = new ArrayList<>();
            paramList.add(new BasicNameValuePair("image", img));
            paramList.add(new BasicNameValuePair("language_type", LANGUAGE_TYPE));
            HttpResponse response = Request
                    .Post(url)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .bodyForm(paramList)
                    .execute()
                    .returnResponse();

            String result = EntityUtils.toString(response.getEntity());
            resMap = new JsonUtils().getCustomObjectMapper().readValue(result, new TypeReference<Map<String, Object>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resMap;
    }

    /**
     * 图片转base64字符串
     */
    private static String imageToBase64Str(String imgFile) {
        InputStream inputStream;
        byte[] data = null;
        try {
            inputStream = new FileInputStream(imgFile);
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
