package de.szut.lf8_starter.project;
import de.szut.lf8_starter.project.client.EmployeeClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;


import java.time.LocalDate;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.show-sql=false"
})
public class GetByIdIT {
    @Autowired MockMvc mockMvc;          // „HTTP-Client“ im Test
    ObjectMapper objectMapper;     // JSON serialisieren
    @Autowired ProjectRepository repo;        // optional: DB prüfen
    @MockBean EmployeeClient employeeClient;  // externer Dienst -> faken
    Long projectId;
    ProjectEntity entity;

    @BeforeEach
    void setUp() {
        entity = new ProjectEntity();
        entity.setBezeichnung("Mein Testprojekt");
        entity.setKundenId(123L);
        entity.setVerantwortlicherMitarbeiterId(456L);
        entity.setStartdatum(LocalDate.parse("2024-01-15"));
        entity.setGeplantesEnddatum(LocalDate.parse("2024-12-31"));
        entity.setBeschreibung("Das ist eine Testbeschreibung für das Projekt");
        repo.save(entity);
        projectId = entity.getId();

    }

    @Test
    void testHappyPath() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/projects/" + projectId))
                .andExpect(status().isOk());
    }

}
