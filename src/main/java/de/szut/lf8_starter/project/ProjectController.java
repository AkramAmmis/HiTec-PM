package de.szut.lf8_starter.project;

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
    @ResponseStatus(HttpStatus.CREATED)  // 👈 Das ist der Trick!
    public ProjectResponseDto createProject(@Valid @RequestBody ProjectCreateDto dto) {
        return service.create(dto);
    }

    @GetMapping("/{id}")
    public ProjectResponseDto getProjectById(@PathVariable long id){
        return service.getById(id);
    }
}
