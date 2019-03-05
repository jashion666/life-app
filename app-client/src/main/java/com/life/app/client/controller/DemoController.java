package com.life.app.client.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.life.app.service.DemoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wkh.
 */
@RestController
@RequestMapping("/demo")
public class DemoController {

  @Reference(version = "1.0.0", timeout = 100000, retries = 3, group = "DemoServiceImpl")
  private DemoService demoService;

  @Reference(version = "1.0.0", timeout = 100000, retries = 3, group = "CarServiceImpl")
  private DemoService carService;

  @RequestMapping("/test")
  public String hello() {

    return demoService.sayHello("WORLD") + this.carService.sayHello("666啊");
  }
}
