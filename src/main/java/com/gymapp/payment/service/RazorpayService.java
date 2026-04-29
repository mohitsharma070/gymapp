package com.gymapp.payment.service;

import com.gymapp.common.constants.PaymentConstants;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class RazorpayService {

    public Map<String, Object> createOrder(Integer amount, String currency) {
        Map<String, Object> order = new HashMap<>();
        order.put("id", "order_" + UUID.randomUUID().toString().replace("-", ""));
        order.put("amount", amount);
        order.put("currency", currency);
        order.put("status", PaymentConstants.ORDER_STATUS_CREATED);
        return order;
    }

    public boolean verifyPaymentSignature(String orderId, String paymentId, String signature) {
        return signature != null && !signature.isBlank() && orderId != null && paymentId != null;
    }
}
