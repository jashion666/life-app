package com.app.application.test;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author :wkh.
 * @date :2019/10/31.
 */
@RestController
public class TestController {

    @RequestMapping("tests")
    public List<Map<String, Object>> test() {
        return getData();
    }

    @RequestMapping("saves")
    public Map<String, Object> save(@RequestBody Map<String,Object> homeMap) {
        System.out.println(homeMap.get("homeCode"));
        return homeMap;
    }

    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> result = new ArrayList<>();

        Map<String, Object> map = new HashMap<>();
        map.put("homeCode", "0101");
        map.put("name", "太郎");
        map.put("lastTime", "102");
        map.put("thisTime", "");

        Map<String, Object> map1 = new HashMap<>();
        map1.put("homeCode", "0102");
        map1.put("name", "次郎");
        map1.put("lastTime", "102");
        map1.put("thisTime", "");

        Map<String, Object> map2 = new HashMap<>();
        map2.put("homeCode", "0103");
        map2.put("name", "三郎");
        map2.put("lastTime", "103");
        map2.put("thisTime", "");
        result.add(map);
        result.add(map1);
        result.add(map2);
        return result;
    }
}
