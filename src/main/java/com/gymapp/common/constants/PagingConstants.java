package com.gymapp.common.constants;

import org.springframework.data.domain.Sort;

public final class PagingConstants {

    public static final int DEFAULT_PAGE = 0;
    public static final int DEFAULT_SIZE = 20;
    public static final String DEFAULT_SORT = "id";
    public static final Sort.Direction DEFAULT_DIRECTION = Sort.Direction.DESC;

    private PagingConstants() {
    }
}


