package app.miniprogram.application.express.service;

import app.miniprogram.application.express.entity.ExpressTypeEntity;
import app.miniprogram.application.express.entity.TrajectoryEntity;

import java.io.IOException;
import java.util.List;

/**
 * @author :wkh.
 * @date :2019/5/25.
 */
public interface Express {

    /**
     * 快递查询
     *
     * @param postId 快递单号
     * @param type   快递公司类型
     * @return 查询结果
     * @throws Exception e
     */
    TrajectoryEntity queryExpress(String postId, String type) throws Exception;

    /**
     * 快递公司查询
     *
     * @param postId 快递单号
     * @return 公司集合
     */
    List<ExpressTypeEntity> getExpressTypeList(String postId) throws IOException;
}
