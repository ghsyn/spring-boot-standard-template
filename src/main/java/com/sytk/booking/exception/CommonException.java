package com.sytk.booking.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 비즈니스 예외들의 기반 클래스
 */
@Getter
public abstract class CommonException extends RuntimeException {

    /**
     * 필드 별 검증 오류 메시지
     */
    public final Map<String, String> validation = new HashMap<>();

    public CommonException(String message) {
        super(message);
    }

    /**
     * 예외에 대응하는 HTTP 상태 코드
     * @return HTTP status code
     */
    public abstract int getStatusCode();

    /**
     * 검증 오류 설명 추가
     * @param fieldName 오류 발생 필드명
     * @param message 검증 실패 메시지
     */
    public void addValidation(String fieldName, String message) {
        validation.put(fieldName, message);
    }
}
