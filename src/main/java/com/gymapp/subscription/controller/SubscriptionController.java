package com.gymapp.subscription.controller;

import com.gymapp.common.dto.ApiResponse;
import com.gymapp.security.SecurityUser;
import com.gymapp.subscription.entity.Subscription;
import com.gymapp.subscription.service.SubscriptionService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @GetMapping("/me")
    public ApiResponse<Subscription> getMySubscription(Authentication authentication) {
        Long userId = ((SecurityUser) authentication.getPrincipal()).getId();
        Subscription response = subscriptionService.getMySubscription(userId);
        return ApiResponse.success("Subscription fetched successfully", response);
    }

    @PostMapping("/start")
    public ApiResponse<Subscription> startSubscription(Authentication authentication) {
        Long userId = ((SecurityUser) authentication.getPrincipal()).getId();
        Subscription response = subscriptionService.startSubscription(userId);
        return ApiResponse.success("Subscription started successfully", response);
    }

    @PostMapping("/cancel")
    public ApiResponse<Subscription> cancelSubscription(Authentication authentication) {
        Long userId = ((SecurityUser) authentication.getPrincipal()).getId();
        Subscription response = subscriptionService.cancelSubscription(userId);
        return ApiResponse.success("Subscription canceled successfully", response);
    }
}
