package com.quoteflow.dto;

import java.math.BigDecimal;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 报价明细请求。
 *
 * @author zhangyujie
 */
public class QuoteDetailRequest {

    /**
     * 产品名称。
     */
    @NotBlank
    private String productName;

    /**
     * 规格型号。
     */
    private String specification;

    /**
     * 数量。
     */
    @NotNull
    @DecimalMin(value = "0.00", inclusive = false)
    private BigDecimal quantity;

    /**
     * 单价。
     */
    @NotNull
    @DecimalMin(value = "0.00", inclusive = false)
    private BigDecimal unitPrice;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    /**
     * 计算小计金额。
     *
     * @return 小计金额
     */
    public BigDecimal calculateSubtotal() {
        return quantity.multiply(unitPrice);
    }
}
