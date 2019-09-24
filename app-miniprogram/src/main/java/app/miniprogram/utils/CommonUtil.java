package app.miniprogram.utils;

import app.miniprogram.application.express.entity.TrajectoryEntity;
import com.alibaba.fastjson.JSONObject;
import com.app.utils.ApiUtil;
import com.fasterxml.jackson.core.type.TypeReference;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * @author :wkh.
 * @date :2019/8/9.
 */
public class CommonUtil extends ApiUtil {

    /**
     * 将物流轨迹转换成字符串
     *
     * @param entity 实体类
     * @return 字符串
     */
    public static String formatExpressData(TrajectoryEntity entity) {
        return Optional.ofNullable(entity).map(e -> JSONObject.toJSONString(e.getData())).orElse("");
    }

    /**
     * 将物流轨迹转换成实体类
     *
     * @param trajectory 字符串
     * @return 实体类
     */
    public static List<TrajectoryEntity.DataBean> parseExpressData(String trajectory) {
        try {
            return new JsonUtil().getCustomObjectMapper().readValue(trajectory, new TypeReference<List<TrajectoryEntity.DataBean>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("类型转换异常");
        }
    }

    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
