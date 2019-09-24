package app.miniprogram;

import app.miniprogram.utils.JsonResult;
import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author :wkh.
 */
@SpringBootApplication
@EnableScheduling
@EnableDubbo(scanBasePackages = "com.app.miniprogram")
@RestController
public class AppMiniprogramApplication {

    public static void main(String[] args) {
        // 关闭热编译
        System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(AppMiniprogramApplication.class, args);
    }

    @RequestMapping
    public ResponseEntity<JsonResult> error() {
        return new ResponseEntity<>(JsonResult.failed("无效的接口"), HttpStatus.OK);
    }

}
