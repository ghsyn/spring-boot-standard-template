package com.olpang.exception;

/**
 * 접근 대상 제품 존재하지 않을 시 발생
 */
public class ProductNotFoundException extends CommonException {

    private static final String MESSAGE = "존재하지 않는 제품입니다.";

    public ProductNotFoundException() {
        super(MESSAGE);
    }

    /**
     * @return HTTP 404 상태 코드
     */
    @Override
    public int getStatusCode() {
        return 404;
    }
}
