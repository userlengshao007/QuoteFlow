package com.quoteflow.dto;

import java.math.BigDecimal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * 单元测试： {@link QuoteDetailRequest}.
 *
 * @author zhangyujie
 */
class QuoteDetailRequestTest {

    /**
     * 测试小计金额计算。
     */
    @Test
    void calculateSubtotalShouldMultiplyQuantityAndUnitPrice() {
        QuoteDetailRequest request = new QuoteDetailRequest();
        request.setQuantity(new BigDecimal("2.5"));
        request.setUnitPrice(new BigDecimal("100.00"));

        BigDecimal subtotal = request.calculateSubtotal();

        Assertions.assertEquals(new BigDecimal("250.000"), subtotal);
    }
}
