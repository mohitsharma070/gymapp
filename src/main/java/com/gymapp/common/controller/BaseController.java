package com.gymapp.common.controller;

import com.gymapp.common.constants.ErrorMessages;
import com.gymapp.common.dto.ApiResponse;
import com.gymapp.security.SecurityUser;
import jakarta.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public abstract class BaseController {

    protected static final String HEADER_X_CORRELATION_ID = "X-Correlation-Id";

    protected <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.success(message, data);
    }

    protected ApiResponse<Void> success(String message) {
        return ApiResponse.success(message, null);
    }

    protected <T> ApiResponse<T> fail(String message) {
        return ApiResponse.fail(message);
    }

    protected SecurityUser getSecurityUser(Authentication authentication) {
        return (SecurityUser) authentication.getPrincipal();
    }

    protected Long getCurrentUserId(Authentication authentication) {
        return getSecurityUser(authentication).getId();
    }

    protected String getCurrentUsername(Authentication authentication) {
        return authentication.getName();
    }

    protected void requireOwnership(Long resourceUserId, Authentication authentication, String deniedMessage) {
        Long currentUserId = getCurrentUserId(authentication);
        if (!currentUserId.equals(resourceUserId)) {
            throw new AccessDeniedException(deniedMessage);
        }
    }

    protected void denyAccess() {
        throw new AccessDeniedException(ErrorMessages.ACCESS_DENIED);
    }

    protected Pageable buildPageable(
            Integer page,
            Integer size,
            String sortBy,
            String direction,
            int defaultPage,
            int defaultSize,
            String defaultSortBy,
            Sort.Direction defaultDirection) {
        int resolvedPage = page != null ? Math.max(page, 0) : defaultPage;
        int resolvedSize = size != null ? Math.max(size, 1) : defaultSize;
        String resolvedSortBy = (sortBy != null && !sortBy.isBlank()) ? sortBy : defaultSortBy;
        Sort.Direction resolvedDirection = parseDirection(direction, defaultDirection);
        return PageRequest.of(resolvedPage, resolvedSize, Sort.by(resolvedDirection, resolvedSortBy));
    }

    protected Sort.Direction parseDirection(String direction, Sort.Direction fallback) {
        if (direction == null || direction.isBlank()) {
            return fallback;
        }
        return Sort.Direction.fromOptionalString(direction).orElse(fallback);
    }

    protected String correlationId(HttpServletRequest request) {
        String id = request.getHeader(HEADER_X_CORRELATION_ID);
        if (id == null || id.isBlank()) {
            return "N/A";
        }
        return id;
    }

    protected Map<String, String> validationErrors(BindingResult bindingResult) {
        Map<String, String> errors = new LinkedHashMap<>();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return errors;
    }
}
