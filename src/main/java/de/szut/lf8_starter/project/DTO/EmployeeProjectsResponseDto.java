package de.szut.lf8_starter.project.DTO;

import java.util.List;

public class EmployeeProjectsResponseDto {
    private Long employeeId;
    private List<EmployeeProjectItemDto> projects;

    public EmployeeProjectsResponseDto() { }
    public EmployeeProjectsResponseDto(Long employeeId, List<EmployeeProjectItemDto> projects) {
        this.employeeId = employeeId;
        this.projects = projects;
    }

    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }
    public List<EmployeeProjectItemDto> getProjects() { return projects; }
    public void setProjects(List<EmployeeProjectItemDto> projects) { this.projects = projects; }
}
