package de.szut.lf8_starter.project.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeAssignmentRequest {
    @NotNull
    @JsonProperty("ma_id")   // akzeptiert snake_case im JSON
    private Long maId;

    @NotNull
    @JsonProperty("role_id") // akzeptiert snake_case im JSON
    private Long roleId;

    public Long getMaId() { return maId; }
    public void setMaId(Long maId) { this.maId = maId; }

    public Long getRoleId() { return roleId; }
    public void setRoleId(Long roleId) { this.roleId = roleId;
    }}
