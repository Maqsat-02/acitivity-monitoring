package kz.iitu.edu.activity.monitoring.service;

import kz.iitu.edu.activity.monitoring.dto.common.response.ErrorResponseDto;
import kz.iitu.edu.activity.monitoring.dto.common.response.UserDto;
import kz.iitu.edu.activity.monitoring.entity.FirebaseUser;
import kz.iitu.edu.activity.monitoring.enums.Role;
import kz.iitu.edu.activity.monitoring.exception.ApiException;
import kz.iitu.edu.activity.monitoring.mapper.UserMapper;
import kz.iitu.edu.activity.monitoring.repository.FirebaseUserRepository;
import kz.iitu.edu.activity.monitoring.repository.ProjectRepository;
import kz.iitu.edu.activity.monitoring.repository.ReviewRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class UserService {
    private final FirebaseUserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final ProjectRepository projectRepository;

    public List<UserDto> getChiefEditorsNotAssignedAsMainToAnyProject(Pageable pageable) {
        List<FirebaseUser> chiefEditors = userRepository.findAllChiefEditors();
        return chiefEditors.stream()
                .filter(chiefEditor -> !isChiefEditorAssignedAsMainToAnyProject(chiefEditor.getId()))
                .map(UserMapper.INSTANCE::entityToDto)
                .skip(pageable.getOffset())
                .limit(pageable.getPageSize())
                .toList();
    }

    FirebaseUser getTranslatorByIdOrThrow(String id) {
        return getByIdOrThrow(id, Role.TRANSLATOR.name());
    }

    FirebaseUser getManagerByIdOrThrow(String id) {
        return getByIdOrThrow(id, Role.PROJECT_MANAGER.name());
    }

    FirebaseUser getChiefEditorByIdOrThrow(String id) {
        return getByIdOrThrow(id, Role.CHIEF_EDITOR.name());
    }

    boolean isChiefEditorAssignedAsMainToAnyProject(String chiefEditorId) {
        return projectRepository.projectExistsWithChiefEditorId(chiefEditorId);
    }

    boolean isChiefEditorBusyInProject(String chiefEditorId, Long projectId) {
        long countOfAssignedReviews = reviewRepository.countWithStatusTodoOrInProgressByChiefEditorAndProject(
                chiefEditorId, projectId
        );
        return countOfAssignedReviews > 0;
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
