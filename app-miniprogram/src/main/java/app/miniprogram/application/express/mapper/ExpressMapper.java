package app.miniprogram.application.express.mapper;

import app.miniprogram.application.express.entity.ExpressEntity;
import app.miniprogram.application.express.entity.ExpressEntityKey;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.Date;

public interface ExpressMapper {
    @Delete({
            "delete from t_express",
            "where post_id = #{postId,jdbcType=VARCHAR}",
            "and u_id = #{uId,jdbcType=INTEGER}",
            "and type = #{type,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(ExpressEntityKey key);

    @Insert({
            "insert into t_express (post_id, u_id, ",
            "type, trajectory, ",
            "complete_flag, insert_time, ",
            "insert_id, update_time, ",
            "update_id)",
            "values (#{postId,jdbcType=VARCHAR}, #{uId,jdbcType=INTEGER}, ",
            "#{type,jdbcType=VARCHAR}, #{trajectory,jdbcType=VARCHAR}, ",
            "#{completeFlag,jdbcType=INTEGER}, NOW(), ",
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
            "and type = #{type,jdbcType=VARCHAR}",
    })
    ExpressEntity selectByPrimaryKey(ExpressEntityKey key);

    @UpdateProvider(type = ExpressSqlProvider.class, method = "updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(ExpressEntity record);

    @Update({
            "update t_express",
            "set trajectory = #{trajectory,jdbcType=VARCHAR},",
            "complete_flag = #{completeFlag,jdbcType=INTEGER},",
            "update_time = NOW(),",
            "update_id = #{updateId,jdbcType=INTEGER}",
            "where post_id = #{postId,jdbcType=VARCHAR}",
            "and u_id = #{uId,jdbcType=INTEGER}",
            "and type = #{type,jdbcType=VARCHAR}",
            "and update_time = #{updateTime,jdbcType=TIMESTAMP}"
    })
    int updateByPrimaryKey(ExpressEntity record);
}