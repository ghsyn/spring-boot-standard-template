package com.sytk.booking.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import static java.lang.Math.*;

/**
 * 제품 목록 조회용 페이지 요청 DTO
 */
@Getter
@Setter
@Builder
public class ProductPageRequest {

    private static final int MAX_SIZE = 2000;

    @Builder.Default
    private Integer page = 1;

    @Builder.Default
    private Integer size = 10;

    /**
     * 페이지 번호 offset 계산
     * @return 조회 시작 offset
     */
    public long getOffset() {
        return (long) (max(1, page) - 1) * min(size, MAX_SIZE);
    }
}