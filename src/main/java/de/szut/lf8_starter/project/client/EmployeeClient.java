package de.szut.lf8_starter.project.client;

import de.szut.lf8_starter.project.DTO.EmployeeQualificationsResponse;
import de.szut.lf8_starter.project.DTO.QualificationDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Component
public class EmployeeClient {

    private final RestTemplate restTemplate;

    @Value("${employee.service.url:https://employee-api.szut.dev}")
    private String employeeServiceUrl;

    public EmployeeClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /** Prüft, ob Mitarbeiter existiert (200 = true, 404 = false). 401/403 sauber durchreichen. */
    public boolean exists(Long employeeId) {
        try {
            ResponseEntity<Void> resp = restTemplate.exchange(
                    employeeServiceUrl + "/employees/{id}",
                    HttpMethod.GET,
                    new HttpEntity<>(authHeaders()),   // Header weiterreichen
                    Void.class,
                    employeeId
            );
            return resp.getStatusCode().is2xxSuccessful();
        } catch (HttpClientErrorException.NotFound e) {
            return false;
        } catch (HttpClientErrorException.Unauthorized e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized beim Employee-Service");
        } catch (HttpClientErrorException.Forbidden e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Forbidden beim Employee-Service");
        }
    }

    /** Holt ALLE Qualifikationen (Top-Level-Array von QualificationDto). */
    public List<QualificationDto> getQualifications() {
        ResponseEntity<List<QualificationDto>> resp = restTemplate.exchange(
                employeeServiceUrl + "/qualifications",
                HttpMethod.GET,
                httpEntity(),
                new ParameterizedTypeReference<>() {}
        );
        return resp.getBody() != null ? resp.getBody() : List.of();
    }

    /** Holt Qualifikationen eines Mitarbeiters; 401/403 sauber durchreichen. */
    public List<QualificationDto> getQualifications(Long employeeId) {
        try {
            ResponseEntity<EmployeeQualificationsResponse> resp = restTemplate.exchange(
                    employeeServiceUrl + "/employees/{id}/qualifications",
                    HttpMethod.GET,
                    httpEntity(),
                    EmployeeQualificationsResponse.class,
                    employeeId
            );
            var body = resp.getBody();
            return (body != null && body.getQualifications() != null)
                    ? body.getQualifications()
                    : List.of();
        } catch (HttpClientErrorException.Unauthorized e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized beim Employee-Service");
        } catch (HttpClientErrorException.Forbidden e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Forbidden beim Employee-Service");
        }
    }

    // ---- intern ----

    private HttpEntity<Void> httpEntity() {
        return new HttpEntity<>(authHeaders());
    }

    private HttpHeaders authHeaders() {
        HttpHeaders h = new HttpHeaders();
        var attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs != null) {
            String auth = attrs.getRequest().getHeader(HttpHeaders.AUTHORIZATION);
            if (auth != null && !auth.isBlank()) {
                h.set(HttpHeaders.AUTHORIZATION, auth);
            }
        }
        return h;
    }

    public void assertExists(long l) {

    }

    /** Response-Wrapper für /employees/{id}/qualifications */


}
