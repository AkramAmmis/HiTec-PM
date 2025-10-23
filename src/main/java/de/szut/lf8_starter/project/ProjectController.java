package de.szut.lf8_starter.project;

import de.szut.lf8_starter.project.DTO.ProjectCreateDto;
import de.szut.lf8_starter.project.DTO.ProjectResponseDto;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ProjectResponseDto createProject(
            @Valid @RequestBody ProjectCreateDto dto) {
        return projectService.create(dto);
      // 201 + Location + Body
    }

    @GetMapping
    public List<ProjectResponseDto> getAllProjects() {
        return projectService.findAllProjects();
    }
}