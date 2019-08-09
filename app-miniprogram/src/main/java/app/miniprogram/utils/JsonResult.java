package app.miniprogram.utils;

import app.miniprogram.enums.ResultCodeEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class JsonResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private int state;

    private List<String> msgList;

    private T result;

    private JsonResult() {

    }

    @SuppressWarnings("unchecked")
    public static JsonResult success() {
        JsonResult result = new JsonResult();
        result.setState(ResultCodeEnum.RESULT_SUCCESS_CODE.getCode());
        result.setResult(null);
        return result;
    }

    @SuppressWarnings("unchecked")
    public static JsonResult success(String message) {
        JsonResult result = new JsonResult();
        result.setState(ResultCodeEnum.RESULT_SUCCESS_CODE.getCode());
        List<String> msgList = new ArrayList<>();
        msgList.add(message);
        result.setMsgList(msgList);
        result.setResult(null);
        return result;
    }

    @SuppressWarnings("unchecked")
    public static <T> JsonResult success(String message, T data) {
        JsonResult result = new JsonResult();
        List<String> msgList = new ArrayList<>();
        msgList.add(message);
        result.setState(ResultCodeEnum.RESULT_SUCCESS_CODE.getCode());
        result.setMsgList(msgList);
        result.setResult(data);
        return result;
    }

    @SuppressWarnings("unchecked")
    public static <T> JsonResult success(T data) {
        JsonResult result = new JsonResult();
        result.setState(ResultCodeEnum.RESULT_SUCCESS_CODE.getCode());
        result.setResult(data);
        return result;
    }

    @SuppressWarnings("unchecked")
    public static JsonResult failed() {
        JsonResult result = new JsonResult();
        result.setState(ResultCodeEnum.RESULT_FAILED_CODE.getCode());
        result.setResult(null);
        return result;
    }

    @SuppressWarnings("unchecked")
    public static JsonResult failed(String message) {
        JsonResult result = new JsonResult();
        List<String> msgList = new ArrayList<>();
        msgList.add(message);
        result.setState(ResultCodeEnum.RESULT_FAILED_CODE.getCode());
        result.setMsgList(msgList);
        result.setResult(null);
        return result;
    }

    @SuppressWarnings("unchecked")
    public static JsonResult failed(List<String> errorMsgList) {
        JsonResult result = new JsonResult();
        result.setState(ResultCodeEnum.RESULT_FAILED_CODE.getCode());
        result.setMsgList(errorMsgList);
        result.setResult(null);
        return result;
    }

    @SuppressWarnings("unchecked")
    public static <T> JsonResult warn(String message, T data) {
        JsonResult result = new JsonResult();
        List<String> msgList = new ArrayList<>();
        msgList.add(message);
        result.setState(ResultCodeEnum.RESULT_WARNING_CODE.getCode());
        result.setMsgList(msgList);
        result.setResult(data);
        return result;
    }

    @Override
    public String toString() {
        return "JsonResult[state=" + state + "]";
    }

}