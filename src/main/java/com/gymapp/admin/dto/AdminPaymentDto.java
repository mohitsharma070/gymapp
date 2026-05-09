package com.gymapp.admin.dto;

import com.gymapp.payment.entity.Payment;
import java.time.LocalDateTime;

public class AdminPaymentDto {

    private Long id;
    private Long subscriptionId;
    private String orderId;
    private String paymentId;
    private Integer amount;
    private String currency;
    private String status;
    private LocalDateTime createdAt;

    public static AdminPaymentDto from(Payment payment) {
        AdminPaymentDto dto = new AdminPaymentDto();
        dto.id = payment.getId();
        dto.subscriptionId = payment.getSubscription() != null ? payment.getSubscription().getId() : null;
        dto.orderId = payment.getOrderId();
        dto.paymentId = payment.getPaymentId();
        dto.amount = payment.getAmount();
        dto.currency = payment.getCurrency();
        dto.status = payment.getStatus();
        dto.createdAt = payment.getCreatedAt();
        return dto;
    }

    public Long getId() { return id; }
    public Long getSubscriptionId() { return subscriptionId; }
    public String getOrderId() { return orderId; }
    public String getPaymentId() { return paymentId; }
    public Integer getAmount() { return amount; }
    public String getCurrency() { return currency; }
    public String getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}


