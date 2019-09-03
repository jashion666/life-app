package app.miniprogram.enums;

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
    RESULT_FAILED_CODE(-1),
    /**
     * api failed code.
     */
    FORBIDDEN_CODE(403),
    /**
     * 没有权限
     */
    UNAUTHORIZED(401),
    /**
     * token过期
     */
    TOKEN_EXPIRE(9999);

    private final int code;

    ResultCodeEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}