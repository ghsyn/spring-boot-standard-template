package com.olpang.advice;

import com.olpang.exception.CommonException;
import com.olpang.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 공통 예외 처리 advice 클래스.
 *
 * 컨트롤러 계층에서 발생하는 예외 처리를 전역적으로 처리,
 * 다양한 예외에 대한 표준 에러 응답을 생성
 */
@ControllerAdvice
public class CommonControllerAdvice {

    /**
     * {@link MethodArgumentNotValidException} 예외 처리 핸들러.
     *
     * 유효성 검증(@Valid) 실패 시 발생하는 예외를 처리하여
     * HTTP 400 응답과 함께 표준화된 에러 응답 객체를 반환
     *
     * @param e 발생한 MethodArgumentNotValidException 예외
     * @return ErrorResponse 객체
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ErrorResponse invalidRequestHandler(MethodArgumentNotValidException e) {

        ErrorResponse response = ErrorResponse.builder()
                .code("400")
                .message("잘못된 요청입니다.")
                .build();

        for (FieldError fieldError : e.getFieldErrors()) {
            response.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return response;
    }

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
