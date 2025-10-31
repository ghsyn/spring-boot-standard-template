package com.olpang.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.olpang.domain.Product;
import com.olpang.repository.ProductRepository;
import com.olpang.request.ProductCreateRequest;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc   // mockMvc 사용하기 위해 @WebMvcTest에서 가져옴
@SpringBootTest // service, repository 사용 -> 통합 테스트로 변경
@Transactional
class ProductControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private EntityManager entityManager; // 시퀀스 초기화용

    @BeforeEach
    void clean() {
        productRepository.deleteAll();
        entityManager.flush();
        // H2 시퀀스 초기화 (auto_increment 재시작)
        entityManager.createNativeQuery("ALTER TABLE product ALTER COLUMN id RESTART WITH 1").executeUpdate();
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
    @DisplayName("제품 목록 조회 - 조회한 목록과 제품명/제품설명 문자열의 길이 제한을 검증한다.")
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
    @DisplayName("제품 목록 조회 - 제품 1페이지 조회 및 id 내림차순 정렬")
    void getListWithPaginationTest() throws Exception {
        // given
        List<Product> requestProduct = IntStream.range(1, 31)
                .mapToObj(i -> Product.builder()
                        .name("제품명 " + i)
                        .brand("제조사 " + i)
                        .description("제품 설명 " + i)
                        .build())
                .toList();
        productRepository.saveAll(requestProduct);

        // when & then
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
    @DisplayName("제품 목록 조회 - 페이지를 0으로 요청하면 첫 페이지를 가져온다.")
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

        // when & then
        mockMvc.perform(get("/api/v1/products?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(10)))
                .andExpect(jsonPath("$[0].id").value(30))
                .andExpect(jsonPath("$[0].name").value("제품명 30"))
                .andExpect(jsonPath("$[0].brand").value("제조사 30"))
                .andExpect(jsonPath("$[0].description").value("제품 설명 30"))
                .andDo(print());
    }

}