package app.miniprogram.application.express.service;

import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author :wkh.
 * @date :2019/7/31.
 */
@Service
public interface ExpressService {

    /**
     * 获取快递信息
     *
     * @param postId 快递单号
     * @param type   快递类型
     * @return 查询结果
     */
    Map<String, Object> getExpressMap(String postId, String type) throws Exception;

    /**
     * 插入快递查询结果
     *
     * @param map 查询结果
     */
    void insertExpress(Map<String, Object> map);
}
