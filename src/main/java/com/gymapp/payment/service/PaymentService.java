package com.gymapp.payment.service;

import com.gymapp.common.constants.AppConstants;
import com.gymapp.common.constants.ErrorMessages;
import com.gymapp.common.constants.PaymentConstants;
import com.gymapp.common.exception.BadRequestException;
import com.gymapp.common.exception.ResourceNotFoundException;
import com.gymapp.common.util.RepositoryHelper;
import com.gymapp.payment.dto.CreateOrderRequest;
import com.gymapp.payment.entity.Payment;
import com.gymapp.payment.repository.PaymentRepository;
import com.gymapp.subscription.entity.Subscription;
import com.gymapp.subscription.repository.SubscriptionRepository;
import java.util.HashMap;
import java.util.Map;
import org.springframework.security.access.AccessDeniedException;
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
    public Map<String, Object> createOrder(CreateOrderRequest request, Long userId) {
        Subscription subscription = RepositoryHelper.getOrThrow(
                subscriptionRepository.findById(request.getSubscriptionId()),
                () -> new ResourceNotFoundException(
                        ErrorMessages.SUBSCRIPTION_NOT_FOUND_WITH_ID + request.getSubscriptionId()));

        if (!subscription.getUser().getId().equals(userId)) {
            throw new AccessDeniedException(ErrorMessages.ACCESS_DENIED);
        }

        Map<String, Object> order = razorpayService.createOrder(request.getAmount(), AppConstants.CURRENCY_INR);

        Payment payment = new Payment();
        payment.setSubscription(subscription);
        payment.setOrderId((String) order.get("id"));
        payment.setAmount(request.getAmount());
        payment.setCurrency(AppConstants.CURRENCY_INR);
        payment.setStatus(PaymentConstants.PAYMENT_STATUS_CREATED);
        paymentRepository.save(payment);

        Map<String, Object> response = new HashMap<>();
        response.put("orderId", order.get("id"));
        response.put("amount", order.get("amount"));
        response.put("currency", order.get("currency"));
        response.put("status", order.get("status"));
        return response;
    }

    @Transactional
    public Map<String, Object> verifyPayment(String orderId, String paymentId, String signature, Long userId) {
        Payment payment = RepositoryHelper.getOrThrow(
                paymentRepository.findByOrderId(orderId),
                () -> new ResourceNotFoundException(ErrorMessages.PAYMENT_NOT_FOUND_FOR_ORDER_ID + orderId));

        if (!payment.getSubscription().getUser().getId().equals(userId)) {
            throw new AccessDeniedException(ErrorMessages.ACCESS_DENIED);
        }

        boolean valid = razorpayService.verifyPaymentSignature(orderId, paymentId, signature);
        if (!valid) {
            throw new BadRequestException(ErrorMessages.INVALID_PAYMENT_SIGNATURE);
        }

        payment.setPaymentId(paymentId);
        payment.setStatus(PaymentConstants.PAYMENT_STATUS_SUCCESS);
        paymentRepository.save(payment);

        Map<String, Object> response = new HashMap<>();
        response.put("verified", true);
        response.put("orderId", orderId);
        response.put("paymentId", paymentId);
        response.put("status", PaymentConstants.PAYMENT_STATUS_SUCCESS);
        return response;
    }
}


