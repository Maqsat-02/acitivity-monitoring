package kz.iitu.edu.activity.monitoring.exception;

import org.springframework.http.HttpStatus;

class BadRequestApiException extends ApiException {
    BadRequestApiException(String message) {
        super(HttpStatus.BAD_REQUEST.value(), message);
    }
}
