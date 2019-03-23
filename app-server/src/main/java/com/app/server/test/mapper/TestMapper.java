package com.app.server.test.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @author :wkh.
 */
@Mapper
@Repository
public interface TestMapper {

    /**
     * 查询
     * @param name 姓名
     * @return id
     */
    @Select("SELECT ID FROM T_USER WHERE username = #{name}")
    String selectByName(@Param("name") String name);

    /**
     * 查询
     * @param name 姓名
     * @return id
     */
    @Select("DELETE FROM T_USER WHERE username = #{name}")
    Integer delete(@Param("name") String name);
}
