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
    public String save(@RequestBody PersonDto personDto) {
        personService.insert(personDto);
        return "Save OK";
    }

    @RequestMapping("update")
    public String update(@RequestBody PersonDto personDto) {
        personService.update(personDto);
        return "update OK";
    }

    @RequestMapping("delete")
    public String delete(@RequestBody PersonDto personDto) {
        personService.delete(personDto);
        return "delete OK";
    }
}
