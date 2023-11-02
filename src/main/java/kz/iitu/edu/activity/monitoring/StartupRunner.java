package kz.iitu.edu.activity.monitoring;

import kz.iitu.edu.activity.monitoring.dto.activity.request.ActivityCreationReq;
import kz.iitu.edu.activity.monitoring.dto.activity.response.ActivityDto;
import kz.iitu.edu.activity.monitoring.dto.project.request.ProjectCreationReq;
import kz.iitu.edu.activity.monitoring.dto.project.response.ProjectDto;
import kz.iitu.edu.activity.monitoring.entity.Activity;
import kz.iitu.edu.activity.monitoring.entity.FirebaseUser;
import kz.iitu.edu.activity.monitoring.entity.Project;
import kz.iitu.edu.activity.monitoring.entity.Review;
import kz.iitu.edu.activity.monitoring.enums.ReviewStatus;
import kz.iitu.edu.activity.monitoring.repository.ActivityRepository;
import kz.iitu.edu.activity.monitoring.repository.FirebaseUserRepository;
import kz.iitu.edu.activity.monitoring.repository.ProjectRepository;
import kz.iitu.edu.activity.monitoring.repository.ReviewRepository;
import kz.iitu.edu.activity.monitoring.service.ActivityService;
import kz.iitu.edu.activity.monitoring.service.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class StartupRunner implements CommandLineRunner {
    private static final String PROJECT_MANAGER_ID_MAKSAT = "AR3yg7gYTMTTm8VBLXIWrSgnssc2";
    private static final String TRANSLATOR_ID_ALDIYAR = "XH4Powot0ve0fiCfi2B7197wuTD2";
    private static final String CHIEF_EDITOR_ID_NURZHAN = "qpGIE2dwjfYSx9CFCfC2PnDnWZQ2";
    private static final String CHIEF_EDITOR_ID_JAKE = "toOmb6UYBRW9Yq8xF0gP6Jt6vr23";

    private final ProjectService projectService;
    private final ActivityService activityService;
    private final FirebaseUserRepository userRepository;

    private final ProjectRepository projectRepository;
    private final ActivityRepository activityRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public void run(String... args) throws Exception {
        ProjectCreationReq projectCreationReq1 = ProjectCreationReq.builder()
                .name("Name1")
                .description("Description1")
                .chiefEditorId(CHIEF_EDITOR_ID_NURZHAN)
                .build();
        ProjectDto projectDto1 = projectService.create(projectCreationReq1, PROJECT_MANAGER_ID_MAKSAT);

        ActivityCreationReq activityCreationReq1 = ActivityCreationReq.builder()
                .projectId(projectDto1.getId())
                .title("Text 1")
                .language("EN")
                .targetLanguage("KZ")
                .translatorId(TRANSLATOR_ID_ALDIYAR)
                .build();
        ActivityDto activityDto1 = activityService.create(activityCreationReq1);

        // Test ReviewRepository.countWithStatusTodoOrInProgressByChiefEditorAndProject:
//        Activity activity1 = activityRepository.findById(activityDto1.getId())
//                .orElseThrow(() -> new RuntimeException("Activity does not exist"));
//        Review review = Review.builder()
//                .activity(activity1)
//                .chiefEditorId(CHIEF_EDITOR_ID_NURZHAN)
//                .status(ReviewStatus.TO_DO.name())
//                .build();
//        reviewRepository.save(review);
//
//        long count = reviewRepository.countWithStatusTodoOrInProgressByChiefEditorAndProject(CHIEF_EDITOR_ID_NURZHAN,
//                activity1.getProject().getId());
//        System.out.println("Count: " + count);
//
//        Review sameReview = reviewRepository.findById(review.getId()).orElseThrow(() -> new RuntimeException("Review does not exist"));
//        System.out.println(sameReview.getActivity().getProject());
    }

    private void testFirebaseUserRepository() {
        Page<FirebaseUser> firebaseUserPage = userRepository.findAllPaginated(PageRequest.of(0, 10));
        List<FirebaseUser> firebaseUsers = firebaseUserPage.getContent();
        for (FirebaseUser user : firebaseUsers) {
            System.out.println(user);
        }

        Optional<FirebaseUser> nonExistentUserOptional = userRepository.findById("blabla");
        System.out.println(nonExistentUserOptional.isPresent());

        Optional<FirebaseUser> userOptional = userRepository.findById("XH4Powot0ve0fiCfi2B7197wuTD2");
        System.out.println(userOptional.get());
    }

    private void testProjectEntityToDto() {
        Project project = Project.builder()
                .id(1L)
                .name("Name")
                .description("description")
                .managerId("3BgCu717aSR5DwCLgXMQIAWCTJM2")
                .chiefEditorId("XH4Powot0ve0fiCfi2B7197wuTD2")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
//        System.out.println(projectService.entityToDto(project));
    }
}
