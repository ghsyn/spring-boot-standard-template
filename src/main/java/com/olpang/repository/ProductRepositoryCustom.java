package com.olpang.repository;

import com.olpang.domain.Product;
import com.olpang.request.ProductPageRequest;

import java.util.List;

/**
 * 제품 커스텀 Repository 인터페이스
 */
public interface ProductRepositoryCustom {

    /**
     * 요청 페이지 내 제품 목록 조회
     *
     * @param productPageRequest 페이지 번호 및 사이즈
     * @return 조회 제품 목록
     */
    List<Product> getList(ProductPageRequest productPageRequest);
}
