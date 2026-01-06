package com.sytk.booking.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

/**
 * 제품 수정 요청 DTO
 */
@Getter
@Builder
public class ProductEditRequest {

    @NotBlank(message = "제품명을 입력해주세요.")
    private String name;

    @NotBlank(message = "제조사를 입력해주세요.")
    private String brand;

    @NotBlank(message = "제품 설명을 입력해주세요.")
    private String description;

    public ProductEditRequest(String name, String brand, String description) {
        this.name = name;
        this.brand = brand;
        this.description = description;
    }
}
