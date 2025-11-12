package de.szut.lf8_starter.project;

import de.szut.lf8_starter.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GetByIdIT extends AbstractIntegrationTest {

    @Autowired
    private ProjectRepository projectRepository;

    private Long projectId;

    @BeforeEach
    void setUp() {
        projectRepository.deleteAll();

        ProjectEntity entity = new ProjectEntity();
        entity.setBezeichnung("Mein Testprojekt");
        entity.setKundenId(123L);
        entity.setVerantwortlicherMitarbeiterId(456L);
        entity.setStartdatum(LocalDate.parse("2024-01-15"));
        entity.setGeplantesEnddatum(LocalDate.parse("2024-12-31"));
        entity.setBeschreibung("Das ist eine Testbeschreibung für das Projekt");

        ProjectEntity saved = projectRepository.save(entity);
        projectId = saved.getId();
    }

    @Test
    @WithMockUser(roles = "user")
    void happyPath() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/projects/" + projectId)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(projectId.intValue())))
                .andExpect(jsonPath("$.bezeichnung", is("Mein Testprojekt")))
                .andExpect(jsonPath("$.kundenId", is(123)))
                .andExpect(jsonPath("$.verantwortlicherMitarbeiterId", is(456)))
                .andExpect(jsonPath("$.startdatum", is("2024-01-15")))
                .andExpect(jsonPath("$.geplantesEnddatum", is("2024-12-31")))
                .andExpect(jsonPath("$.beschreibung", is("Das ist eine Testbeschreibung für das Projekt")));
    }

}
