package de.szut.lf8_starter.project;

import de.szut.lf8_starter.testcontainers.AbstractIntegrationTest;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

public class GetAllProjectsIT extends AbstractIntegrationTest {
    ProjectEntity project1 = new ProjectEntity();
    ProjectEntity project2 = new ProjectEntity();
    ProjectEntity project3 = new ProjectEntity();

    public void insertTestProjects() {
        project1.setId(1L);
        project1.setBezeichnung("Nebula Nexus");
        project1.setKundenId(1L);
        project1.setVerantwortlicherMitarbeiterId(1L);
        project1.setStartdatum(LocalDate.of(2025, 4, 18));
        project1.setGeplantesEnddatum(LocalDate.of(2025, 10, 18));
        project1.setBeschreibung("Entwicklung einer cloudbasierten Plattform zur zentralen Verwaltung und Analyse interstellarer Datenströme.");

        project2.setId(2L);
        project2.setBezeichnung("Orion Ops");
        project2.setKundenId(2L);
        project2.setVerantwortlicherMitarbeiterId(2L);
        project2.setStartdatum(LocalDate.of(2025, 5, 10));
        project2.setGeplantesEnddatum(LocalDate.of(2025, 11, 20));
        project2.setBeschreibung("Entwicklung eines automatisierten Deployment systems für interplanetare Serverflotten.");

        project3.setId(3L);
        project3.setBezeichnung("Lunar Logic");
        project3.setKundenId(3L);
        project3.setVerantwortlicherMitarbeiterId(3L);
        project3.setStartdatum(LocalDate.of(2025, 2, 5));
        project3.setGeplantesEnddatum(LocalDate.of(2025, 8, 15));
        project3.setBeschreibung("KI-gestützte Optimierung von Kommunikationsrouten zwischen Mondbasen und Erdstationen.");
    }

    public void getAllProjects_returnsAllProjects() throws Exception {
        mockMvc.perform(get("/api/v1/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[*].bezeichnung", hasItem("Nebula Nexus")))
                .andExpect(jsonPath("$[*].id").exists())
                .andExpect(jsonPath("$[*].bezeichnung").exists())
                .andExpect(jsonPath("$[*].kundenId").exists());

        ;
    }
}