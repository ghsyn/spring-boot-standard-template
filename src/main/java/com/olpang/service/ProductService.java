package com.olpang.service;

import com.olpang.domain.Product;
import com.olpang.domain.ProductEditor;
import com.olpang.exception.ProductNotFoundException;
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
                .orElseThrow(ProductNotFoundException::new);

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
                .orElseThrow(ProductNotFoundException::new);

        ProductEditor.ProductEditorBuilder editorBuilder = product.toEditor();

        ProductEditor productEditor = editorBuilder
                .name(productEditRequest.getName())
                .brand(productEditRequest.getBrand())
                .description(productEditRequest.getDescription())
                .build();

        product.edit(productEditor);
    }

    public void delete(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);

        productRepository.delete(product);
    }
}
