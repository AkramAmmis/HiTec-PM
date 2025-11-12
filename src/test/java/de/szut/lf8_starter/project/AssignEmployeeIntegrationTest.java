package de.szut.lf8_starter.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.szut.lf8_starter.project.DTO.EmployeeAssignmentRequest;
import de.szut.lf8_starter.project.DTO.QualificationDto;
import de.szut.lf8_starter.project.client.EmployeeClient;
import de.szut.lf8_starter.project.ProjectEntity;
import de.szut.lf8_starter.project.ProjectRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AssignEmployeeIntegrationTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired
    de.szut.lf8_starter.project.ProjectRepository projectRepository;

    @MockBean EmployeeClient employeeClient;
    @Test
    @DisplayName("POST /api/v1/projects/{id}/employees -> 204 bei erfolgreicher Zuweisung")
    void shouldReturn204WhenEmployeeAssignedSuccessfully() throws Exception {
        // Arrange: Projekt anlegen
        ProjectEntity p = new ProjectEntity();
        p.setBezeichnung("Testprojekt");
        p.setKundenId(100L);
        p.setVerantwortlicherMitarbeiterId(1L);
        p.setStartdatum(LocalDate.now());
        var saved = projectRepository.save(p);
        long projectId = saved.getId();

        // Arrange: Request
        long maId = 1L;
        long roleId = 1L;
        var req = new EmployeeAssignmentRequest();
        req.setMaId(maId);
        req.setRoleId(roleId);


        when(employeeClient.exists(maId)).thenReturn(true);

        var q = new QualificationDto();
        q.setId(roleId);
        q.setDesignation("egal"); // Name egal, Hauptsache ID passt
        when(employeeClient.getQualifications(maId)).thenReturn(List.of(q));

        // Act & Assert
        mockMvc.perform(post("/api/v1/projects/{projectId}/employees", projectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isNoContent());
    }


}
