package app.miniprogram.application.login.mapper;

import app.miniprogram.application.login.entity.UserEntity;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface UserEntityMapper {
    @Delete({
            "delete from t_user",
            "where u_id = #{uId,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer uId);

    @Insert({
            "insert into t_user ( open_id, ",
            "session_key, username, ",
            "phone, insert_time, ",
            "update_time)",
            "values ( #{openId,jdbcType=VARCHAR}, ",
            "#{sessionKey,jdbcType=VARCHAR}, #{username,jdbcType=VARCHAR}, ",
            "#{phone,jdbcType=VARCHAR}, NOW(), ",
            "NOW())"
    })
    int insert(UserEntity record);

    @InsertProvider(type = UserEntitySqlProvider.class, method = "insertSelective")
    int insertSelective(UserEntity record);

    @Select({
            "select",
            "u_id, open_id, session_key, username, phone, insert_time, update_time",
            "from t_user",
            "where open_id = #{openId,jdbcType=VARCHAR}"
    })
    UserEntity selectByOpenId(String openId);

    @UpdateProvider(type = UserEntitySqlProvider.class, method = "updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(UserEntity record);

    @Update({
            "update t_user ",
            "set session_key = #{sessionKey,jdbcType=VARCHAR},",
            "username = #{username,jdbcType=VARCHAR},",
            "phone = #{phone,jdbcType=VARCHAR},",
            "update_time = NOW() ",
            "where open_id = #{openId,jdbcType=VARCHAR}"
    })
    int updateByOpenId(UserEntity record);
}