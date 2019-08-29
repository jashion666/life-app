package app.miniprogram.application.express.service.impl;

import app.miniprogram.application.constant.Constants;
import app.miniprogram.application.express.entity.ExpressTypeEntity;
import app.miniprogram.application.express.entity.TrajectoryEntity;
import app.miniprogram.application.express.service.Express;
import app.miniprogram.enums.KdNiaoExpressEnums;
import app.miniprogram.utils.JsonUtil;
import com.alibaba.fastjson.JSONObject;
import com.app.utils.encrypt.EncryptUtil;
import com.app.utils.http.HttpClient;
import com.app.utils.http.HttpClientImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.*;

/**
 * 调用api接口方式查询快递
 *
 * @author :wkh.
 * @date :2019/7/31.
 */
@Service("apiExpressImpl")
@Slf4j
public class ApiExpressImpl implements Express {

    @Value("${kdniao.business.id}")
    private String eBusinessID;
    @Value("${kdniao.app.key}")
    private String appKey;
    @Value("${kdniao.track.url}")
    private String apiUrl;
    @Value("${kuaidi100.api.type.url}")
    private String apiExpressTypeUrl;

    @Override
    public TrajectoryEntity queryExpress(String postId, String kd100Type) throws Exception {

        log.info("===> api查询开始");
        String kdNiaoType = getType(postId, kd100Type);
        Map<String, Object> ret = doQuery(postId, kdNiaoType);
        checkResult(ret);
        log.info("<=== api快递查询结束");

        return analysisResult(ret, kd100Type);
    }

    private String getType(String postId, String type) throws IOException {

        if (StringUtils.isEmpty(type)) {
            type = getExpressTypeList(postId).get(0).getComCode();
        }
        // 除了京东顺丰和为空的code，其他都从网关查
        type = Objects.requireNonNull(KdNiaoExpressEnums.getInstance(type)).getCodeValue();
        if (Constants.SPECIAL_EXPRESS_TYPE_SHUNFENG.equals(type) || Constants.SPECIAL_EXPRESS_TYPE_JD.equals(type)) {
            throw new RuntimeException();
        }
        return type;
    }

    private Map<String, Object> doQuery(String postId, String type) throws Exception {
        String requestData = "{'OrderCode':'','ShipperCode':'" + type + "','LogisticCode':'" + postId + "'}";
        Map<String, String> params = new HashMap<>(8);
        params.put("RequestData", EncryptUtil.urlEncoder(requestData, "UTF-8"));
        params.put("EBusinessID", eBusinessID);
        params.put("RequestType", "1002");
        String dataSign = EncryptUtil.encrypt(requestData, appKey, "UTF-8");
        params.put("DataSign", EncryptUtil.urlEncoder(dataSign, "UTF-8"));
        params.put("DataType", "2");

        HttpClient httpClient = new HttpClientImpl();
        String ret = httpClient.post(apiUrl, params);
        log.info("查询结果 " + ret.replaceAll("\n", ""));
        return new JsonUtil().getCustomObjectMapper().readValue(ret, new TypeReference<Map<String, Object>>() {
        });
    }

    private void checkResult(Map<String, Object> ret) throws Exception {
        if (!(boolean) ret.get("Success")) {
            log.info("api查询失败，请换成网关查询");
            throw new Exception();
        }
    }

    @SuppressWarnings("unchecked")
    private TrajectoryEntity analysisResult(Map<String, Object> retMap, String kd100Type) {
        TrajectoryEntity entity = new TrajectoryEntity();
        entity.setCom(kd100Type);
        entity.setState((String) retMap.get("State"));
        // 获取物流
        List<Map<String, Object>> list = (List<Map<String, Object>>) retMap.get("Traces");
        // 转型后的list
        List<TrajectoryEntity.DataBean> data = new ArrayList<>();

        for (int i = list.size() - 1; i >= 0; i--) {
            Map<String, Object> item = list.get(i);
            TrajectoryEntity.DataBean bean = new TrajectoryEntity.DataBean();
            bean.setFtime((String) item.get("AcceptTime"));
            bean.setContext((String) item.get("AcceptStation"));
            bean.setTime(bean.getFtime());
            data.add(bean);
        }
        entity.setData(data);
        return entity;
    }

    @Override
    public List<ExpressTypeEntity> getExpressTypeList(String postId) throws IOException {
        HttpClient httpClient = new HttpClientImpl();
        log.info("查询快递类型");
        Map<String, String> param1 = new HashMap<>(4);
        param1.put("resultv2", "1");
        param1.put("text", postId);
        String ret = httpClient.post(this.apiExpressTypeUrl, param1);

        Map<String, Object> map = JSONObject.parseObject(ret);

        if (!map.containsKey("auto")) {
            throw new IllegalArgumentException("获取快递类型失败 结果：" + map);
        }
        ret = JSONObject.toJSONString(map.get("auto"));
        log.info("快递类型结果：" + ret);
        return new JsonUtil().getCustomObjectMapper().readValue(ret, new TypeReference<List<ExpressTypeEntity>>() {
        });
    }
}
