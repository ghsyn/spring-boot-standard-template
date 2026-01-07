package com.sytk.booking.response;

import lombok.Builder;
import lombok.Getter;

/**
 * 제품 단건 조회용 상세 정보 응답 DTO
 * 엔티티 직접 노출하지 않고, 서비스 정책에 맞게 응답 형태 변환
 */
@Getter
public class ProductDetailsResponse {

    private final Long id;
    private final String name;
    private final String brand;
    private final String description;

    @Builder
    public ProductDetailsResponse(Long id, String name, String brand, String description) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.description = description;
    }
}
