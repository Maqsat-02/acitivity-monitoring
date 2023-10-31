package kz.iitu.edu.activity.monitoring;

import kz.iitu.edu.activity.monitoring.dto.project.request.ProjectCreationReq;
import kz.iitu.edu.activity.monitoring.dto.project.response.ProjectDto;
import kz.iitu.edu.activity.monitoring.entity.FirebaseUser;
import kz.iitu.edu.activity.monitoring.entity.Project;
import kz.iitu.edu.activity.monitoring.mapper.ProjectMapper;
import kz.iitu.edu.activity.monitoring.repository.FirebaseUserRepository;
import kz.iitu.edu.activity.monitoring.service.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class StartupRunner implements CommandLineRunner {
    private final FirebaseUserRepository userRepository;
    private final ProjectService projectService;

    @Override
    public void run(String... args) throws Exception {
//        Page<FirebaseUser> firebaseUserPage = userRepository.findAllPaginated(PageRequest.of(0, 10));
//        List<FirebaseUser> firebaseUsers = firebaseUserPage.getContent();
//        for (FirebaseUser user : firebaseUsers) {
//            System.out.println(user);
//        }
//
//        Optional<FirebaseUser> nonExistentUserOptional = userRepository.findById("blabla");
//        System.out.println(nonExistentUserOptional.isPresent());
//
//        Optional<FirebaseUser> userOptional = userRepository.findById("XH4Powot0ve0fiCfi2B7197wuTD2");
//        System.out.println(userOptional.get());

//        Project project = Project.builder()
//                .id(1L)
//                .name("Name")
//                .description("description")
//                .managerId("3BgCu717aSR5DwCLgXMQIAWCTJM2")
//                .chiefEditorId("XH4Powot0ve0fiCfi2B7197wuTD2")
//                .createdAt(LocalDateTime.now())
//                .updatedAt(LocalDateTime.now())
//                .build();
//        ProjectCreationReq projectCreationReq1 = ProjectCreationReq
//                .builder()
//                .name("Name1")
//                .description("Description1")
//                .chiefEditorId("AR3yg7gYTMTTm8VBLXIWrSgnssc2")
//                .build();
//        ProjectCreationReq projectCreationReq2 = ProjectCreationReq
//                .builder()
//                .name("Name2")
//                .description("Description2")
//                .chiefEditorId("AR3yg7gYTMTTm8VBLXIWrSgnssc2")
//                .build();
//        ProjectCreationReq projectCreationReq3 = ProjectCreationReq
//                .builder()
//                .name("Name3")
//                .description("Description3")
//                .chiefEditorId("AR3yg7gYTMTTm8VBLXIWrSgnssc2")
//                .build();
//        projectService.createProject(projectCreationReq1, "3BgCu717aSR5DwCLgXMQIAWCTJM2");
//        projectService.createProject(projectCreationReq2, "3BgCu717aSR5DwCLgXMQIAWCTJM2");
//        projectService.createProject(projectCreationReq3, "3BgCu717aSR5DwCLgXMQIAWCTJM2");
//        System.out.println(projectService.entityToDto(project));
    }
}
