package com.olpang.response;

import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * API 에러 응답 DTO 클래스.
 *
 * 요청 처리 중 발생한 예외를 클라이언트에 일관된 형태로 전달하기 위한 응답 객체
 *
 * 구조 예시)
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
     * 유효성 검증 오류 필드명 및 메시지를 validation 맵에 추가
     *
     * @param fieldError 유효성 검증에 실패한 필드명
     * @param errorMessage 해당 필드의 error message
     */
    public void addValidation(String fieldError, String errorMessage) {
        this.validation.put(fieldError, errorMessage);
    }
}
