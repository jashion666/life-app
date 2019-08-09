package app.miniprogram.application.express.service.impl;

import app.miniprogram.application.constant.Constants;
import app.miniprogram.application.express.entity.ExpressEntity;
import app.miniprogram.application.express.entity.ExpressEntityKey;
import app.miniprogram.application.express.mapper.ExpressMapper;
import app.miniprogram.application.express.service.Express;
import app.miniprogram.application.express.service.ExpressService;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * mapper 接口
     */
    private final ExpressMapper expressMapper;

    @Override
    public ExpressEntity getExpressInfo(Integer uId, String postId, String type) throws Exception {

        ExpressEntityKey entityKey = new ExpressEntityKey(uId, postId);
        // 先从db检索，检索未果，从api查询
        ExpressEntity result = getByDb(entityKey);
        if (!Constants.EXPRESS_COMPLETE_FLAG.equals(result.getCompleteFlag())) {
            result.setTrajectoryMap(this.getTrajectoryMapByApi(postId, type));
            //TODO 物流完成flag 不对
            result.setCompleteFlag("3".equals(result.getTrajectoryMap().get("state"))
                    ? Constants.EXPRESS_COMPLETE_FLAG
                    : Constants.EXPRESS_NOT_COMPLETE_FLAG);
        }

        // 将map物流结果转成json字符串保存到db
        result.setTrajectory(String.valueOf(result.getTrajectoryMap().get("org")));
        // 获取快递类型
        result.setType(result.getTrajectoryMap().get("com").toString());

        return result;
    }

    /**
     * 从数据库查出物流整体信息
     */
    private ExpressEntity getByDb(ExpressEntityKey entityKey) {
        ExpressEntity expressEntity = expressMapper.selectByPrimaryKey(entityKey);
        if (expressEntity == null) {
            return new ExpressEntity(entityKey);
        }
        expressEntity.setTrajectoryMap(JSONObject.parseObject(expressEntity.getTrajectory()));
        return expressEntity;
    }

    /**
     * 从api查询物流信息
     */
    private Map<String, Object> getTrajectoryMapByApi(String postId, String type) throws Exception {
        try {
            return gateWayExpressImpl.queryExpress(postId, type);
        } catch (Exception e) {
            return apiExpressImpl.queryExpress(postId, type);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveExpress(ExpressEntity entity) {

        entity.setInsertId(entity.getUId());
        entity.setUpdateId(entity.getUId());

        ExpressEntity dbResult = expressMapper.selectByPrimaryKey(entity);
        // rebbitMQ?
        if (dbResult == null) {
            expressMapper.insert(entity);
            return;
        }
        // DB中已经是完成的数据不再处理
        if (Constants.EXPRESS_COMPLETE_FLAG.equals(dbResult.getCompleteFlag())) {
            return;
        }

        if (expressMapper.updateByPrimaryKey(entity) == 0) {
            throw new RuntimeException();
        }
    }

    @Autowired
    public ExpressServiceImpl(@Qualifier("apiExpressImpl") Express apiExpressImpl,
                              @Qualifier("gateWayExpressImpl") Express gateWayExpressImpl,
                              ExpressMapper expressMapper) {
        this.apiExpressImpl = apiExpressImpl;
        this.gateWayExpressImpl = gateWayExpressImpl;
        this.expressMapper = expressMapper;
    }
}
