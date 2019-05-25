package app.miniprogram.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class PropertiesUtil {

    @Autowired
    private MessageSource source;

    public String getValue(String code) {
        return this.source.getMessage(code, null, LocaleContextHolder.getLocale());
    }

    public String getValue(String code, String str) {
        return this.source.getMessage(code, new Object[]{str}, LocaleContextHolder.getLocale());
    }

    public String getValue(String code, Object[] obj) {
        return this.source.getMessage(code, obj, LocaleContextHolder.getLocale());
    }

}
