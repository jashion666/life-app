package app.miniprogram.application.login.mapper;

import app.miniprogram.application.login.entity.Authority;
import app.miniprogram.application.login.entity.Role;
import app.miniprogram.application.login.entity.UserEntity;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

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

    /**
     * 查询角色以及menu
     *
     * @param openId id
     * @return 角色信息
     */
    @Select({
            "select",
            "u_id as uId,u_id as rUid, username, phone, insert_time, update_time",
            "from t_user",
            "where open_id = #{openId,jdbcType=VARCHAR}"
    })
    @Results({
            @Result(property = "roleList", column = "rUid",
                    many = @Many(select = "app.miniprogram.application.login.mapper.UserEntityMapper.selectRoleByUserId"))
    })
    UserEntity selectWithRole(String openId);

    /**
     * 查询角色信息
     *
     * @param rUid id
     * @return 角色信息
     */
    @Select({"select role_id as roleId,role_id as aRoleId,role_name,locked from role where u_id = #{uId}"})
    @Results({
            @Result(property = "authorities" ,column = "aRoleId",
            many = @Many(select = "app.miniprogram.application.login.mapper.UserEntityMapper.selectAuthorityByUserId"))
    })
    List<Role> selectRoleByUserId(Long rUid);

    /**
     * 查询角色权限
     *
     * @param aRoleId id
     * @return 角色信息
     */
    @Select({"select menu_id,menu_name from authority where role_id = #{roleId}"})
    List<Authority> selectAuthorityByUserId(Long aRoleId);

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