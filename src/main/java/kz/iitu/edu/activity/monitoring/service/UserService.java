package kz.iitu.edu.activity.monitoring.service;

import kz.iitu.edu.activity.monitoring.dto.common.response.UserDto;
import kz.iitu.edu.activity.monitoring.entity.FirebaseUser;
import kz.iitu.edu.activity.monitoring.enums.Role;
import kz.iitu.edu.activity.monitoring.exception.EntityNotFoundException;
import kz.iitu.edu.activity.monitoring.mapper.UserMapper;
import kz.iitu.edu.activity.monitoring.repository.FirebaseUserRepository;
import kz.iitu.edu.activity.monitoring.repository.ProjectRepository;
import kz.iitu.edu.activity.monitoring.repository.ReviewRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class UserService {
    private final FirebaseUserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final ProjectRepository projectRepository;

    public List<UserDto> getProjectManagers(Pageable pageable) {
        List<FirebaseUser> projectManagers = userRepository.findAllProjectManagers();
        return projectManagers.stream()
                .map(UserMapper.INSTANCE::entityToDto)
                .skip(pageable.getOffset())
                .limit(pageable.getPageSize())
                .toList();
    }

    public List<UserDto> getTranslators(Pageable pageable) {
        List<FirebaseUser> translators = userRepository.findAllTranslators();
        return translators.stream()
                .map(UserMapper.INSTANCE::entityToDto)
                .skip(pageable.getOffset())
                .limit(pageable.getPageSize())
                .toList();
    }

    public List<UserDto> getChiefEditorsNotAssignedAsMainToAnyProject(Pageable pageable) {
        List<FirebaseUser> chiefEditors = userRepository.findAllChiefEditors();
        return chiefEditors.stream()
                .filter(chiefEditor -> !projectRepository.projectExistsWithChiefEditorId(chiefEditor.getId()))
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

    boolean isChiefEditorBusyInProject(String chiefEditorId, Long projectId) {
        long countOfAssignedReviews = reviewRepository.countWithStatusTodoOrInProgressByChiefEditorAndProject(
                chiefEditorId, projectId
        );
        return countOfAssignedReviews > 0;
    }

    private FirebaseUser getByIdOrThrow(String id, String role) {
        FirebaseUser user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(role, id));
        if (!Objects.equals(user.getRole(), role)) {
            throw new EntityNotFoundException(role, id);
        }
        return user;
    }
}
