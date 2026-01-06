package com.sytk.booking.controller;

import com.sytk.booking.request.ProductCreateRequest;
import com.sytk.booking.request.ProductEditRequest;
import com.sytk.booking.request.ProductPageRequest;
import com.sytk.booking.response.ProductDetailsResponse;
import com.sytk.booking.response.ProductListResponse;
import com.sytk.booking.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Product 도메인 API 제공
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /**
     * 제품 등록 (관리자::수집 파이프라인용)
     */
    @PostMapping("/api/v1/products")
    public void post(@RequestBody @Valid ProductCreateRequest request) {
        request.doValidate();
        productService.register(request);
    }

    /**
     * 제품 단건 조회
     */
    @GetMapping("/api/v1/products/{productId}")
    public ProductDetailsResponse getDetails(@PathVariable Long productId) {
        return productService.getDetails(productId);
    }

    /**
     * 제품 목록 조회 (검색 쿼리, 페이징, 정렬: price, popularity)
     */
    @GetMapping("/api/v1/products")
    public List<ProductListResponse> getList(@ModelAttribute ProductPageRequest request) {
        return productService.getList(request);
    }

    /**
     * 제품 정보 수정
     */
    @PatchMapping("/api/v1/products/{productId}")
    public void edit(@PathVariable Long productId, @RequestBody @Valid ProductEditRequest request) {
        productService.edit(productId, request);
    }

    /**
     * 제품 삭제
     */
    @DeleteMapping("/api/v1/products/{productId}")
    public void delete(@PathVariable Long productId) {
        productService.delete(productId);
    }
}
