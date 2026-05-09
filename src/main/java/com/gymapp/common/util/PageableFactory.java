package com.gymapp.common.util;

import com.gymapp.common.dto.PageRequestDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public final class PageableFactory {

    private PageableFactory() {
    }

    public static Pageable from(
            PageRequestDto request,
            int defaultPage,
            int defaultSize,
            String defaultSortBy,
            Sort.Direction defaultDirection) {
        int resolvedPage = request != null && request.getPage() != null ? Math.max(request.getPage(), 0) : defaultPage;
        int resolvedSize = request != null && request.getSize() != null ? Math.max(request.getSize(), 1) : defaultSize;
        String resolvedSortBy = request != null
                && request.getSortBy() != null
                && !request.getSortBy().isBlank()
                ? request.getSortBy()
                : defaultSortBy;
        Sort.Direction resolvedDirection = parseDirection(request != null ? request.getDirection() : null, defaultDirection);
        return PageRequest.of(resolvedPage, resolvedSize, Sort.by(resolvedDirection, resolvedSortBy));
    }

    private static Sort.Direction parseDirection(String direction, Sort.Direction fallback) {
        if (direction == null || direction.isBlank()) {
            return fallback;
        }
        return Sort.Direction.fromOptionalString(direction).orElse(fallback);
    }
}


