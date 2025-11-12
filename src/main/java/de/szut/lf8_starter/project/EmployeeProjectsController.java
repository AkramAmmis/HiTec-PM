package de.szut.lf8_starter.project;

import de.szut.lf8_starter.project.DTO.EmployeeProjectsResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeProjectsController {

    private final ProjectService projectService;

    public EmployeeProjectsController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/{employeeId}/projects")
    @ResponseStatus(HttpStatus.OK)
    public EmployeeProjectsResponseDto getEmployeeProjects(@PathVariable Long employeeId) {
        return projectService.getProjectsByEmployee(employeeId);
    }
}

