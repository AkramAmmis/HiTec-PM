package de.szut.lf8_starter.project.DTO;

import java.time.LocalDate;

public class EmployeeProjectItemDto {
    private Long projectId;
    private String bezeichnung;
    private LocalDate startdatum;
    private LocalDate enddatum;   // aus geplantesEnddatum
    private Long roleId;
    private String roleName;      // aus Employee-Service

    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }
    public String getBezeichnung() { return bezeichnung; }
    public void setBezeichnung(String bezeichnung) { this.bezeichnung = bezeichnung; }
    public LocalDate getStartdatum() { return startdatum; }
    public void setStartdatum(LocalDate startdatum) { this.startdatum = startdatum; }
    public LocalDate getEnddatum() { return enddatum; }
    public void setEnddatum(LocalDate enddatum) { this.enddatum = enddatum; }
    public Long getRoleId() { return roleId; }
    public void setRoleId(Long roleId) { this.roleId = roleId; }
    public String getRoleName() { return roleName; }
    public void setRoleName(String roleName) { this.roleName = roleName; }
}
