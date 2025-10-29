package com.olpang.service;

import com.olpang.domain.Product;
import com.olpang.repository.ProductRepository;
import com.olpang.request.ProductCreateRequest;
import com.olpang.response.ProductDetailsResponse;
import com.olpang.response.ProductListResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.data.domain.Sort.Direction.DESC;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void clean() {
        productRepository.deleteAll();
    }

    @Test
    @DisplayName("제품 등록")
    void registerTest() {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .name("제품명")
                .brand("제조사")
                .description("제품에 대한 설명입니다.")
                .build();

        productService.register(request);

        // when
        assertEquals(1L, productRepository.count());

        // then
        Product product = productRepository.findAll().get(0);
        assertEquals("제품명", product.getName());
        assertEquals("제조사", product.getBrand());
        assertEquals("제품에 대한 설명입니다.", product.getDescription());
    }

    @Test
    @DisplayName("제품 단건 조회")
    void getDetailsTest() {
        // given
        Product requstProduct = Product.builder()
                .name("foo")
                .brand("bar")
                .description("baz")
                .build();

        productRepository.save(requstProduct);

        // when
        ProductDetailsResponse response = productService.getDetails(requstProduct.getId());

        // then
        assertNotNull(response);
        assertEquals(1L, productRepository.count());
        assertEquals("foo", response.getName());
        assertEquals("bar", response.getBrand());
        assertEquals("baz", response.getDescription());
    }

    @Test
    @DisplayName("제품 목록 1페이지 조회")
    void getListTest() {
        // given
        List<Product> requestProduct = IntStream.range(1, 31)
                .mapToObj(i -> Product.builder()
                        .name("제품명 " + i)
                        .brand("제조사 " + i)
                        .description("제품 설명 " + i)
                        .build())
                .toList();

        productRepository.saveAll(requestProduct);

        Pageable pageable = PageRequest.of(0, 5, DESC, "id");

        // when
        List<ProductListResponse> products = productService.getList(pageable);

        // then
        assertEquals(5L, products.size());
        assertEquals("제품명 30", products.get(0).getName());
        assertEquals("제품명 26", products.get(4).getName());
    }
}