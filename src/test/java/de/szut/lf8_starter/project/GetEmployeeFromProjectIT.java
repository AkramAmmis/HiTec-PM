package de.szut.lf8_starter.project;

import de.szut.lf8_starter.project.DTO.EmployeeDto;
import de.szut.lf8_starter.project.DTO.SkillDto;
import de.szut.lf8_starter.project.client.EmployeeClient;
import de.szut.lf8_starter.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GetEmployeeFromProjectIT extends AbstractIntegrationTest {

    @Autowired
    private ProjectRepository projectRepository;

    @MockBean
    private EmployeeClient employeeClient;

    private Long projectId;
    private EmployeeDto mockEmployee;

    @BeforeEach
    void setUp() {
        projectRepository.deleteAll();

        ProjectEntity entity = new ProjectEntity();
        entity.setBezeichnung("Testprojekt");
        entity.setKundenId(123L);
        entity.setVerantwortlicherMitarbeiterId(1L);
        entity.setStartdatum(LocalDate.parse("2025-01-15"));
        entity.setGeplantesEnddatum(LocalDate.parse("2025-12-31"));
        entity.setBeschreibung("Testbeschreibung");

        ProjectEntity saved = projectRepository.save(entity);
        projectId = saved.getId();

        List<SkillDto> skills = List.of(
                new SkillDto(1L, "Java"),
                new SkillDto(2L, "Spring Boot"),
                new SkillDto(3L, "Testcontainers")
        );

        mockEmployee = new EmployeeDto(
                1L,
                "Max",
                "Mustermann",
                skills
        );
    }

    @Test
    @WithMockUser(roles = "user")
    void happyPath() throws Exception {

        when(employeeClient.getEmployeeById(eq(1L), isNull()))
                .thenReturn(mockEmployee);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/projects/" + projectId + "/employees")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.projectId", is(projectId.intValue())))
                .andExpect(jsonPath("$.projectBezeichnung", is("Testprojekt")))
                .andExpect(jsonPath("$.employees", hasSize(1)))
                .andExpect(jsonPath("$.employees[0].id", is(1)))
                .andExpect(jsonPath("$.employees[0].firstName", is("Max")))
                .andExpect(jsonPath("$.employees[0].lastName", is("Mustermann")))
                .andExpect(jsonPath("$.employees[0].skillSet", hasSize(3)))
                .andExpect(jsonPath("$.employees[0].skillSet[0].id", is(1)))
                .andExpect(jsonPath("$.employees[0].skillSet[0].skill", is("Java")))
                .andExpect(jsonPath("$.employees[0].skillSet[1].id", is(2)))
                .andExpect(jsonPath("$.employees[0].skillSet[1].skill", is("Spring Boot")))
                .andExpect(jsonPath("$.employees[0].skillSet[2].id", is(3)))
                .andExpect(jsonPath("$.employees[0].skillSet[2].skill", is("Testcontainers")));
    }
}