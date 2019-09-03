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
import org.springframework.beans.factory.annotation.Autowired;
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

    private final ExpressUtil expressUtil;

    @Autowired
    public ApiExpressImpl(ExpressUtil expressUtil) {
        this.expressUtil = expressUtil;
    }

    @Override
    public TrajectoryEntity queryExpress(String postId, String kd100Type) throws Exception {

        log.info("===> api查询");
        // 查询快递类型
        kd100Type = searchType(postId, kd100Type);
        // 将快递类型转换为快递鸟可用的类型 （因为快递100的类型和快递鸟的不一样）
        String kdNiaoType = getType(kd100Type);
        // 请求api，返回结果
        Map<String, Object> ret = doQuery(postId, kdNiaoType);
        // 验证返回结果
        checkResult(ret);
        // 将返回结果封装到实体类
        TrajectoryEntity result = analysisResult(ret, kd100Type);
        log.info("<=== api快递查询结束");
        return result;
    }

    private String getType(String type) {
        // 除了京东顺丰和为空的code，其他都从网关查
        type = Objects.requireNonNull(KdNiaoExpressEnums.getInstance(type)).getCodeValue();
        if (Constants.SPECIAL_EXPRESS_TYPE_SHUNFENG.equals(type) || Constants.SPECIAL_EXPRESS_TYPE_JD.equals(type)) {
            throw new IllegalArgumentException();
        }
        return type;
    }

    private String searchType(String postId, String type) throws IOException {
        return StringUtils.isEmpty(type) ? expressUtil.getTypeListByKd100(postId).get(0).getComCode() : type;
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
        if ("暂无轨迹信息".equals(ret.get("Reason"))) {
            log.info("api未查到，转为网关查询");
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

}
