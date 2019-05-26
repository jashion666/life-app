package app.miniprogram.utils;

/**
 * http枚举
 *
 * @author :wkh
 */
public enum ResultCodeEnum {

    /**
     * api success code.
     */
    RESULT_SUCCESS_CODE(1),

    /**
     * api success code.
     */
    RESULT_WARNING_CODE(9),

    /**
     * api failed code.
     */
    RESULT_FAILED_CODE(-1);

    private final int code;

    ResultCodeEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}