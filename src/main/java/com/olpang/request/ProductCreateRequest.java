package com.olpang.request;

import com.olpang.exception.InvalidRequestException;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

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

    public void doValidate() {
        if (description.matches(".*<script>.*</script>.*")) {
            throw new InvalidRequestException("description", "제품 정보에 스크립트 태그를 사용할 수 없습니다.");
        }
    }
}
