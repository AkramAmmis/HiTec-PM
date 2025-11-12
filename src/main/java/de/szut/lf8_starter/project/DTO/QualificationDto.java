package de.szut.lf8_starter.project.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class QualificationDto {
    private Long id;

    @JsonProperty("skill")
    private String designation;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }
}
