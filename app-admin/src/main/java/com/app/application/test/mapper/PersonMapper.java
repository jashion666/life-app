package com.app.application.test.mapper;


import com.app.application.test.model.PersonDto;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface PersonMapper {

    @Select("SELECT * FROM person")
    List<PersonDto> selectPerson();


    @Select("SELECT * FROM person where homeCode = #{homeCode}")
    PersonDto search(String homeCode);

    @Insert("insert into person " +
            " values" +
            " (#{homeCode},#{name},#{lastTime},#{thisTime})")
    int insert(PersonDto personDto);

    @Update("update  person " +
            " set" +
            " NAME=#{name}," +
            " LASTTIME=#{lastTime}," +
            " THISTIME=#{thisTime}" +
            " where HOMECODE=#{homeCode}")
    int update(PersonDto personDto);

    @Delete("Delete From person " +
            "where HOMECODE=#{homeCode}")
    int delete(String homeCode);

}