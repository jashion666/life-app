package app.miniprogram;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author :wkh.
 */
@SpringBootApplication
@EnableScheduling
@EnableDubbo(scanBasePackages = "com.app.miniprogram")
public class AppMiniprogramApplication {

    public static void main(String[] args) {
        // 关闭热编译
        System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(AppMiniprogramApplication.class, args);
    }

}
