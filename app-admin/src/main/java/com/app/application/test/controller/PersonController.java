package com.app.application.test.controller;


import com.app.application.test.model.PersonDto;
import com.app.application.test.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

/**
 * @author :wkh.
 * @date :2019/10/31.
 */
@RestController
public class PersonController {
    @Autowired
    private PersonService personService;

    @RequestMapping("test")
    public List<PersonDto> test() {
        return personService.getPerson();
    }

    @RequestMapping("search")
    public PersonDto search(@RequestBody PersonDto personDto) {
        return personService.search(personDto.getHomeCode());
    }

    @RequestMapping("save")
    public PersonDto save(@RequestBody PersonDto personDto) {
        personService.insert(personDto);
        return personDto;
    }

    @RequestMapping("sync-data")
    public List<PersonDto> syncData(@RequestBody List<PersonDto> personDtoList) {
        personService.updateAll(personDtoList);
        return personDtoList;
    }

    @RequestMapping("update")
    public PersonDto update(@RequestBody PersonDto personDto) {
        personService.update(personDto);
        return personDto;
    }

    @RequestMapping("delete")
    public PersonDto delete(@RequestBody PersonDto personDto) {
        personService.delete(personDto);
        return personDto;
    }
}
