
package de.szut.lf8_starter.project.DTO;

import java.util.List;

/**
 * Wrapper-DTO für den Endpoint:
 * GET /employees/{id}/qualifications
 */
public class EmployeeQualificationsResponse {

    private Long employeeId;
    private List<QualificationDto> qualifications;

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public List<QualificationDto> getQualifications() {
        return qualifications;
    }

    public void setQualifications(List<QualificationDto> qualifications) {
        this.qualifications = qualifications;
    }
}
