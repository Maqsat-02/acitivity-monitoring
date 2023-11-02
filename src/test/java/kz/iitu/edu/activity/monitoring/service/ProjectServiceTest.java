package kz.iitu.edu.activity.monitoring.service;

import kz.iitu.edu.activity.monitoring.dto.common.response.UserDto;
import kz.iitu.edu.activity.monitoring.dto.project.request.ProjectCreationReq;
import kz.iitu.edu.activity.monitoring.dto.project.request.ProjectUpdateReq;
import kz.iitu.edu.activity.monitoring.dto.project.response.ProjectDto;
import kz.iitu.edu.activity.monitoring.entity.FirebaseUser;
import kz.iitu.edu.activity.monitoring.entity.Project;
import kz.iitu.edu.activity.monitoring.repository.ProjectRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

class ProjectServiceTest {
    @Mock
    ProjectRepository projectRepository;
    @Mock
    UserService userService;
    @InjectMocks
    ProjectService projectService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate() {
        // Mock UserService
        when(userService.getManagerByIdOrThrow(anyString())).thenReturn(new FirebaseUser("1", "email", "role", "firstName", "lastName"));
        when(userService.getChiefEditorByIdOrThrow(anyString())).thenReturn(new FirebaseUser("2", "email", "role", "firstName", "lastName"));

        // Create a Project object to return when save is called
        Project project = Project.builder()
                .id(1L)
                .managerId("1")
                .chiefEditorId("2")
                .build();

        // Create a mock for ProjectRepository
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        // Create an instance of ProjectService and inject the mock dependencies

        // Call the method you want to test
        ProjectCreationReq creationReq = ProjectCreationReq.builder()
                .chiefEditorId("2")
                .build();
        ProjectDto result = projectService.create(creationReq, "1");

        // Assert the result
        ProjectDto expected = ProjectDto.builder()
                .id(1L)
                .manager(UserDto.builder()
                        .id("1")
                        .role("role")
                        .firstName("firstName")
                        .lastName("lastName")
                        .build())
                .chiefEditor(UserDto.builder()
                        .id("2")
                        .role("role")
                        .firstName("firstName")
                        .lastName("lastName")
                        .build())
                .extraChiefEditors(new ArrayList<>())
                .build();

        Assertions.assertEquals(expected, result);
    }

    @Test
    void testGetAll() {
        // Create a list of projects
        List<Project> projectList = List.of(Project.builder()
                .extraChiefEditors(new ArrayList<>())
                .build()
        );

        // Mock the Page<Project> with a PageImpl
        Page<Project> jk = new PageImpl<>(projectList);

        // Define the behavior for projectRepository mock
        when(projectRepository.findAllByOrderByIdDesc(PageRequest.of(0, 1))).thenReturn(jk);

        // Call the method you want to test
        List<ProjectDto> result = projectService.getAll(PageRequest.of(0, 1));

        // Assert the result
        Assertions.assertEquals(1, result.size());
    }

    @Test
    void testUpdate() {
        when(userService.getManagerByIdOrThrow(anyString())).thenReturn(new FirebaseUser("1", "email", "role", "firstName", "lastName"));
        when(userService.getChiefEditorByIdOrThrow(anyString())).thenReturn(new FirebaseUser("2", "email", "role", "firstName", "lastName"));
        // Create a Project object to return when findById is called
        Project project = Project.builder()
                .id(1L)
                .extraChiefEditors(new ArrayList<>())
                .build();

        // Define the behavior for projectRepository mock
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        ProjectUpdateReq updateReq = ProjectUpdateReq.builder().build();/* initialize your updateReq object here */

        ProjectDto expected = ProjectDto.builder()
                .id(1L)
                .extraChiefEditors(new ArrayList<>())
                .build();
        when(projectRepository.save(any())).thenReturn(project);
        // Call the method you want to test
        ProjectDto result = projectService.update(1L, updateReq);
        // Assert the result
        Assertions.assertEquals(result, expected);
    }

    @Test
    void testGetByIdOrThrow() {
        Project project = Project.builder().build();
        when(projectRepository.findById(anyLong())).thenReturn(Optional.ofNullable(project));
        Project result = projectService.getByIdOrThrow(Long.valueOf(1));
        Assertions.assertEquals(new Project(), result);
    }
}
