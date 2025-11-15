package com.olpang.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.olpang.domain.Product;
import com.olpang.repository.ProductRepository;
import com.olpang.request.ProductCreateRequest;
import com.olpang.request.ProductEditRequest;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc     // mockMvc 사용
@SpringBootTest     // 통합 테스트
@Transactional
class ProductControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void clean() {
        productRepository.deleteAll();
        entityManager.flush();
        // H2 시퀀스 초기화 (auto_increment 재시작)
        entityManager.createNativeQuery("ALTER TABLE product ALTER COLUMN id RESTART WITH 1").executeUpdate();
    }

    @Test
    @DisplayName("제품 등록")
    void postTest() throws Exception {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .name("제품명")
                .brand("제조사")
                .description("제품에 대한 설명입니다.")
                .build();

        String jsonRequest = objectMapper.writeValueAsString(request);

        // when
        mockMvc.perform(post("/api/v1/products")
                        .contentType(APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andDo(print());

        // then
        assertEquals(1L, productRepository.count());

        Product product = productRepository.findAll().get(0);
        assertEquals("제품명", product.getName());
        assertEquals("제조사", product.getBrand());
        assertEquals("제품에 대한 설명입니다.", product.getDescription());
    }

    @Test
    @DisplayName("[실패케이스] 제품 등록 시 필드 누락")
    void validationFailedHandlerTest() throws Exception {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
                // 제품명 누락
                .brand("제조사")
                .description("제품에 대한 설명입니다.")
                .build();

        String jsonRequest = objectMapper.writeValueAsString(request);

        // expected
        mockMvc.perform(post("/api/v1/products")
                        .contentType(APPLICATION_JSON)
                        .content(jsonRequest)
                )
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("[실패케이스] 제품 등록 시 유효하지 않은 정보 포함")
    void commonHandlerTest() throws Exception {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .name("제품명")
                .brand("제조사")
                .description("<script>alert('xss')</script>")
                .build();

        String jsonRequest = objectMapper.writeValueAsString(request);

        // expected
        mockMvc.perform(post("/api/v1/products")
                        .contentType(APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("제품 단건 조회")
    void getDetailsTest() throws Exception {
        // given
        Product product = Product.builder()
                .name("foo")
                .brand("bar")
                .description("baz")
                .build();

        productRepository.save(product);

        // expected (when + then)
        mockMvc.perform(get("/api/v1/products/{productId}", product.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.name").value("foo"))
                .andExpect(jsonPath("$.brand").value("bar"))
                .andExpect(jsonPath("$.description").value("baz"))
                .andDo(print());
    }

    @Test
    @DisplayName("[실패케이스] 존재하지 않는 제품 단건 조회")
    void getDetailsProductNotFoundTest() throws Exception {
        // expected
        mockMvc.perform(get("/api/v1/products/{productId}", 1L))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("제품 목록 조회 - 문자열 길이 요약 검증")
    void getListFieldLengthTest() throws Exception {

        // given
        Product longProduct1 = Product.builder()
                .name("매우 길게 만든 제품명1, 10자 이상 문자열 길이 테스트용")    // 10자 이상
                .brand("제조사")
                .description("아주아주아주 길게 만든 제품 설명1, 50자 이상 데이터 부분 문자열로 가져오는지 테스트용입니다.")   // 50자 이상
                .build();

        productRepository.save(longProduct1);

        Product longProduct2 = Product.builder()
                .name("매우 길게 만든 제품명2, 10자 이상 문자열 길이 테스트용")    // 10자 이상
                .brand("제조사")
                .description("아주아주아주 길게 만든 제품 설명2, 50자 이상 데이터 부분 문자열로 가져오는지 테스트용입니다.")   // 50자 이상
                .build();

        productRepository.save(longProduct2);

        // expected
        mockMvc.perform(get("/api/v1/products?page=1&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$[1].id").value(longProduct1.getId()))
                .andExpect(jsonPath("$[0].id").value(longProduct2.getId()))
                .andExpect(jsonPath("$[1].name").value("매우 길게 만든 제"))
                .andExpect(jsonPath("$[1].brand").value("제조사"))
                .andExpect(jsonPath("$[1].description").value("아주아주아주 길게 만든 제품 설명1, 50자 이상 데이터 부분 문자열로 가져오는지 테스트용"))
                .andDo(print());
    }

    @Test
    @DisplayName("제품 목록 조회 - 제품 1페이지 조회 및 ID 내림차순 정렬 검증")
    void getListPaginationTest() throws Exception {
        // given
        List<Product> requestProduct = IntStream.range(1, 31)
                .mapToObj(i -> Product.builder()
                        .name("제품명 " + i)
                        .brand("제조사 " + i)
                        .description("제품 설명 " + i)
                        .build())
                .toList();
        productRepository.saveAll(requestProduct);

        // expected
        mockMvc.perform(get("/api/v1/products?page=1&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(10)))
                .andExpect(jsonPath("$[0].id").value(30))
                .andExpect(jsonPath("$[0].name").value("제품명 30"))
                .andExpect(jsonPath("$[0].brand").value("제조사 30"))
                .andExpect(jsonPath("$[0].description").value("제품 설명 30"))
                .andDo(print());
    }

    @Test
    @DisplayName("제품 목록 조회 - 0번째 페이지 요청 시 첫 페이지 조회 검증")
    void getPage0Test() throws Exception {
        // given
        List<Product> requestProduct = IntStream.range(1, 31)
                .mapToObj(i -> Product.builder()
                        .name("제품명 " + i)
                        .brand("제조사 " + i)
                        .description("제품 설명 " + i)
                        .build())
                .toList();
        productRepository.saveAll(requestProduct);

        // expected
        mockMvc.perform(get("/api/v1/products?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(10)))
                .andExpect(jsonPath("$[0].id").value(30))
                .andExpect(jsonPath("$[0].name").value("제품명 30"))
                .andExpect(jsonPath("$[0].brand").value("제조사 30"))
                .andExpect(jsonPath("$[0].description").value("제품 설명 30"))
                .andDo(print());
    }

    @Test
    @DisplayName("제품 정보 수정")
    void editTest() throws Exception {
        // given
        Product product = Product.builder()
                .name("foo")
                .brand("bar")
                .description("baz")
                .build();

        productRepository.save(product);

        ProductEditRequest request = ProductEditRequest.builder()
                .name("new foo")
                .brand("bar")
                .description("baz")
                .build();

        // expected
        mockMvc.perform(patch("/api/v1/products/{productId}", product.getId())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("[실패케이스] 존재하지 않는 제품 정보 수정")
    void editProductNotFoundTest() throws Exception {
        // given
        ProductEditRequest request = ProductEditRequest.builder()
                .name("new foo")
                .brand("bar")
                .description("baz")
                .build();

        // expected
        mockMvc.perform(patch("/api/v1/products/{productId}", 1L)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("제품 삭제")
    void deleteTest() throws Exception {
        // given
        Product product = Product.builder()
                .name("foo")
                .brand("bar")
                .description("baz")
                .build();

        productRepository.save(product);

        // expected
        mockMvc.perform(delete("/api/v1/products/{productId}", product.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("[실패케이스] 존재하지 않는 제품 삭제")
    void deleteProductNotFoundTest() throws Exception {
        // expected
        mockMvc.perform(delete("/api/v1/products/{productId}", 1L)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
}