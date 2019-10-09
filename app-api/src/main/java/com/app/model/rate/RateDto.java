package com.app.model.rate;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 汇率数据传输实体类
 *
 * @author :wkh.
 * @date :2019/9/27.
 */

@Data
@EqualsAndHashCode
public class RateDto implements Serializable {
    private static final long serialVersionUID = 5069767801682904426L;
    /**
     * 货币名称
     */
    private String currencyName;
    /**
     * 交易单位
     */
    private String tradingUnit;
    /**
     * 现汇买入价
     */
    private String buyingRate;
    /**
     * 现钞买入价
     */
    private String cashPurchasePrice;
    /**
     * 现汇卖出价
     */
    private String spotSellingPrice;
    /**
     * 现钞卖出价
     */
    private String cashSellingPrice;
    /**
     * 更新时间
     */
    private String updateTime;
}
