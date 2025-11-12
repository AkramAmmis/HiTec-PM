package de.szut.lf8_starter.project.client;

import de.szut.lf8_starter.exceptionHandling.ResourceNotFoundException;
import de.szut.lf8_starter.project.DTO.EmployeeDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class EmployeeClient {

    private final RestTemplate restTemplate;

    @Value("${employee.service.url:https://employee-api.szut.dev}")
    private String employeeServiceUrl;

    public EmployeeClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void assertEmployeeExists(Long id, String token){
        assertExists(id);

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token.replace("Bearer ", ""));
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    employeeServiceUrl + "/employees/" + id,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            if (response.getStatusCode() != HttpStatus.OK) {
                throw new RuntimeException("Mitarbeiter mit ID " + id + " existiert nicht");
            }
        }catch (ResourceNotFoundException e) {
            throw new RuntimeException("Mitarbeiter mit ID " + id + " nicht gefunden");
        } catch (Exception e) {
            throw new RuntimeException("Fehler bei der Überprüfung der Mitarbeiter-ID: " + e.getMessage());
        }
    }
    public EmployeeDto getEmployeeById(Long id, String token) {
        assertExists(id);

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token.replace("Bearer ", ""));
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<EmployeeDto> response = restTemplate.exchange(
                    employeeServiceUrl + "/employees/" + id,
                    HttpMethod.GET,
                    entity,
                    EmployeeDto.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            } else {
                throw new RuntimeException("Mitarbeiter mit ID " + id + " konnte nicht abgerufen werden");
            }
        } catch (ResourceNotFoundException e) {
            throw new RuntimeException("Mitarbeiter mit ID " + id + " nicht gefunden");
        } catch (Exception e) {
            throw new RuntimeException("Fehler beim Abrufen der Mitarbeiterdaten: " + e.getMessage());
        }
    }

    public void assertExists(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Ungültige Mitarbeiter-ID: " + id);
        }
    }

}

