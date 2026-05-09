package com.gymapp.subscription.controller;

import com.gymapp.common.dto.ApiResponse;
import com.gymapp.common.controller.BaseController;
import com.gymapp.common.constants.ApiMessages;
import com.gymapp.common.web.annotation.CurrentUserId;
import com.gymapp.subscription.entity.Subscription;
import com.gymapp.subscription.service.SubscriptionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController extends BaseController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @GetMapping("/me")
    public ApiResponse<Subscription> getMySubscription(@CurrentUserId Long userId) {
        Subscription response = subscriptionService.getMySubscription(userId);
        return success(String.format(ApiMessages.FETCHED_SUCCESSFULLY, "Subscription"), response);
    }

    @PostMapping("/start")
    public ApiResponse<Subscription> startSubscription(@CurrentUserId Long userId) {
        Subscription response = subscriptionService.startSubscription(userId);
        return success(String.format(ApiMessages.CREATED_SUCCESSFULLY, "Subscription"), response);
    }

    @PostMapping("/cancel")
    public ApiResponse<Subscription> cancelSubscription(@CurrentUserId Long userId) {
        Subscription response = subscriptionService.cancelSubscription(userId);
        return success(String.format(ApiMessages.UPDATED_SUCCESSFULLY, "Subscription"), response);
    }
}


