package com.sytk.booking.exception;

/**
 * 잘못된 형식, 내용 포함 요청 시 발생
 */
public class InvalidRequestException extends CommonException {

    private static final String MESSAGE = "잘못된 요청입니다.";

    /**
     * 특정 필드 유효성 검증 실패 시 사용
     */
    public InvalidRequestException(String fieldName, String message) {
        super(MESSAGE);
        addValidation(fieldName, message);
    }

    /**
     * @return HTTP 400 상태 코드
     */
    @Override
    public int getStatusCode() {
        return 400;
    }
}