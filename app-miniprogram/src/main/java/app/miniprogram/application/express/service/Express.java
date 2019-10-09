package app.miniprogram.application.express.service;

import app.miniprogram.application.express.entity.TrajectoryEntity;


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
     * @param phone  电话号码
     * @return 查询结果
     * @throws Exception e
     */
    TrajectoryEntity queryExpress(String postId, String type, String phone) throws Exception;

}
