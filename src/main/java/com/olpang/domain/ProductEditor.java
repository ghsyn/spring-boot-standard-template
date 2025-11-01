package com.olpang.domain;

import lombok.Builder;
import lombok.Getter;

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
