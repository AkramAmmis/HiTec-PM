package de.szut.lf8_starter.project;

import de.szut.lf8_starter.project.DTO.EmployeeAssignmentRequest;
import de.szut.lf8_starter.project.DTO.ProjectCreateDto;
import de.szut.lf8_starter.project.DTO.ProjectResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/projects")
public class ProjectController {

    private final ProjectService service;

    public ProjectController(ProjectService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectResponseDto createProject(@Valid @RequestBody ProjectCreateDto dto) {
        return service.create(dto);
    }

    @GetMapping("/{id}")
    public ProjectResponseDto getProjectById(@PathVariable long id){
        return service.getById(id);
    }
    @DeleteMapping("/{id}")
    public  ResponseEntity<Void> deleteProject(@PathVariable long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/{projectId}/employees")
    public ResponseEntity<Void> assignEmployee(
            @PathVariable Long projectId,
            @Valid @RequestBody EmployeeAssignmentRequest request
    ) {
        service.assignEmployee(projectId, request);
        return ResponseEntity.noContent().build(); // 204
    }

}
