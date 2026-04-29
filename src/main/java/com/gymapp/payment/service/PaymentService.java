package com.gymapp.payment.service;

import com.gymapp.common.exception.BadRequestException;
import com.gymapp.common.exception.ResourceNotFoundException;
import com.gymapp.payment.dto.CreateOrderRequest;
import com.gymapp.payment.entity.Payment;
import com.gymapp.payment.repository.PaymentRepository;
import com.gymapp.subscription.entity.Subscription;
import com.gymapp.subscription.repository.SubscriptionRepository;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final RazorpayService razorpayService;

    public PaymentService(
            PaymentRepository paymentRepository,
            SubscriptionRepository subscriptionRepository,
            RazorpayService razorpayService) {
        this.paymentRepository = paymentRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.razorpayService = razorpayService;
    }

    @Transactional
    public Map<String, Object> createOrder(CreateOrderRequest request) {
        Subscription subscription = subscriptionRepository.findById(request.getSubscriptionId())
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found with id: " + request.getSubscriptionId()));

        Map<String, Object> order = razorpayService.createOrder(request.getAmount(), "INR");

        Payment payment = new Payment();
        payment.setSubscription(subscription);
        payment.setOrderId((String) order.get("id"));
        payment.setAmount(request.getAmount());
        payment.setCurrency("INR");
        payment.setStatus("CREATED");
        paymentRepository.save(payment);

        Map<String, Object> response = new HashMap<>();
        response.put("orderId", order.get("id"));
        response.put("amount", order.get("amount"));
        response.put("currency", order.get("currency"));
        response.put("status", order.get("status"));
        return response;
    }

    @Transactional
    public Map<String, Object> verifyPayment(String orderId, String paymentId, String signature) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found for order id: " + orderId));

        boolean valid = razorpayService.verifyPaymentSignature(orderId, paymentId, signature);
        if (!valid) {
            throw new BadRequestException("Invalid payment signature");
        }

        payment.setPaymentId(paymentId);
        payment.setStatus("PAID");
        paymentRepository.save(payment);

        Map<String, Object> response = new HashMap<>();
        response.put("verified", true);
        response.put("orderId", orderId);
        response.put("paymentId", paymentId);
        response.put("status", "PAID");
        return response;
    }
}
