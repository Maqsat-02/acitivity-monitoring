package kz.iitu.edu.activity.monitoring.exception;

import kz.iitu.edu.activity.monitoring.dto.common.response.ErrorResponseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ApiException extends RuntimeException {
    private final ErrorResponseDto errorResponseDto;

    public ApiException(int status, String message) {
        this.errorResponseDto = new ErrorResponseDto(status, message);
    }

    public ApiException(ErrorResponseDto errorResponseDto) {
        super(errorResponseDto.getMessage());
        this.errorResponseDto = errorResponseDto;
    }

}
