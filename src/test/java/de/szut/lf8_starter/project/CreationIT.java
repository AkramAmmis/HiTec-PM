package de.szut.lf8_starter.project;
import de.szut.lf8_starter.project.client.EmployeeClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

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
public class CreationIT {
    @Autowired MockMvc mockMvc;          // „HTTP-Client“ im Test
    ObjectMapper objectMapper;     // JSON serialisieren
    @Autowired ProjectRepository repo;        // optional: DB prüfen
    @MockBean EmployeeClient employeeClient;  // externer Dienst -> faken
}
