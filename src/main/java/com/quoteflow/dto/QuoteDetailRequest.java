package com.quoteflow.dto;

import java.math.BigDecimal;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Quote detail request.
 *
 * @author QuoteFlow
 */
public class QuoteDetailRequest {

    /**
     * Product name.
     */
    @NotBlank
    private String productName;

    /**
     * Specification.
     */
    private String specification;

    /**
     * Quantity.
     */
    @NotNull
    @DecimalMin(value = "0.00", inclusive = false)
    private BigDecimal quantity;

    /**
     * Unit price.
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
     * Calculates subtotal amount.
     *
     * @return subtotal amount
     */
    public BigDecimal calculateSubtotal() {
        return quantity.multiply(unitPrice);
    }
}
