package com.olpang.service;

import com.olpang.domain.Product;
import com.olpang.domain.ProductEditor;
import com.olpang.repository.ProductRepository;
import com.olpang.request.ProductCreateRequest;
import com.olpang.request.ProductEditRequest;
import com.olpang.request.ProductPageRequest;
import com.olpang.response.ProductDetailsResponse;
import com.olpang.response.ProductListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor    // 생성자 injection
public class ProductService {

    private final ProductRepository productRepository;

    public void register(ProductCreateRequest request) {
        Product product = Product.builder()
                .name(request.getName())
                .brand(request.getBrand())
                .description(request.getDescription())
                .build();

        productRepository.save(product);
    }

    public ProductDetailsResponse getDetails(Long id) {
        Product product = productRepository.findById(id)
                // TODO: ID 값 없다면 제품 등록 전이라는 적절한 exception 만들기
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 제품입니다."));

        return ProductDetailsResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .brand(product.getBrand())
                .description(product.getDescription())
                .build();
    }

    public List<ProductListResponse> getList(ProductPageRequest productPageRequest) {
        return productRepository.getList(productPageRequest).stream()
                .map(ProductListResponse::of)
                .toList();
    }

    @Transactional
    public void edit(Long id, ProductEditRequest productEditRequest) {
        Product product = productRepository.findById(id)
                // TODO: ID 값 없다면 제품 등록 전이라는 적절한 exception 만들기
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 제품입니다."));

        ProductEditor.ProductEditorBuilder editorBuilder = product.toEditor();

        // 수정할 필드에 대해서만 builder 업데이트
        if (productEditRequest.getName() != null) {
            editorBuilder.name(productEditRequest.getName());
        }
        if (productEditRequest.getBrand() != null) {
            editorBuilder.brand(productEditRequest.getBrand());
        }
        if (productEditRequest.getDescription() != null) {
            editorBuilder.description(productEditRequest.getDescription());
        }

        product.edit(editorBuilder.build());
    }
}
