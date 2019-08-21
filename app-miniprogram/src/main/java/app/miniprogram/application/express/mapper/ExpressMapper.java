package app.miniprogram.application.express.mapper;

import app.miniprogram.application.express.entity.ExpressEntity;
import app.miniprogram.application.express.entity.ExpressEntityKey;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public interface ExpressMapper {
    @Delete({
            "delete from t_express",
            "where post_id = #{postId,jdbcType=VARCHAR}",
            "and u_id = #{uId,jdbcType=INTEGER}",
    })
    int deleteByPrimaryKey(ExpressEntityKey key);

    @Insert({
            "insert into t_express (post_id, u_id, ",
            "type, trajectory, ",
            "complete_flag,last_update_time, insert_time, ",
            "insert_id, update_time, ",
            "update_id)",
            "values (#{postId,jdbcType=VARCHAR}, #{uId,jdbcType=INTEGER}, ",
            "#{type,jdbcType=VARCHAR}, #{trajectory,jdbcType=VARCHAR}, ",
            "#{completeFlag,jdbcType=INTEGER}, last_update_time = #{lastUpdateTime,jdbcType=TIMESTAMP}, NOW(), ",
            "#{insertId,jdbcType=INTEGER}, NOW(), ",
            "#{updateId,jdbcType=INTEGER})"
    })
    int insert(ExpressEntity record);

    @InsertProvider(type = ExpressSqlProvider.class, method = "insertSelective")
    int insertSelective(ExpressEntity record);

    @Select({
            "select",
            "post_id, u_id, type, trajectory, complete_flag, insert_time, insert_id, update_time, ",
            "update_id",
            "from t_express",
            "where post_id = #{postId,jdbcType=VARCHAR}",
            "and u_id = #{uId,jdbcType=INTEGER}",
    })
    ExpressEntity selectByPrimaryKey(ExpressEntityKey key);

    @Select({
            "select",
            "u_id, post_id, type, trajectory, complete_flag ,last_update_time ",
            "from t_express ",
            "where u_id = #{uId,jdbcType=INTEGER} ",
            "order by complete_flag , last_update_time desc",
    })
    List<ExpressEntity> selectHistory(Integer uId);

    @UpdateProvider(type = ExpressSqlProvider.class, method = "updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(ExpressEntity record);

    @Update({
            "update t_express",
            "set trajectory = #{trajectory,jdbcType=VARCHAR},",
            "complete_flag = #{completeFlag,jdbcType=INTEGER},",
            "last_update_time = #{lastUpdateTime,jdbcType=TIMESTAMP},",
            "update_time = NOW(),",
            "update_id = #{updateId,jdbcType=INTEGER}",
            "where post_id = #{postId,jdbcType=VARCHAR}",
            "and u_id = #{uId,jdbcType=INTEGER}",
            "and update_time = #{updateTime,jdbcType=TIMESTAMP}"
    })
    int updateByPrimaryKey(ExpressEntity record);
}