package com.olpang.service;

import com.olpang.domain.Product;
import com.olpang.repository.ProductRepository;
import com.olpang.request.ProductCreateRequest;
import com.olpang.response.ProductDetailsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
                // .orElse( ... ) TODO: ID 값 없다면 제품 등록 전이라는 적절한 exception 만들기
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 제품입니다."));

        // "WebProductService: 호출 담당 서비스 클래스" / "ProductService: 외부 통신 및 데이터 처리 담당 서비스 클래스"로 나누어 확장 가능
        return ProductDetailsResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .brand(product.getBrand())
                .description(product.getDescription())
                .build();
    }
}
