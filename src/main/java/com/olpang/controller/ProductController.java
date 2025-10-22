package com.olpang.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductController {

    /**
     * 제품 목록 조회
     * @return '제품 목록 조회'
     */
    @GetMapping("/api/v1/products")
    public String getList() {
        return "제품 목록 조회";
    }
}
