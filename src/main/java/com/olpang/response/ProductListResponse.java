package com.olpang.response;

import com.olpang.domain.Product;
import lombok.Builder;
import lombok.Getter;

import static java.lang.Math.min;

/**
 * 제품 목록(요약 정보) 응답 DTO 클래스
 * - 리스트 및 검색 결과용
 * - 성능을 위해 최소 필드만 포함
 */
@Getter
public class ProductListResponse {

    private final Long id;
    private final String name;
    private final String brand;
    private final String description;

    public ProductListResponse(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.brand = product.getBrand();
        this.description = product.getDescription();
    }

    /**
     * name 문자열 길이 최대 10자
     * description 문자열 길이 최대 50자
     * (::목록 내 제품 정보 요약)
     */
    @Builder
    public ProductListResponse(Long id, String name, String brand, String description) {
        this.id = id;
        this.name = name.substring(0, min(name.length(), 10));
        this.brand = brand;
        this.description = description.substring(0, min(description.length(), 50));
    }
}
