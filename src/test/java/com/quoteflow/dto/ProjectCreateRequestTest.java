package com.quoteflow.dto;

import java.math.BigDecimal;
import java.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link ProjectCreateRequest}.
 *
 * @author QuoteFlow
 */
class ProjectCreateRequestTest {

    /**
     * Tests total quote amount calculation.
     */
    @Test
    void calculateTotalQuoteAmountShouldSumAllDetailSubtotals() {
        ProjectCreateRequest request = new ProjectCreateRequest();
        QuoteDetailRequest first = new QuoteDetailRequest();
        first.setQuantity(new BigDecimal("2"));
        first.setUnitPrice(new BigDecimal("100.00"));

        QuoteDetailRequest second = new QuoteDetailRequest();
        second.setQuantity(new BigDecimal("3"));
        second.setUnitPrice(new BigDecimal("50.00"));
        request.setQuoteDetails(Arrays.asList(first, second));

        BigDecimal totalQuoteAmount = request.calculateTotalQuoteAmount();

        Assertions.assertEquals(new BigDecimal("350.00"), totalQuoteAmount);
    }
}
