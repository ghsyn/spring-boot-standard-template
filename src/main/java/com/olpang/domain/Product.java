package com.olpang.domain;

import com.olpang.domain.ProductEditor.ProductEditorBuilder;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 제품 정보 엔티티
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String brand;

    @Lob
    private String description;

    @Builder
    public Product(String name, String brand, String description) {
        this.name = name;
        this.brand = brand;
        this.description = description;
    }

    /**
     * ProductEditorBuilder 생성
     * 불변 객체 기반 수정 전략으로 의도치 않은 필드 변경 방지
     * 파라미터 순서, 타입, 개수에 의존하지 않고 요청 필드에 정확히 매핑
     *
     * @return {@link ProductEditorBuilder}
     */
    public ProductEditorBuilder toEditor() {
        return ProductEditor.builder()
                .name(name)
                .brand(brand)
                .description(description);
    }

    /**
     * ProductEditor 객체 기반 필드 수정
     * 직접 필드 변경을 방지하여 일관성과 변경 추적성 확보
     *
     * @param productEditor 수정할 값
     */
    public void edit(ProductEditor productEditor) {
        name = productEditor.getName();
        brand = productEditor.getBrand();
        description = productEditor.getDescription();
    }
}
