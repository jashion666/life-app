package app.miniprogram.application.express.service.impl;

import app.miniprogram.application.express.service.Express;
import app.miniprogram.application.express.service.ExpressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author :wkh.
 * @date :2019/7/31.
 */
@Service
@Slf4j
public class ExpressServiceImpl implements ExpressService {

    /**
     * 网关查询接口.
     */
    private final Express gateWayExpressImpl;

    /**
     * api查询接口.
     */
    private final Express apiExpressImpl;

    @Override
    public Map<String, Object> getExpressMap(String postId, String type) throws Exception {
        try {
            return gateWayExpressImpl.queryExpressByGateWay(postId, type);
        } catch (Exception e) {
            return apiExpressImpl.queryExpressByGateWay(postId, type);
        }
    }

    @Override
    public void insertExpress(Map<String, Object> map) {

    }

    @Autowired
    public ExpressServiceImpl(@Qualifier("apiExpressImpl") Express apiExpressImpl,
                              @Qualifier("gateWayExpressImpl") Express gateWayExpressImpl) {
        this.apiExpressImpl = apiExpressImpl;
        this.gateWayExpressImpl = gateWayExpressImpl;
    }
}
