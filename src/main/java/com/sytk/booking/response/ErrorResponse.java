package com.sytk.booking.response;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

/**
 * API 에러 응답 DTO
 *
 * 예시:
 * {
 *   "code": "400",
 *   "message": "잘못된 요청입니다.",
 *   "validation": {
 *     "name": "제품명을 입력해주세요."
 *   }
 * }
 */
@Getter
public class ErrorResponse {

    private final String code;
    private final String message;
    private final Map<String, String> validation;

    @Builder
    public ErrorResponse(String code, String message, Map<String, String> validation) {
        this.code = code;
        this.message = message;
        this.validation = validation;
    }

    /**
     * 유효성 검증(@Valid) 실패 필드 정보 추가
     *
     * @param fieldName 검증 실패 필드명
     * @param message 검증 실패 메시지
     */
    public void addValidation(String fieldName, String message) {
        this.validation.put(fieldName, message);
    }
}
