package kz.iitu.edu.activity.monitoring.exception;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class InvalidTargetDateException extends BadRequestApiException {
    public InvalidTargetDateException(LocalDate targetDate, LocalDate createdAtDate) {
        super("targetDate " + targetDate + " cannot be earlier or same as createdAt date " + createdAtDate);
    }
}
