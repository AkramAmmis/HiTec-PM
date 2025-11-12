package de.szut.lf8_starter.project;

import de.szut.lf8_starter.project.assignment.ProjectAssignment;
import de.szut.lf8_starter.project.client.EmployeeClient;
import de.szut.lf8_starter.project.DTO.QualificationDto;
import de.szut.lf8_starter.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class GetEmployeeProjectsIT extends AbstractIntegrationTest {

    @Autowired private ProjectRepository projectRepository;
    @Autowired private ProjectAssignmentRepository projectAssignmentRepository;

    @MockBean private EmployeeClient employeeClient;

    @BeforeEach
    void setup() {
        projectAssignmentRepository.deleteAll();
        projectRepository.deleteAll();
    }

    @Test
    void returns200_withProjects() throws Exception {
        Long employeeId = 99L;

        when(employeeClient.exists(employeeId)).thenReturn(true);
        QualificationDto q = new QualificationDto(); q.setId(7L); q.setDesignation("Backend Dev");
        when(employeeClient.getQualifications(employeeId)).thenReturn(List.of(q));

        ProjectEntity p = new ProjectEntity();
        p.setBezeichnung("Andromeda");
        p.setKundenId(1L);
        p.setVerantwortlicherMitarbeiterId(10L);
        p.setStartdatum(LocalDate.of(2025, 1, 1));
        p.setGeplantesEnddatum(LocalDate.of(2025, 12, 31));
        projectRepository.save(p);

        ProjectAssignment pa = new ProjectAssignment();
        pa.setProject(p);
        pa.setEmployeeId(employeeId);
        pa.setRoleId(7L);
        projectAssignmentRepository.save(pa);

        mockMvc.perform(get("/api/v1/employees/{employeeId}/projects", employeeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeId").value(employeeId))
                .andExpect(jsonPath("$.projects[0].projectId").value(p.getId()))
                .andExpect(jsonPath("$.projects[0].bezeichnung").value("Andromeda"))
                .andExpect(jsonPath("$.projects[0].startdatum").exists())
                .andExpect(jsonPath("$.projects[0].enddatum").exists())
                .andExpect(jsonPath("$.projects[0].roleId").value(7))
                .andExpect(jsonPath("$.projects[0].roleName").value("Backend Dev"));
    }
}
