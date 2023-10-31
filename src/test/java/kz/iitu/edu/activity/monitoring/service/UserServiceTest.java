package kz.iitu.edu.activity.monitoring.service;

import kz.iitu.edu.activity.monitoring.entity.FirebaseUser;
import kz.iitu.edu.activity.monitoring.enums.Role;
import kz.iitu.edu.activity.monitoring.repository.FirebaseUserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.Mockito.*;

class UserServiceTest {
    @Mock
    FirebaseUserRepository userRepository;
    @InjectMocks
    UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetTranslatorByIdOrThrow() {
        FirebaseUser manager = FirebaseUser
                .builder()
                .role(Role.TRANSLATOR.name())
                .build();

        when(userRepository.findById(anyString())).thenReturn(Optional.ofNullable(manager));

        FirebaseUser result = userService.getTranslatorByIdOrThrow("id");
        Assertions.assertEquals(FirebaseUser.builder().role(Role.TRANSLATOR.name()).build(), result);}

    @Test
    void testGetManagerByIdOrThrow() {
        FirebaseUser manager = FirebaseUser
                .builder()
                .role(Role.PROJECT_MANAGER.name())
                .build();

        when(userRepository.findById(anyString())).thenReturn(Optional.ofNullable(manager));

        FirebaseUser result = userService.getManagerByIdOrThrow("id");
        Assertions.assertEquals(FirebaseUser.builder().role(Role.PROJECT_MANAGER.name()).build(), result);
    }

    @Test
    void testGetChiefEditorByIdOrThrow() {
        FirebaseUser chiefEditor = FirebaseUser
                .builder()
                .role(Role.CHIEF_EDITOR.name())
                .build();

        when(userRepository.findById(anyString())).thenReturn(Optional.ofNullable(chiefEditor));

        FirebaseUser result = userService.getChiefEditorByIdOrThrow("id");
        Assertions.assertEquals(FirebaseUser.builder().role(Role.CHIEF_EDITOR.name()).build(), result);
    }
}