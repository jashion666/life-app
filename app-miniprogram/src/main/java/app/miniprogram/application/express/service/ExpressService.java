package app.miniprogram.application.express.service;

import app.miniprogram.application.express.entity.ExpressEntity;

/**
 * @author :wkh.
 * @date :2019/7/31.
 */
public interface ExpressService {

    /**
     * 获取快递信息
     *
     * @param uId    用户id
     * @param postId 快递单号
     * @param type   快递类型
     * @return 查询结果
     * @throws Exception e
     */
    ExpressEntity getExpressInfo(Integer uId, String postId, String type) throws Exception;

    /**
     * 插入快递查询结果
     *
     * @param entity 快递实体类
     */
    void saveExpress(ExpressEntity entity);
}
