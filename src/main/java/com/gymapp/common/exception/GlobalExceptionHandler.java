package com.gymapp.common.exception;

import com.gymapp.common.constants.ErrorMessages;
import com.gymapp.common.dto.ApiResponse;
import com.gymapp.common.dto.ErrorResponseDto;
import com.gymapp.common.error.ErrorCode;
import java.util.stream.Collectors;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<ErrorResponseDto>> handleResourceNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.fail(ex.getMessage(), new ErrorResponseDto(ErrorCode.RESOURCE_NOT_FOUND, ex.getMessage())));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<ErrorResponseDto>> handleBadRequest(BadRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.fail(ex.getMessage(), new ErrorResponseDto(ErrorCode.BAD_REQUEST, ex.getMessage())));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<ErrorResponseDto>> handleValidationException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.fail(message, new ErrorResponseDto(ErrorCode.VALIDATION_FAILED, message)));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<ErrorResponseDto>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.fail(
                        ErrorMessages.DB_CONSTRAINT_VIOLATION,
                        new ErrorResponseDto(ErrorCode.DB_CONSTRAINT_VIOLATION, ErrorMessages.DB_CONSTRAINT_VIOLATION)));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<ErrorResponseDto>> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.fail(
                        ErrorMessages.ACCESS_DENIED,
                        new ErrorResponseDto(ErrorCode.ACCESS_DENIED, ErrorMessages.ACCESS_DENIED)));
    }

    @ExceptionHandler(PropertyReferenceException.class)
    public ResponseEntity<ApiResponse<ErrorResponseDto>> handleInvalidSortProperty(PropertyReferenceException ex) {
        String message = "Invalid sort field: " + ex.getPropertyName();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.fail(
                        message,
                        new ErrorResponseDto(ErrorCode.BAD_REQUEST, message)));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<ErrorResponseDto>> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.fail(
                        ErrorMessages.SOMETHING_WENT_WRONG,
                        new ErrorResponseDto(ErrorCode.INTERNAL_ERROR, ErrorMessages.SOMETHING_WENT_WRONG)));
    }
}


