package com.olpang.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.olpang.domain.Product;
import com.olpang.repository.ProductRepository;
import com.olpang.request.ProductCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.olpang.com", uriPort = 443)
@ExtendWith(RestDocumentationExtension.class)
public class ProductControllerDocTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("제품 단건 조회")
    void getDetailsDocTest() throws Exception {
        // given
        Product product = Product.builder()
                .name("아토베리어365 크림")
                .brand("에스트라")
                .description("[11월 올영픽] 에스트라 아토베리어365 크림 80ml 더블 기획(+에센스 25ML + 세라-히알 앰플 7ML)")
                .build();

        productRepository.save(product);

        // expected
        mockMvc.perform(get("/api/v1/products/{productId}", 1L)
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("product-inquiry",
                        pathParameters(
                            parameterWithName("productId").description("제품 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").description("제품 ID"),
                                fieldWithPath("name").description("제품명"),
                                fieldWithPath("brand").description("제조사"),
                                fieldWithPath("description").description("제품 설명")
                        )
                ));
    }

    @Test
    @DisplayName("제품 등록")
    void postDocTest() throws Exception {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .name("아토베리어365 크림")
                .brand("에스트라")
                .description("[11월 올영픽] 에스트라 아토베리어365 크림 80ml 더블 기획(+에센스 25ML + 세라-히알 앰플 7ML)")
                .build();

        String jsonRequest = objectMapper.writeValueAsString(request);

        // expected
        mockMvc.perform(post("/api/v1/products")
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(jsonRequest))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("product-create",
                        requestFields(
                                PayloadDocumentation.fieldWithPath("name").description("제품명")
                                        .attributes(key("constraints").value("정확한 제품명을 입력해주세요.")),
                                PayloadDocumentation.fieldWithPath("brand").description("제조사"),
                                PayloadDocumentation.fieldWithPath("description").description("제품 설명").optional()
                        )
                ));
    }
}
