package dev.raul.btgpactual.orderms.controller.dto;

import java.math.BigDecimal;

public record CustomerSummaryResponse(Long totalOrders, BigDecimal totalAmount) {
}
