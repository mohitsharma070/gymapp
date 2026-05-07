package com.gymapp.payment.service;

import com.gymapp.common.constants.PaymentConstants;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.HexFormat;
import java.util.Map;
import java.util.UUID;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RazorpayService {

    private static final String HMAC_SHA256 = "HmacSHA256";

    @Value("${app.payment.razorpay.key-secret:}")
    private String razorpayKeySecret;

    public Map<String, Object> createOrder(Integer amount, String currency) {
        Map<String, Object> order = new HashMap<>();
        order.put("id", "order_" + UUID.randomUUID().toString().replace("-", ""));
        order.put("amount", amount);
        order.put("currency", currency);
        order.put("status", PaymentConstants.ORDER_STATUS_CREATED);
        return order;
    }

    public boolean verifyPaymentSignature(String orderId, String paymentId, String signature) {
        if (orderId == null || paymentId == null || signature == null || signature.isBlank()) {
            return false;
        }
        if (razorpayKeySecret == null || razorpayKeySecret.isBlank()) {
            throw new IllegalStateException(
                    "RAZORPAY_KEY_SECRET is not configured. Cannot verify payment signatures.");
        }
        try {
            String payload = orderId + "|" + paymentId;
            Mac mac = Mac.getInstance(HMAC_SHA256);
            SecretKeySpec keySpec = new SecretKeySpec(
                    razorpayKeySecret.getBytes(StandardCharsets.UTF_8), HMAC_SHA256);
            mac.init(keySpec);
            byte[] hash = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
            String expectedSignature = HexFormat.of().formatHex(hash);
            // Constant-time comparison to prevent timing attacks
            return MessageDigest.isEqual(
                    expectedSignature.getBytes(StandardCharsets.UTF_8),
                    signature.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException | InvalidKeyException ex) {
            throw new IllegalStateException("Failed to verify Razorpay signature", ex);
        }
    }
}
