package com.sytk.booking.advice;

import com.sytk.booking.exception.CommonException;
import com.sytk.booking.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 컨트롤러 전역 예외 처리
 */
@ControllerAdvice
public class CommonControllerAdvice {

    /**
     * 유효성 검증(@Valid) 실패 처리
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ErrorResponse validationFailedHandler(MethodArgumentNotValidException e) {

        ErrorResponse response = ErrorResponse.builder()
                .code("400")
                .message("잘못된 요청입니다.")
                .build();

        for (FieldError fieldError : e.getFieldErrors()) {
            response.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return response;
    }

    /**
     * 비즈니스 예외 처리
     */
    @ExceptionHandler(CommonException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> commonHandler(CommonException e) {
        int statusCode = e.getStatusCode();

        ErrorResponse responseBody = ErrorResponse.builder()
                .code(String.valueOf(statusCode))
                .message(e.getMessage())
                .validation(e.getValidation())
                .build();

        return ResponseEntity.status(statusCode)
                .body(responseBody);
    }
}
