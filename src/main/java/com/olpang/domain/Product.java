package com.olpang.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
     * ProductEditor: 인자값을 타입과 파라미터의 순서에 의존하지 않고 정확한 필드에 꽂아주기 위해 사용.
     * @return {@link ProductEditor.ProductEditorBuilder}
     */
    public ProductEditor.ProductEditorBuilder toEditor() {
        return ProductEditor.builder()
                .name(name)
                .brand(brand)
                .description(description);
    }

    public void edit(ProductEditor productEditor) {
        name = productEditor.getName();
        brand = productEditor.getBrand();
        description = productEditor.getDescription();
    }
}
