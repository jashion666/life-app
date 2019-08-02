package app.miniprogram.application.express.service.impl;

import app.miniprogram.application.express.service.Express;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 调用api接口方式查询快递
 *
 * @author :wkh.
 * @date :2019/7/31.
 */
@Service("apiExpressImpl")
@Slf4j
public class ApiExpressImpl implements Express {

    @Override
    public Map<String, Object> queryExpress(String postId, String type) {
        log.info("== api查询开始");
        // TODO 需要去申请快递
        log.info("== api快递查询结束");
        throw new IllegalArgumentException("api查询功能未实装");
    }

    @Override
    public Map<String, Object> getExpressTypeMap(String postId) {
        return null;
    }
}
