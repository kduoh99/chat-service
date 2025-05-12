package com.study.chatserver.global.error;

import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.study.chatserver.global.error.dto.ErrorResponse;
import com.study.chatserver.global.error.exception.AccessDeniedGroupException;
import com.study.chatserver.global.error.exception.AuthGroupException;
import com.study.chatserver.global.error.exception.ConflictGroupException;
import com.study.chatserver.global.error.exception.InvalidGroupException;
import com.study.chatserver.global.error.exception.NotFoundGroupException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    // 잘못된 요청
    @ExceptionHandler({InvalidGroupException.class})
    public ResponseEntity<ErrorResponse> handleBadRequest(RuntimeException e) {
        return createErrorResponse(e, HttpStatus.BAD_REQUEST);
    }

    // 인증
    @ExceptionHandler({AuthGroupException.class})
    public ResponseEntity<ErrorResponse> handleUnauthorized(RuntimeException e) {
        return createErrorResponse(e, HttpStatus.UNAUTHORIZED);
    }

    // 접근 거부
    @ExceptionHandler({AccessDeniedGroupException.class})
    public ResponseEntity<ErrorResponse> handleForbidden(RuntimeException e) {
        return createErrorResponse(e, HttpStatus.FORBIDDEN);
    }

    // 데이터 없음
    @ExceptionHandler({NotFoundGroupException.class})
    public ResponseEntity<ErrorResponse> handleNotFound(RuntimeException e) {
        return createErrorResponse(e, HttpStatus.NOT_FOUND);
    }

    // 충돌
    @ExceptionHandler({ConflictGroupException.class})
    public ResponseEntity<ErrorResponse> handleConflict(RuntimeException e) {
        return createErrorResponse(e, HttpStatus.CONFLICT);
    }

    // 유효성 검사
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleValidData(final MethodArgumentNotValidException e) {
        FieldError fieldError = Objects.requireNonNull(e.getFieldError());
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
                String.format("%s. (%s)", fieldError.getDefaultMessage(), fieldError.getField()));
        log.warn("Validation error - [{}]: {}", fieldError.getField(), fieldError.getDefaultMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // 공통 에러 응답 생성
    private ResponseEntity<ErrorResponse> createErrorResponse(RuntimeException e, HttpStatus status) {
        ErrorResponse errorResponse = new ErrorResponse(status.value(), e.getMessage());
        log.error("Error [{}]: {}", status.value(), e.getMessage());

        return new ResponseEntity<>(errorResponse, status);
    }
}
