package de.szut.lf8_starter.project;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.szut.lf8_starter.project.client.EmployeeClient;
import org.junit.jupiter.api.Test; // JUnit 5!
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
// static imports für MockMvc:
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})

class CreationIT {

    @Autowired MockMvc mockMvc;                 // HTTP-Client im Test


    @MockBean EmployeeClient employeeClient;    // externer Dienst -> faken

    @Test
    void shouldReturn201() throws Exception {
        // Arrange
        doNothing().when(employeeClient).assertExists(42L);

        String body = """
        {
          "bezeichnung": "Website Relaunch",
          "kundenId": 3,
          "verantwortlicherMitarbeiterId": 42,
          "startdatum": "2025-01-15",
          "geplantesEnddatum": "2025-03-01",
          "beschreibung": "Neue Webseite mit modernem Design"
        }
        """;

        // Act & Assert
        mockMvc.perform(
                        post("/api/v1/projects")             // ggf. auf /api/projects ändern
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body)
                )
                .andExpect(status().isCreated())             // 201 vorhanden
                .andExpect(jsonPath("$.bezeichnung").value("Website Relaunch"))
                .andExpect(jsonPath("$.kundenId").value(3))
                .andExpect(jsonPath("$.verantwortlicherMitarbeiterId").value(42));
    }
}
