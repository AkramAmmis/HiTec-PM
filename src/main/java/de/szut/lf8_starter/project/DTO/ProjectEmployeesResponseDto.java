package de.szut.lf8_starter.project.DTO;

import java.util.List;

public class ProjectEmployeesResponseDto {
    private Long projectId;
    private String projectBezeichnung;
    private List<EmployeeDto> employees;

    public ProjectEmployeesResponseDto(Long projectId, String projectBezeichnung, List<EmployeeDto> employees) {
        this.projectId = projectId;
        this.projectBezeichnung = projectBezeichnung;
        this.employees = employees;
    }

    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }

    public String getProjectBezeichnung() { return projectBezeichnung; }
    public void setProjectBezeichnung(String projectBezeichnung) { this.projectBezeichnung = projectBezeichnung; }

    public List<EmployeeDto> getEmployees() { return employees; }
    public void setEmployees(List<EmployeeDto> employees) { this.employees = employees; }
}
