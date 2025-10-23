package com.olpang.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.olpang.request.ProductCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest
class ProductControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("요청 값을 JSON에 담아 POST /api/v1/products 요청 시 '제품 등록' 문자열을 출력한다.")
    void postTest() throws Exception {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .name("제품명")
                .brand("제조사")
                .description("제품에 대한 설명입니다.")
                .build();

        String jsonRequest = objectMapper.writeValueAsString(request);

        // expected
        mockMvc.perform(post("/api/v1/products")
                        .contentType(APPLICATION_JSON)  // mockMvc contentType 기본값: "text/html"
                        .content(jsonRequest)  // json object로 비교
                )
                .andExpect(status().isOk())
                .andExpect(content().string("제품 등록"))
                .andDo(print());
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
    @DisplayName("GET /api/v1/products 요청 시 '제품 목록 조회' 문자열을 출력한다.")
    void getListTest() throws Exception {
        // expected
        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(content().string("제품 목록 조회"))
                .andDo(print());
    }
}