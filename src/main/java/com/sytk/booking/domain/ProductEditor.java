package com.sytk.booking.domain;

import lombok.Builder;
import lombok.Getter;

/**
 * Product 도메인 수정 전용 객체
 * 엔티티 내부에서 직접 Builder를 통해 안전하게 변경하도록 함
 */
@Getter
public class ProductEditor {

    private final String name;
    private final String brand;
    private final String description;

    @Builder
    public ProductEditor(String name, String brand, String description) {
        this.name = name;
        this.brand = brand;
        this.description = description;
    }
}
