package com.gymapp.payment.controller;

import com.gymapp.common.dto.ApiResponse;
import com.gymapp.common.controller.BaseController;
import com.gymapp.common.constants.ApiMessages;
import com.gymapp.common.web.annotation.CurrentUserId;
import com.gymapp.payment.dto.CreateOrderRequest;
import com.gymapp.payment.service.PaymentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.Map;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
@Validated
public class PaymentController extends BaseController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/create-order")
    public ApiResponse<Map<String, Object>> createOrder(
            @Valid @RequestBody CreateOrderRequest request,
            @CurrentUserId Long userId) {
        Map<String, Object> response = paymentService.createOrder(request, userId);
        return success(String.format(ApiMessages.CREATED_SUCCESSFULLY, "Payment order"), response);
    }

    @PostMapping("/verify")
    public ApiResponse<Map<String, Object>> verifyPayment(
            @RequestParam @NotBlank(message = "Order id is required") String orderId,
            @RequestParam @NotBlank(message = "Payment id is required") String paymentId,
            @RequestParam @NotBlank(message = "Signature is required") String signature,
            @CurrentUserId Long userId) {
        Map<String, Object> response = paymentService.verifyPayment(orderId, paymentId, signature, userId);
        return success(String.format(ApiMessages.UPDATED_SUCCESSFULLY, "Payment"), response);
    }
}


