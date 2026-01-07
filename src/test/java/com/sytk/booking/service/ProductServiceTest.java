package com.sytk.booking.service;

import com.sytk.booking.domain.Product;
import com.sytk.booking.exception.ProductNotFoundException;
import com.sytk.booking.repository.ProductRepository;
import com.sytk.booking.request.ProductCreateRequest;
import com.sytk.booking.request.ProductEditRequest;
import com.sytk.booking.request.ProductPageRequest;
import com.sytk.booking.response.ProductDetailsResponse;
import com.sytk.booking.response.ProductListResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

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
        Product product = Product.builder()
                .name("foo")
                .brand("bar")
                .description("baz")
                .build();

        productRepository.save(product);

        // when
        ProductDetailsResponse response = productService.getDetails(product.getId());

        // then
        assertNotNull(response);
        assertEquals(1L, productRepository.count());
        assertEquals("foo", response.getName());
        assertEquals("bar", response.getBrand());
        assertEquals("baz", response.getDescription());
    }

    @Test
    @DisplayName("[실패케이스] 존재하지 않는 제품 단건 조회")
    void getDetailsProductNotFoundTest() {
        // given
        Product product = Product.builder()
                .name("foo")
                .brand("bar")
                .description("baz")
                .build();

        productRepository.save(product);

        // expected
        assertThrows(ProductNotFoundException.class, () -> {
            productService.getDetails(product.getId() + 1L);
        });
    }

    @Test
    @DisplayName("제품 목록 조회")
    void getListTest() {
        // given
        List<Product> products = IntStream.range(1, 31)
                .mapToObj(i -> Product.builder()
                        .name("제품명 " + i)
                        .brand("제조사 " + i)
                        .description("제품 설명 " + i)
                        .build())
                .toList();

        productRepository.saveAll(products);

        ProductPageRequest productPageRequest = ProductPageRequest.builder().build();

        // when
        List<ProductListResponse> productResponse = productService.getList(productPageRequest);

        // then
        assertEquals(10L, productResponse.size());
        assertEquals("제품명 30", productResponse.get(0).getName());
        assertEquals("제품명 21", productResponse.get(9).getName());
    }

    @Test
    @DisplayName("제품 수정 - name")
    void editNameTest() {
        // given
        Product product = Product.builder()
                .name("foo")
                .brand("bar")
                .description("baz")
                .build();

        productRepository.save(product);

        ProductEditRequest productEditRequest = ProductEditRequest.builder()
                .name("new foo")
                .brand("bar")
                .description("baz")
                .build();

        // when
        productService.edit(product.getId(), productEditRequest);

        // then
        Product changedProduct = productRepository.findById(product.getId())
                .orElseThrow(() -> new RuntimeException("제품이 존재하지 않습니다. id = " + product.getId()));
        assertEquals("new foo", changedProduct.getName());
        assertEquals("bar", changedProduct.getBrand());
        assertEquals("baz", changedProduct.getDescription());
    }

    @Test
    @DisplayName("제품 수정 - brand, description")
    void editBrandAndDescriptionTest() {
        // given
        Product product = Product.builder()
                .name("foo")
                .brand("bar")
                .description("baz")
                .build();

        productRepository.save(product);

        ProductEditRequest productEditRequest = ProductEditRequest.builder()
                .name("foo")
                .brand("new bar")
                .description("new baz")
                .build();

        // when
        productService.edit(product.getId(), productEditRequest);

        // then
        Product changedProduct = productRepository.findById(product.getId())
                .orElseThrow(() -> new RuntimeException("제품이 존재하지 않습니다. id = " + product.getId()));
        assertEquals("foo", changedProduct.getName());
        assertEquals("new bar", changedProduct.getBrand());
        assertEquals("new baz", changedProduct.getDescription());
    }

    @Test
    @DisplayName("[실패케이스] 존재하지 않는 제품 수정")
    void editProductNotFoundTest() {
        // given
        Product product = Product.builder()
                .name("foo")
                .brand("bar")
                .description("baz")
                .build();

        productRepository.save(product);

        ProductEditRequest productEditRequest = ProductEditRequest.builder()
                .name("new foo")
                .brand("bar")
                .description("baz")
                .build();

        // expected
        assertThrows(ProductNotFoundException.class, () -> {
            productService.edit(product.getId() + 1L, productEditRequest);
        });
    }

    @Test
    @DisplayName("제품 삭제")
    void deleteTest() {
        // given
        Product product = Product.builder()
                .name("foo")
                .brand("bar")
                .description("baz")
                .build();

        productRepository.save(product);

        // when
        productService.delete(product.getId());

        //then
        assertEquals(0, productRepository.count());
    }

    @Test
    @DisplayName("[실패케이스] 존재하지 않는 제품 삭제")
    void deleteProductNotFoundTest() {
        // given
        Product product = Product.builder()
                .name("foo")
                .brand("bar")
                .description("baz")
                .build();

        productRepository.save(product);

        // expected
        assertThrows(ProductNotFoundException.class, () -> {
            productService.delete(product.getId() + 1L);
        });
    }
}