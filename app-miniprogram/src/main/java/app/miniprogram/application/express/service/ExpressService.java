package app.miniprogram.application.express.service;

import java.util.Map;

/**
 * @author :wkh.
 * @date :2019/5/25.
 */
public interface ExpressService {

    Map<String, Object> queryExpressByGateWay(String postId);
}
