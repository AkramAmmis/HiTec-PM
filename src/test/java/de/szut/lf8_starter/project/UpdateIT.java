package de.szut.lf8_starter.project;

import de.szut.lf8_starter.project.client.EmployeeClient;
import de.szut.lf8_starter.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UpdateIT extends AbstractIntegrationTest {
    @Autowired
    private ProjectRepository projectRepository;

    @MockBean
    private EmployeeClient employeeClient;

    private Long projectId;

    @BeforeEach
    void setUp() {
        projectRepository.deleteAll();

        ProjectEntity entity = new ProjectEntity();
        entity.setBezeichnung("Altes Test Projekt");
        entity.setKundenId(123L);
        entity.setVerantwortlicherMitarbeiterId(456L);
        entity.setStartdatum(LocalDate.parse("2025-01-15"));
        entity.setGeplantesEnddatum(LocalDate.now().plusDays(1));
        entity.setBeschreibung("Alte Test Beschreibung");

        ProjectEntity saved = projectRepository.save(entity);
        projectId = saved.getId();

        doNothing().when(employeeClient).assertEmployeeExists(any(Long.class), anyString());
    }
    @Test
    @WithMockUser(roles = "user")
    void happyPath() throws Exception {
        LocalDate geplantesEnddatum = LocalDate.now().plusDays(2);
        String updateJson = String.format("""
            {
                "bezeichnung": "Test Projekt",
                "kundenId": 789,
                "verantwortlicherMitarbeiterId": 999,
                "startdatum": "2025-02-01",
                "geplantesEnddatum": "%s",
                "beschreibung": "Test Beschreibung"
            }
            """, geplantesEnddatum);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/projects/" + projectId)
                        .contentType("application/json")
                        .content(updateJson)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(projectId.intValue())))
                .andExpect(jsonPath("$.bezeichnung", is("Test Projekt")))
                .andExpect(jsonPath("$.kundenId", is(789)))
                .andExpect(jsonPath("$.verantwortlicherMitarbeiterId", is(999)))
                .andExpect(jsonPath("$.startdatum", is("2025-02-01")))
                .andExpect(jsonPath("$.geplantesEnddatum", is(geplantesEnddatum.toString())))
                .andExpect(jsonPath("$.beschreibung", is("Test Beschreibung")));
    }

}
