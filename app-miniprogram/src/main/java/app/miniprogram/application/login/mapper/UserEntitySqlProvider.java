package app.miniprogram.application.login.mapper;

import app.miniprogram.application.login.entity.UserEntity;
import org.apache.ibatis.jdbc.SQL;

public class UserEntitySqlProvider {

    public String insertSelective(UserEntity record) {
        SQL sql = new SQL();
        sql.INSERT_INTO("t_user");
        
        if (record.getUId() != null) {
            sql.VALUES("u_id", "#{uId,jdbcType=INTEGER}");
        }
        
        if (record.getOpenId() != null) {
            sql.VALUES("open_id", "#{openId,jdbcType=VARCHAR}");
        }
        
        if (record.getSessionKey() != null) {
            sql.VALUES("session_key", "#{sessionKey,jdbcType=VARCHAR}");
        }
        
        if (record.getUsername() != null) {
            sql.VALUES("username", "#{username,jdbcType=VARCHAR}");
        }
        
        if (record.getPhone() != null) {
            sql.VALUES("phone", "#{phone,jdbcType=VARCHAR}");
        }
        
        if (record.getInsertTime() != null) {
            sql.VALUES("insert_time", "#{insertTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getUpdateTime() != null) {
            sql.VALUES("update_time", "#{updateTime,jdbcType=TIMESTAMP}");
        }
        
        return sql.toString();
    }

    public String updateByPrimaryKeySelective(UserEntity record) {
        SQL sql = new SQL();
        sql.UPDATE("t_user");
        
        if (record.getOpenId() != null) {
            sql.SET("open_id = #{openId,jdbcType=VARCHAR}");
        }
        
        if (record.getSessionKey() != null) {
            sql.SET("session_key = #{sessionKey,jdbcType=VARCHAR}");
        }
        
        if (record.getUsername() != null) {
            sql.SET("username = #{username,jdbcType=VARCHAR}");
        }
        
        if (record.getPhone() != null) {
            sql.SET("phone = #{phone,jdbcType=VARCHAR}");
        }
        
        if (record.getInsertTime() != null) {
            sql.SET("insert_time = #{insertTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getUpdateTime() != null) {
            sql.SET("update_time = #{updateTime,jdbcType=TIMESTAMP}");
        }
        
        sql.WHERE("u_id = #{uId,jdbcType=INTEGER}");
        
        return sql.toString();
    }
}