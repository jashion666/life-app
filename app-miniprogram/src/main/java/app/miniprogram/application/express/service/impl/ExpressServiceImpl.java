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
import java.util.Optional;

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

        // TODO 业务逻辑改善 type 用户不指定一直都是null
        ExpressEntityKey entityKey = new ExpressEntityKey(uId, postId, type);
        // 先从db检索，检索未果，从api查询
        ExpressEntity result = getByDb(entityKey);
        if (!Constants.EXPRESS_COMPLETE_FLAG.equals(result.getCompleteFlag())) {
            result.setTrajectoryMap(getTrajectoryMapByApi(entityKey));
            // 物流是否完成
            result.setCompleteFlag("3".equals(result.getTrajectoryMap().get("state"))
                    ? Constants.EXPRESS_COMPLETE_FLAG
                    : Constants.EXPRESS_NOT_COMPLETE_FLAG);
        }
        return result;
    }

    /**
     * 从数据库查出物流整体信息
     */
    private ExpressEntity getByDb(ExpressEntityKey entityKey) {
        return Optional.ofNullable(expressMapper.selectByPrimaryKey(entityKey)).orElse(new ExpressEntity(entityKey));
    }

    /**
     * 从api查询物流信息
     */
    private Map<String, Object> getTrajectoryMapByApi(ExpressEntityKey entityKey) throws Exception {
        try {
            return gateWayExpressImpl.queryExpress(entityKey.getPostId(), entityKey.getType());
        } catch (Exception e) {
            return apiExpressImpl.queryExpress(entityKey.getPostId(), entityKey.getType());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveExpress(ExpressEntity entity) {

        entity.setInsertId(entity.getUId());
        entity.setUpdateId(entity.getUId());
        entity.setType(entity.getTrajectoryMap().get("com").toString());
        entity.setTrajectory(JSONObject.toJSONString(entity.getTrajectoryMap()));

        ExpressEntity dbResult = (expressMapper.selectByPrimaryKey(entity));
        // rebbitMQ?
        if (dbResult == null) {
            expressMapper.insert(entity);
            return;
        }
        // db已经是完成的数据不再处理
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
