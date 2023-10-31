package kz.iitu.edu.activity.monitoring.service;

import kz.iitu.edu.activity.monitoring.dto.common.response.ErrorResponseDto;
import kz.iitu.edu.activity.monitoring.entity.FirebaseUser;
import kz.iitu.edu.activity.monitoring.enums.Role;
import kz.iitu.edu.activity.monitoring.exception.ApiException;
import kz.iitu.edu.activity.monitoring.repository.FirebaseUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@AllArgsConstructor
public class UserService {
    private final FirebaseUserRepository userRepository;

    FirebaseUser getTranslatorByIdOrThrow(String id) {
        return getByIdOrThrow(id, Role.TRANSLATOR.name());
    }

    FirebaseUser getManagerByIdOrThrow(String id) {
        return getByIdOrThrow(id, Role.PROJECT_MANAGER.name());
    }

    FirebaseUser getChiefEditorByIdOrThrow(String id) {
        return getByIdOrThrow(id, Role.CHIEF_EDITOR.name());
    }

    private FirebaseUser getByIdOrThrow(String id, String role) {
        FirebaseUser user = userRepository.findById(id)
                .orElseThrow(() -> createApiException(id, role));
        if (!Objects.equals(user.getRole(), role)) {
            throw createApiException(id, role);
        }
        return user;
    }

    private ApiException createApiException(String id, String role) {
        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .status(404)
                .message(role + " with ID " + id + " does not exist")
                .build();
        return new ApiException(errorResponseDto);
    }
}
