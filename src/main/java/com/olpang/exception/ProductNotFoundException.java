package com.olpang.exception;

public class ProductNotFoundException extends CommonException {

    private static final String MESSAGE = "존재하지 않는 제품입니다.";

    public ProductNotFoundException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
