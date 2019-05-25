package app.miniprogram.application.express.service.impl;

import app.miniprogram.application.express.service.ExpressService;
import app.miniprogram.utils.PropertiesUtil;
import com.alibaba.fastjson.JSONObject;
import com.app.utils.http.HttpClient;
import com.app.utils.http.HttpClientImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author :wkh.
 * @date :2019/5/25.
 */
@Service
public class ExpressServiceImpl implements ExpressService {

    @Autowired
    private PropertiesUtil properties;

    @Override
    public Map<String, Object> queryExpressByGateWay(String postId, String type) {

        // TODO 需要申请api； 先调用快递web接口，如果失败去调用 申请的快递100api
        return queryByWeb(postId, type);
    }

    private Map<String, Object> queryByWeb(String postId, String type) {

        Map<String, String> headers = new HashMap<>(16);

        // TODO cookie 必传 如果cookie失效怎么办？
        headers.put("Cookie", "csrftoken=S204_1FmZVfgogelz5gMPavVQ-htZrIvyV3Za500UG0; WWWID=WWW17AE316632589F8829B366522BB0A8A2; Hm_lvt_22ea01af58ba2be0fec7c11b25e88e6c=1558598018,1558698527; Hm_lpvt_22ea01af58ba2be0fec7c11b25e88e6c=1558698527");
        headers.put("Referer", properties.getValue("kuaidi100.referer"));
        headers.put("User-Agent", properties.getValue("common.user.agent"));

        Map<String, String> param = new HashMap<>(16);
        param.put("postid", postId);
        param.put("type", type);
        // 暂定为空
        param.put("phone", "");
        param.put("temp", String.valueOf(Math.random()));


        HttpClient httpClient = new HttpClientImpl();
        String ret = httpClient.get(properties.getValue("kuaidi100.api.url"), param, headers);
        Map<String, Object> result = JSONObject.parseObject(ret);
        return result;
    }
}
