package com.olpang.controller;

import com.olpang.request.ProductCreateRequest;
import com.olpang.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor    // 생성자 injection
public class ProductController {

    private final ProductService productService;

    /**
     * 제품 등록(관리자::수집 파이프라인용)
     * @param request 제품 등록 요청 DTO
     */
    @PostMapping("/api/v1/products")
    public void post(@RequestBody @Valid ProductCreateRequest request) {
        productService.register(request);
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
