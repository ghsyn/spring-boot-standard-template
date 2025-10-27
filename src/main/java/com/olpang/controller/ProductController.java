package com.olpang.controller;

import com.olpang.request.ProductCreateRequest;
import com.olpang.response.ProductDetailsResponse;
import com.olpang.response.ProductListResponse;
import com.olpang.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
     * 제품 단건 조회
     * @param productId 제품 고유 ID
     * @return {@link ProductDetailsResponse} 제품 상세 응답 DTO
     */
    @GetMapping("/api/v1/products/{productId}")
    public ProductDetailsResponse getDetails(@PathVariable Long productId) {
        return productService.getDetails(productId);
    }

    /**
     * 제품 목록 조회(검색 쿼리, 페이징, 정렬: price, popularity)
     * @return {@link ProductListResponse} 제품 목록 응답 DTO 리스트
     */
    @GetMapping("/api/v1/products")
    public List<ProductListResponse> getList() {
        return productService.getList();
    }
}
