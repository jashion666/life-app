package app.miniprogram.api.image;

import app.miniprogram.utils.JsonUtil;
import com.app.utils.http.HttpClientImpl;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;


/**
 * 图像识别类
 *
 * @author :wkh.
 * @date :2019/5/16.
 */
public class ImageRecognition {

    private static final String IMAGE_RECOGNITION_API_URL = "https://aip.baidubce.com/rest/2.0/ocr/v1/general_basic?";
    // CHN_ENG、ENG、
    // POR、FRE、
    // GER、ITA、
    // SPA、RUS、
    // JAP、KOR
    private static final String LANGUAGE_TYPE = "JAP";

//    public Map<String, Object> getImgText(String imgUrl) {
//        return doRecognition(imageToBase64Str(imgUrl));
//    }

//    private Map<String, Object> doRecognition(String img) {
//        Map<String, Object> resMap = new HashMap<>(16);
//        try {
//            String url = IMAGE_RECOGNITION_API_URL + "access_token=24.ddb69b92c37ebacb6bf069e07e97e6c0.2592000.1570084895.282335-16263178";
//
//            Map<String, String> param = new HashMap<>(16);
//            param.put("image", img);
////            param.put("language_type", LANGUAGE_TYPE);
//            Map<String, String> headers = new HashMap<>(16);
//            headers.put("Content-Type", "application/x-www-form-urlencoded");
//
//            com.app.utils.http.HttpClient httpClient = new HttpClientImpl();
//            String result = httpClient.post(url, param, headers);
//            System.out.println(result);
//            resMap = new JsonUtil().getCustomObjectMapper().readValue(result, new TypeReference<Map<String, Object>>() {
//            });
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return resMap;
//    }

    /**
     * 图片转base64字符串
//     */
//    private static String imageToBase64Str(String imgFile) {
//        InputStream inputStream;
//        byte[] data = null;
//        try {
//            inputStream = new FileInputStream(imgFile);
//            data = new byte[inputStream.available()];
//            inputStream.read(data);
//            inputStream.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        // 加密
//        return new String(Base64.getEncoder().encode(Objects.requireNonNull(data)));
//    }

}
