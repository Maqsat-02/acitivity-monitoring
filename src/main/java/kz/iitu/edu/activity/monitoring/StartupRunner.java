package kz.iitu.edu.activity.monitoring;

import kz.iitu.edu.activity.monitoring.entity.FirebaseUser;
import kz.iitu.edu.activity.monitoring.repository.FirebaseUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class StartupRunner implements CommandLineRunner {
    private final FirebaseUserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
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
}
