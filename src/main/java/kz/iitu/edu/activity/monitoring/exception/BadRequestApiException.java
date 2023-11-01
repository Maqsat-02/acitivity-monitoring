package kz.iitu.edu.activity.monitoring.exception;

import org.springframework.http.HttpStatus;

public class BadRequestApiException extends ApiException {
    public BadRequestApiException(String message) {
        super(HttpStatus.BAD_REQUEST.value(), message);
    }
}
