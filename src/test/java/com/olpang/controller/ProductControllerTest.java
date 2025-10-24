package com.olpang.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.olpang.domain.Product;
import com.olpang.repository.ProductRepository;
import com.olpang.request.ProductCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@AutoConfigureMockMvc   // mockMvc 사용하기 위해 @WebMvcTest에서 가져옴
@SpringBootTest // service, repository 사용 -> 통합 테스트로 변경
class ProductControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void clean() {
        productRepository.deleteAll();
    }

    @Test
    @DisplayName("요청 값을 JSON에 담아 POST /api/v1/products 요청 시 DB에 값이 저장된다.")
    // mockMvc 테스트 최소화하기, SpringBootTest로 repository(db)에 저장되는지 (= 실제로 save 작업이 잘 이루어졌는지) 확인하는 것이 중요
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
    @DisplayName("[실패케이스] POST /api/v1/products JSON 요청 시 name 값은 필수이다.")
    void invalidRequestTest() throws Exception {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
                // 제품명 요청 값 누락
//                .name("제품명")
                .brand("제조사")
                .description("제품에 대한 설명입니다.")
                .build();

        String jsonRequest = objectMapper.writeValueAsString(request);

        // expected
        mockMvc.perform(post("/api/v1/products")
                        .contentType(APPLICATION_JSON)
                        .content(jsonRequest)
                )
                .andExpect(status().isBadRequest()) // 400
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.name").value("제품명을 입력해주세요."))
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
    @DisplayName("GET /api/v1/products 요청 시 '제품 목록 조회' 문자열을 출력한다.")
    void getListTest() throws Exception {
        // expected
        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(content().string("제품 목록 조회"))
                .andDo(print());
    }
}