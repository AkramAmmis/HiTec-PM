package de.szut.lf8_starter.project;

import de.szut.lf8_starter.testcontainers.AbstractIntegrationTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

public class GetAllProjectsIT extends AbstractIntegrationTest {
    ProjectEntity entity = new ProjectEntity();

    public void insertTestProject() {
        entity.setId(1L);
        entity.setBezeichnung("Nebula Nexus");
        entity.setKundenId(1L);
        entity.setVerantwortlicherMitarbeiterId(1L);
        entity.setStartdatum(LocalDate.of(2025, 4, 18));
        entity.setGeplantesEnddatum(LocalDate.of(2025, 10, 18));
        entity.setBeschreibung("Entwicklung einer cloudbasierten Plattform zur zentralen Verwaltung und Analyse interstellarer Datenströme.");
    }

    public void getAllProjects_returnAllProjects() throws Exception {
        mockMvc.perform(get("/api/v1/projects")).andExpect(status().isOk());
    }
}
