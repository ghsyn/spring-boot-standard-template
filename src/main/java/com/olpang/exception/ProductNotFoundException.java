package com.olpang.exception;

public class ProductNotFoundException extends RuntimeException {

    private static final String MESSAGE = "존재하지 않는 제품입니다.";

    public ProductNotFoundException() {
        super(MESSAGE);
    }
}
