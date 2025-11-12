package de.szut.lf8_starter.project;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class DeleteIT {
    @Autowired
    MockMvc mockMvc;
    @Autowired ProjectRepository projectRepository;

    @Test
    public void shouldReturn204_whenProjectDeleted() throws Exception {
        // Arrange: gültiges Projekt in H2 anlegen
        var p = new ProjectEntity();                    // falls dein Name anders ist: anpassen
        p.setBezeichnung("Zum Löschen");
        p.setKundenId(7L);
        p.setVerantwortlicherMitarbeiterId(42L);
        p.setStartdatum(LocalDate.of(2025, 1, 15));
        p.setGeplantesEnddatum(LocalDate.of(2025, 3, 1));
        p.setBeschreibung("Erstellt, um gelöscht zu werden.");

        var saved = projectRepository.save(p);
        Long id = saved.getId();
        mockMvc.perform(delete("/api/v1/projects/{id}", id)).andExpect(status().isNoContent());
        assertTrue(projectRepository.findById(id).isEmpty(), "Projekt sollte gelöscht sein");
    }


    }
