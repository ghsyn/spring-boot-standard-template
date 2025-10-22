package com.olpang.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductCreateRequest {

    @NotBlank(message = "제품명을 입력해주세요.")
    private String name;

    @NotBlank(message = "제조사를 입력해주세요.")
    private String brand;

    @NotBlank(message = "제품 설명을 입력해주세요.")
    private String description;

    @Builder
    public ProductCreateRequest(String name, String brand, String description) {
        this.name = name;
        this.brand = brand;
        this.description = description;
    }
}
