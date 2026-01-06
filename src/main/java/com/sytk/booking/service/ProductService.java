package com.sytk.booking.service;

import com.sytk.booking.domain.Product;
import com.sytk.booking.domain.ProductEditor;
import com.sytk.booking.domain.ProductEditor.ProductEditorBuilder;
import com.sytk.booking.exception.ProductNotFoundException;
import com.sytk.booking.repository.ProductRepository;
import com.sytk.booking.request.ProductCreateRequest;
import com.sytk.booking.request.ProductEditRequest;
import com.sytk.booking.request.ProductPageRequest;
import com.sytk.booking.response.ProductDetailsResponse;
import com.sytk.booking.response.ProductListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Product 도메인 비즈니스 로직 처리
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    /**
     * 제품 등록
     */
    public void register(ProductCreateRequest request) {
        Product product = Product.builder()
                .name(request.getName())
                .brand(request.getBrand())
                .description(request.getDescription())
                .build();

        productRepository.save(product);
    }

    /**
     * 제품 상세 조회
     *
     * @throws {@link ProductNotFoundException} 제품 존재하지 않을 경우
     */
    public ProductDetailsResponse getDetails(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);

        return ProductDetailsResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .brand(product.getBrand())
                .description(product.getDescription())
                .build();
    }

    /**
     * 제품 목록 조회
     */
    public List<ProductListResponse> getList(ProductPageRequest productPageRequest) {
        return productRepository.getList(productPageRequest).stream()
                .map(ProductListResponse::of)
                .toList();
    }

    /**
     * 제품 정보 수정
     * 트랜잭션 적용하여 원자성 보장, 예외 발생 시 롤백 보장
     *
     * @throws {@link ProductNotFoundException} 제품 존재하지 않을 경우
     */
    @Transactional
    public void edit(Long id, ProductEditRequest productEditRequest) {
        Product product = productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);

        ProductEditorBuilder editorBuilder = product.toEditor();

        ProductEditor productEditor = editorBuilder
                .name(productEditRequest.getName())
                .brand(productEditRequest.getBrand())
                .description(productEditRequest.getDescription())
                .build();

        product.edit(productEditor);
    }

    /**
     * 제품 삭제
     * 단일 제품 한정
     *
     * @throws {@link ProductNotFoundException} 제품 존재하지 않을 경우
     */
    public void delete(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);

        productRepository.delete(product);
    }
}
