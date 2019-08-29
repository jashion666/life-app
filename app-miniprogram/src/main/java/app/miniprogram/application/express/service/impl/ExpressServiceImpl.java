package app.miniprogram.application.express.service.impl;

import app.miniprogram.application.constant.Constants;
import app.miniprogram.application.constant.DateConstants;
import app.miniprogram.application.express.entity.ExpressEntity;
import app.miniprogram.application.express.entity.ExpressEntityKey;
import app.miniprogram.application.express.entity.TrajectoryEntity;
import app.miniprogram.application.express.mapper.ExpressMapper;
import app.miniprogram.application.express.service.Express;
import app.miniprogram.application.express.service.ExpressService;
import app.miniprogram.enums.ExpressEnums;
import app.miniprogram.utils.CommonUtil;
import app.miniprogram.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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

    @Value("${kuaidi100.img.cdn}")
    private String cdn;

    @Override
    public List<ExpressEntity> getHistoryList(Integer uId) {

        // TODO 分页
        List<ExpressEntity> resultList = expressMapper.selectHistory(uId);

        for (ExpressEntity event : resultList) {
            // 设置快递类型文字
            event.setTypeText(Objects.requireNonNull(ExpressEnums.getInstance(event.getType())).getCodeValue());

            TrajectoryEntity trajectoryInfo = new TrajectoryEntity();
            trajectoryInfo.setData(CommonUtil.parseExpressData(event.getTrajectory()));
            event.setTrajectoryInfo(trajectoryInfo);
            // 设置最后更新年月
            event.setYmd(DateUtil.format(event.getLastUpdateTime(), DateConstants.FORMAT_YMD));
            // 设置最后更新的文字
            String lastText = trajectoryInfo.getData().get(0).getContext();
            event.setValue(lastText.length() > Constants.EXPRESS_MAX_NUMBER
                    ? lastText.substring(0, Constants.EXPRESS_MAX_NUMBER) + Constants.ELLIPSIS
                    : lastText);
            event.setImgUrl(cdn + event.getType() + Constants.PNG);
        }
        return resultList;
    }

    @Override
    public ExpressEntity getExpressInfo(Integer uId, String postId, String type) throws Exception {

        // 先从db检索
        ExpressEntity result = getByDb(uId, postId, type);
        // 初次查询
        if (result == null) {
            result = getByApi(uId, postId, type);
            result.setOperateFlag(Constants.INSERT);
            return result;
        }

        // 查询未完成
        if (!Constants.EXPRESS_COMPLETE_FLAG.equals(result.getCompleteFlag())) {
            LocalDateTime updateTime = result.getUpdateTime();
            // 更新操作 从api获取
            result = getByApi(uId, postId, type);
            result.setOperateFlag(Constants.UPDATE);
            result.setUpdateTime(updateTime);
            return result;
        }

        // 已完成
        TrajectoryEntity entity = new TrajectoryEntity();
        // 将db中json物流轨迹转换成实体类返回前台
        entity.setData(CommonUtil.parseExpressData(result.getTrajectory()));
        result.setTrajectoryInfo(entity);

        return result;
    }

    /**
     * 从数据库查出物流整体信息
     */
    private ExpressEntity getByDb(Integer uId, String postId, String type) {
        return StringUtils.isEmpty(type)
                ? expressMapper.selectByPrimaryKey(new ExpressEntityKey(uId, postId))
                : expressMapper.selectByKeyAndType(uId, postId, type);
    }

    /**
     * 从api查询物流信息
     */
    private ExpressEntity getByApi(Integer uId, String postId, String type) throws Exception {
        TrajectoryEntity trajectoryEntity;
        // 先用申请的接口去查询，查询失败再用网关去查
        try {
            trajectoryEntity = apiExpressImpl.queryExpress(postId, type);
        } catch (Exception e) {
            trajectoryEntity = gateWayExpressImpl.queryExpress(postId, type);
        }
        ExpressEntity result = new ExpressEntity(uId, postId);
        result.setTrajectoryInfo(trajectoryEntity);
        // 物流状态
        result.setCompleteFlag(trajectoryEntity.getState());
        // 获取快递类型
        result.setType(trajectoryEntity.getCom());
        // 设置快递类型文字
        result.setTypeText(Objects.requireNonNull(ExpressEnums.getInstance(result.getType())).getCodeValue());
        // 设置快递图片地址
        result.setImgUrl(cdn + result.getType() + Constants.PNG);
        return result;
    }

    @Override
    public void saveExpress(ExpressEntity entity) {

        if (Constants.INSERT.equals(entity.getOperateFlag())) {
            insert(entity);
            return;
        }
        if (Constants.UPDATE.equals(entity.getOperateFlag())) {
            update(entity);
        }

    }

    @Transactional(rollbackFor = Exception.class)
    public void insert(ExpressEntity entity) {
        setDataBeforeInsertOrUpdate(entity);
        expressMapper.insert(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(ExpressEntity entity) {
        setDataBeforeInsertOrUpdate(entity);
        if (expressMapper.updateByPrimaryKey(entity) == 0) {
            throw new RuntimeException();
        }
    }

    /**
     * 更新或者插入之前设置部分数据
     */
    private void setDataBeforeInsertOrUpdate(ExpressEntity entity) {
        entity.setInsertId(entity.getUId());
        entity.setUpdateId(entity.getUId());
        // 将实体类物流轨迹转换成json字符串保存在db
        entity.setTrajectory(CommonUtil.formatExpressData(entity.getTrajectoryInfo()));
        LocalDateTime lastUpdateTime = DateUtil.parseDateTime(
                entity.getTrajectoryInfo().getData().get(0).getFtime(), DateConstants.FORMAT_YMDHMS);
        entity.setLastUpdateTime(lastUpdateTime);
        if (Constants.EXPRESS_SENDING_FLAG.equals(entity.getCompleteFlag())) {
            entity.setCompleteFlag(Constants.EXPRESS_NOT_COMPLETE_FLAG);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(Integer uId, String postId) {
        if (expressMapper.deleteByPrimaryKey(new ExpressEntityKey(uId, postId)) == 0) {
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
