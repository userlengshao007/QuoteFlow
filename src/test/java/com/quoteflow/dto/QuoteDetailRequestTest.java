package com.quoteflow.dto;

import java.math.BigDecimal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link QuoteDetailRequest}.
 *
 * @author QuoteFlow
 */
class QuoteDetailRequestTest {

    /**
     * Tests subtotal calculation.
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
