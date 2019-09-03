package app.miniprogram.enums;

import lombok.Getter;

/**
 * @author :wkh.
 * @date :2019/9/3.
 */
@Getter
public enum GeneraBasicEnums {
    /**
     * 中英文混合.
     */
    CHN_ENG("CHN_ENG", "中英文混合"),
    /**
     * 英文.
     */
    ENG("ENG", "英文"),

    /**
     * 葡萄牙语.
     */
    POR("POR", "葡萄牙语"),
    /**
     * 法语.
     */
    FRE("FRE", "法语"),
    /**
     * 德语.
     */
    GER("GER", "德语"),
    /**
     * 意大利语.
     */
    ITA("ITA", "意大利语"),
    /**
     * 西班牙语.
     */
    SPA("SPA", "西班牙语"),
    /**
     * 俄语.
     */
    RUS("RUS", "俄语"),
    /**
     * 日语.
     */
    JAP("JAP", "日语"),
    /**
     * 韩语.
     */
    KOR("KOR", "韩语");

    private String code;

    private String codeValue;

    GeneraBasicEnums(String code, String codeValue) {
        this.code = code;
        this.codeValue = codeValue;
    }

    public static GeneraBasicEnums getInstance(String code) {
        for (GeneraBasicEnums generaBasicEnums : GeneraBasicEnums.values()) {
            if (generaBasicEnums.code.equals(code)) {
                return generaBasicEnums;
            }
        }
        return null;
    }
}
