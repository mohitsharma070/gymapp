package com.gymapp.subscription.service;

import com.gymapp.common.constants.SubscriptionConstants;
import com.gymapp.common.constants.ErrorMessages;
import com.gymapp.common.exception.BadRequestException;
import com.gymapp.common.exception.ResourceNotFoundException;
import com.gymapp.subscription.entity.Subscription;
import com.gymapp.subscription.enums.SubscriptionStatus;
import com.gymapp.subscription.repository.SubscriptionRepository;
import com.gymapp.user.entity.User;
import com.gymapp.user.repository.UserRepository;
import java.time.LocalDate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository, UserRepository userRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public Subscription getMySubscription(Long userId) {
        return subscriptionRepository.findTopByUserIdOrderByCreatedAtDesc(userId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.NO_SUBSCRIPTION_FOUND_FOR_USER_ID + userId));
    }

    @Transactional
    public Subscription startSubscription(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.USER_NOT_FOUND_WITH_ID + userId));

        subscriptionRepository.findTopByUserIdOrderByCreatedAtDesc(userId).ifPresent(existing -> {
            if (existing.getStatus() == SubscriptionStatus.ACTIVE) {
                throw new BadRequestException(ErrorMessages.ACTIVE_SUBSCRIPTION_ALREADY_EXISTS);
            }
        });

        Subscription subscription = new Subscription();
        subscription.setUser(user);
        subscription.setPlanName(SubscriptionConstants.PLAN_PREMIUM);
        subscription.setStatus(SubscriptionStatus.ACTIVE);
        subscription.setStartDate(LocalDate.now());
        subscription.setEndDate(LocalDate.now().plusMonths(1));

        return subscriptionRepository.save(subscription);
    }

    @Transactional
    public Subscription cancelSubscription(Long userId) {
        Subscription subscription = subscriptionRepository.findTopByUserIdOrderByCreatedAtDesc(userId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.NO_SUBSCRIPTION_FOUND_FOR_USER_ID + userId));

        if (subscription.getStatus() != SubscriptionStatus.ACTIVE) {
            throw new BadRequestException(ErrorMessages.ONLY_ACTIVE_SUBSCRIPTIONS_CAN_BE_CANCELED);
        }

        subscription.setStatus(SubscriptionStatus.CANCELED);
        return subscriptionRepository.save(subscription);
    }
}
