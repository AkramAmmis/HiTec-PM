package de.szut.lf8_starter.project;

import de.szut.lf8_starter.project.assignment.ProjectAssignment;
import de.szut.lf8_starter.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RemoveEmployeeFromProjectIT extends AbstractIntegrationTest {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectAssignmentRepository projectAssignmentRepository;

    @BeforeEach
    void setup() {
        projectAssignmentRepository.deleteAll();
        projectRepository.deleteAll();

        ProjectEntity project = new ProjectEntity();
        project.setBezeichnung("Solar Synapse");
        project.setKundenId(1L);
        project.setVerantwortlicherMitarbeiterId(10L);
        project.setStartdatum(LocalDate.of(2025, 1, 1));
        project.setGeplantesEnddatum(LocalDate.of(2025, 12, 31));
        project.setBeschreibung("Testprojekt für Unassign-Endpoint.");
        projectRepository.save(project);

        ProjectAssignment assignment = new ProjectAssignment();
        assignment.setProject(project);
        assignment.setEmployeeId(99L);
        assignment.setRoleId(7L);
        projectAssignmentRepository.save(assignment);
    }

    @Test
    void deleteEmployeeAssignment_returns204_whenSuccessful() throws Exception {
        mockMvc.perform(delete("/api/v1/projects/{projectId}/employees/{employeeId}", 1L, 99L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
