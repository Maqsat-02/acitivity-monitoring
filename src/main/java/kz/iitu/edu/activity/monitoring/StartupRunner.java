package kz.iitu.edu.activity.monitoring;

import kz.iitu.edu.activity.monitoring.dto.project.response.ProjectDto;
import kz.iitu.edu.activity.monitoring.entity.FirebaseUser;
import kz.iitu.edu.activity.monitoring.entity.Project;
import kz.iitu.edu.activity.monitoring.mapper.ProjectMapper;
import kz.iitu.edu.activity.monitoring.mapper.ProjectMapperUtil;
import kz.iitu.edu.activity.monitoring.repository.FirebaseUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class StartupRunner implements CommandLineRunner {
    private final FirebaseUserRepository userRepository;
    private final ProjectMapperUtil projectMapperUtil;

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

        Project project = Project.builder()
                .id(1L)
                .name("Name")
                .description("description")
                .managerId("3BgCu717aSR5DwCLgXMQIAWCTJM2")
                .chiefEditorId("XH4Powot0ve0fiCfi2B7197wuTD2")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        System.out.println(projectMapperUtil.entityToDto(project));
    }
}
