package com.olpang.controller;

import com.olpang.request.ProductCreateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ProductController {

    /**
     * 제품 등록
     * @return '제품 등록'
     */
    @PostMapping("/api/v1/products")
    public String post(@RequestBody ProductCreateRequest request) {
        log.info("request = {}", request.toString());
        return "제품 등록";
    }

    /**
     * 제품 목록 조회
     * @return '제품 목록 조회'
     */
    @GetMapping("/api/v1/products")
    public String getList() {
        return "제품 목록 조회";
    }
}
