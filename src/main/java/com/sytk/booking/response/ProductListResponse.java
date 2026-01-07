package com.sytk.booking.response;

import com.sytk.booking.domain.Product;
import lombok.Builder;
import lombok.Getter;

import static java.lang.Math.min;

/**
 * 제품 목록 조회용 요약 정보 응답 DTO
 * 성능을 위해 최소 필드만 포함
 */
@Getter
public class ProductListResponse {

    private final Long id;
    private final String name;
    private final String brand;
    private final String description;

    /**
     * Entity -> DTO 변환
     */
    public static ProductListResponse of(Product product) {
        return ProductListResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .brand(product.getBrand())
                .description(product.getDescription())
                .build();
    }

    /**
     * 문자열 요약 처리
     * - name: 최대 10자
     * - description: 최대 50자
     */
    @Builder
    public ProductListResponse(Long id, String name, String brand, String description) {
        this.id = id;
        this.name = name.substring(0, min(name.length(), 10));
        this.brand = brand;
        this.description = description.substring(0, min(description.length(), 50));
    }
}
