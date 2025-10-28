package de.szut.lf8_starter.project.client;

import org.springframework.stereotype.Component;

@Component
public class EmployeeClient {

    public void assertExists(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Ungültige Mitarbeiter-ID: " + id);
        }
    }

}

