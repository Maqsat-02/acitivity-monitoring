package kz.iitu.edu.activity.monitoring.exception;

import org.springframework.http.HttpStatus;

class ForbiddenApiException extends ApiException {
    ForbiddenApiException(String message) {
        super(HttpStatus.FORBIDDEN.value(), message);
    }
}
