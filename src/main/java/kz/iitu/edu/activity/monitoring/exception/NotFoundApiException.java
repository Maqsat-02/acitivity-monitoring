package kz.iitu.edu.activity.monitoring.exception;

import org.springframework.http.HttpStatus;

class NotFoundApiException extends ApiException {
    NotFoundApiException(String message) {
        super(HttpStatus.NOT_FOUND.value(), message);
    }
}
