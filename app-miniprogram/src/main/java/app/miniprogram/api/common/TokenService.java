package app.miniprogram.api.common;

import java.util.Map;

import app.miniprogram.utils.JsonUtil;
import com.app.utils.http.HttpClient;
import com.app.utils.http.HttpClientImpl;
import com.fasterxml.jackson.core.type.TypeReference;

/**
 * 获取token类
 *
 * @author :wkh.
 * @date :2019/5/16.
 */
public class TokenService {

    /**
     * 获取权限token
     *
     * @return 返回示例：
     * {
     * "access_token": "24.460da4889caad24cccdb1fea17221975.2592000.1491995545.282335-1234567",
     * "expires_in": 2592000
     * }
     */
    public static String getAuth() {
        // TODO 放到properties里
        // 官网获取的 API Key 更新为你注册的
        String clientId = "xV7sxak2CgDZc1ak2F6qVqeP";
        // 官网获取的 Secret Key 更新为你注册的
        String clientSecret = "vu9P8Az2XR1nxhPU3hOyeA3PBqC84VDO";
        return getAuth(clientId, clientSecret);
    }

    /**
     * 获取API访问token
     * 该token有一定的有效期，需要自行管理，当失效时需重新获取.
     *
     * @param ak - 百度云官网获取的 API Key
     * @param sk - 百度云官网获取的 Securet Key
     * @return assess_token 示例：
     * "24.460da4889caad24cccdb1fea17221975.2592000.1491995545.282335-1234567"
     */
    private static String getAuth(String ak, String sk) {
        String authHost = "https://aip.baidubce.com/oauth/2.0/token?";
        String getAccessTokenUrl = authHost
                + "grant_type=client_credentials"
                + "&client_id=" + ak
                + "&client_secret=" + sk;
        try {
            HttpClient httpClient = new HttpClientImpl();
            String result = httpClient.get(getAccessTokenUrl);
            Map<String, String> resMap = new JsonUtil().getCustomObjectMapper().readValue(result, new TypeReference<Map<String, String>>() {
            });
            return resMap.get("access_token");
        } catch (Exception e) {
            System.err.println("获取token失败！");
            e.printStackTrace(System.err);
        }
        return null;
    }

}