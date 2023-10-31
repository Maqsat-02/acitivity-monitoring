package kz.iitu.edu.activity.monitoring.aop;

import jakarta.servlet.http.HttpServletRequest;
import kz.iitu.edu.activity.monitoring.dto.common.response.ErrorResponseDto;
import kz.iitu.edu.activity.monitoring.exception.ApiException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponseDto> handleAnthillErrorException(ApiException ex, HttpServletRequest request) {
        ErrorResponseDto errorResponseDto = ex.getErrorResponseDto();
        return ResponseEntity.status(errorResponseDto.getStatus()).body(errorResponseDto);
    }
}
