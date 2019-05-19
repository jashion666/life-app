package app.miniprogram.utils;

public enum ResultCodeEnum {

  /**
   * api success code.
   */
  RESULT_SUCCESS_CODE(0),

  /**
   * api success code.
   */
  RESULT_WARNING_CODE(1),

  /**
   * api failed code.
   */
  RESULT_FAILED_CODE(9);

  private final int code;

  ResultCodeEnum(int code) {
    this.code = code;
  }

  public int getCode() {
    return code;
  }
}