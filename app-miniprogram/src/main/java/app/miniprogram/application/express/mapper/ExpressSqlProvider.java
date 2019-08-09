package app.miniprogram.application.express.mapper;

import app.miniprogram.application.express.entity.ExpressEntity;
import org.apache.ibatis.jdbc.SQL;

public class ExpressSqlProvider {

    public String insertSelective(ExpressEntity record) {
        SQL sql = new SQL();
        sql.INSERT_INTO("t_express");
        
        if (record.getPostId() != null) {
            sql.VALUES("post_id", "#{postId,jdbcType=VARCHAR}");
        }
        
        if (record.getUId() != null) {
            sql.VALUES("u_id", "#{uId,jdbcType=INTEGER}");
        }
        
        if (record.getType() != null) {
            sql.VALUES("type", "#{type,jdbcType=VARCHAR}");
        }
        
        if (record.getTrajectory() != null) {
            sql.VALUES("trajectory", "#{trajectory,jdbcType=VARCHAR}");
        }
        
        if (record.getCompleteFlag() != null) {
            sql.VALUES("complete_flag", "#{completeFlag,jdbcType=INTEGER}");
        }
        
        if (record.getInsertTime() != null) {
            sql.VALUES("insert_time", "#{insertTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getInsertId() != null) {
            sql.VALUES("insert_id", "#{insertId,jdbcType=INTEGER}");
        }
        
        if (record.getUpdateTime() != null) {
            sql.VALUES("update_time", "#{updateTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getUpdateId() != null) {
            sql.VALUES("update_id", "#{updateId,jdbcType=INTEGER}");
        }
        
        return sql.toString();
    }

    public String updateByPrimaryKeySelective(ExpressEntity record) {
        SQL sql = new SQL();
        sql.UPDATE("t_express");
        
        if (record.getTrajectory() != null) {
            sql.SET("trajectory = #{trajectory,jdbcType=VARCHAR}");
        }
        
        if (record.getCompleteFlag() != null) {
            sql.SET("complete_flag = #{completeFlag,jdbcType=INTEGER}");
        }
        
        if (record.getInsertTime() != null) {
            sql.SET("insert_time = #{insertTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getInsertId() != null) {
            sql.SET("insert_id = #{insertId,jdbcType=INTEGER}");
        }
        
        if (record.getUpdateTime() != null) {
            sql.SET("update_time = #{updateTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getUpdateId() != null) {
            sql.SET("update_id = #{updateId,jdbcType=INTEGER}");
        }
        
        sql.WHERE("post_id = #{postId,jdbcType=VARCHAR}");
        sql.WHERE("u_id = #{uId,jdbcType=INTEGER}");

        return sql.toString();
    }
}