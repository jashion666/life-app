package com.app.application.test.service;


import com.app.application.test.mapper.PersonMapper;
import com.app.application.test.model.PersonDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * ユーザーを取得するメソッドを提供します.
 */
@Service
public class PersonService {

    @Autowired
    private PersonMapper personMapper;

    /**
     * ユーザーの詳細情報を取得する（ログイン判断）.
     */
    public List<PersonDto> getPerson() {
        return personMapper.selectPerson();
    }

    public void insert(PersonDto personDto) {
        personMapper.insert(personDto);
    }

    public void updateAll(List<PersonDto> personDtoList) {
        for (PersonDto personDto : personDtoList) {
            update(personDto);
        }
    }

    public void update(PersonDto personDto) {
        personMapper.update(personDto);
    }

    public void delete(PersonDto personDto) {
        personMapper.delete(personDto.getHomeCode());
    }

    public PersonDto search(String homeCode) {
        return personMapper.search(homeCode);
    }
}
