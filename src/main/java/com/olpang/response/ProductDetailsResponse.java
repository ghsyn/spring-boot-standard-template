package com.olpang.response;

import com.olpang.domain.Product;
import lombok.Builder;
import lombok.Getter;

/**
 * 제품 상세 정보 응답 DTO 클래스
 * - 상세 페이지용
 * - 설명, 이미지, 재고 등 많은 정보 포함
 *
 * +) 엔티티는 DB 설계 변경에 따라 바뀔 수 있으므로 직접 노출하지 않고,
 * 서비스 정책에 맞는 응답 형태로 변환하는 API 응답 전용 클래스 별도로 둠
 */
@Getter
public class ProductDetailsResponse {

    private final Long id;
    private final String name;
    private final String brand;
    private final String description;

    public ProductDetailsResponse(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.brand = product.getBrand();
        this.description = product.getDescription();
    }

    @Builder
    public ProductDetailsResponse(Long id, String name, String brand, String description) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.description = description;
    }
}
