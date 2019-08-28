package app.miniprogram.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * @author :wkh
 */
@Configuration
@PropertySource({"classpath:/api.properties", "classpath:/dubbo-consumer.properties"})
public class PropertiesConfig {

    private static final String[] BASE_NAMES = {"classpath:/api.properties"};

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource resourceBundleMessageSource = new ReloadableResourceBundleMessageSource();
        resourceBundleMessageSource.setBasenames(BASE_NAMES);
        return resourceBundleMessageSource;

    }
}
