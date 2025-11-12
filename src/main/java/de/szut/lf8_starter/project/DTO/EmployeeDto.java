package de.szut.lf8_starter.project.DTO;

import java.util.List;

public class EmployeeDto {
    private Long id;
    private String firstName;
    private String lastName;
    private List<SkillDto> skillSet;


    public EmployeeDto(Long id, String firstName, String lastName, List<SkillDto> skillSet) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.skillSet = skillSet;
    }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public List<SkillDto> getSkillSet() { return skillSet; }
    public void setSkillSet(List<SkillDto> skillSet) { this.skillSet = skillSet; }

}
