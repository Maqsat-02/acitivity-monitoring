package kz.iitu.edu.activity.monitoring.service;

import kz.iitu.edu.activity.monitoring.dto.common.response.ErrorResponseDto;
import kz.iitu.edu.activity.monitoring.entity.Project;
import kz.iitu.edu.activity.monitoring.entity.XChiefEditor;
import kz.iitu.edu.activity.monitoring.exception.ApiException;
import kz.iitu.edu.activity.monitoring.repository.ExtraChiefEditorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExtraChiefEditorService {
    private final ExtraChiefEditorRepository extraChiefEditorRepository;

    XChiefEditor getByChiefEditorIdOrThrow(String chiefEditorId, Project project) {
        return extraChiefEditorRepository.findByChiefEditorIdAndProject(chiefEditorId, project).orElseThrow(() -> {
            ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                    .status(404)
                    .message("ExtraChiefEditor with ID " + chiefEditorId + " does not exist")
                    .build();
            throw new ApiException(errorResponseDto);
        });
    }
}
