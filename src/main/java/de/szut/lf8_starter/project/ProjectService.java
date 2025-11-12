package de.szut.lf8_starter.project;
import de.szut.lf8_starter.exceptionHandling.ConflictException;
import de.szut.lf8_starter.exceptionHandling.ResourceNotFoundException;
import de.szut.lf8_starter.exceptionHandling.UnprocessableEntityException;
import de.szut.lf8_starter.project.DTO.ProjectCreateDto;
import de.szut.lf8_starter.project.DTO.ProjectResponseDto;
import de.szut.lf8_starter.project.client.EmployeeClient;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    private final EmployeeClient employeeClient;
    private final HttpServletRequest request;

    public ProjectService(ProjectRepository projectRepository, EmployeeClient employeeClient, HttpServletRequest request) {
        this.projectRepository = projectRepository;
        this.employeeClient = employeeClient;
        this.request = request;
    }

    public List<ProjectResponseDto> findAllProjects() {
        return projectRepository.findAll()
                .stream()
                .map(this::mapEntityToResponseDto)
                .collect(Collectors.toList());
    }


    public ProjectResponseDto getById(Long id) {
        Optional<ProjectEntity> project = projectRepository.findById(id);
        if (project.isEmpty()) {
            throw new ResourceNotFoundException("Projekt mit ID " + id + " konnte nicht gefunden werden.");
        }
        return mapEntityToResponseDto(project.orElse(null));
    }

    @Transactional
    public ProjectResponseDto create(ProjectCreateDto dto) {
        ProjectEntity saved;
        try {
            // DTO -> Entity
            ProjectEntity entity = new ProjectEntity();
            entity.setBezeichnung(dto.getBezeichnung());
            entity.setKundenId(dto.getKundenId());
            entity.setVerantwortlicherMitarbeiterId(dto.getVerantwortlicherMitarbeiterId());
            entity.setStartdatum(dto.getStartdatum());
            entity.setGeplantesEnddatum(dto.getGeplantesEnddatum());
            entity.setBeschreibung(dto.getBeschreibung());

            //  Datum prüfen (start < end wenn gesetzt) -> 422
            // Kunden-ID Dummy prüfen -> 422 bei Ungültigkeit
            validateProjectData(dto, entity);

            // Mitarbeiter-ID Dummy prüfen -> 422 bei Ungültigkeit
            validateEmployeeExists(dto.getVerantwortlicherMitarbeiterId());

            // speichern
            saved = projectRepository.save(entity);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (UnprocessableEntityException e) {
            throw new UnprocessableEntityException(e.getMessage());
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
        // Entity -> ResponseDto
        return mapEntityToResponseDto(projectRepository.save(saved));
    }

    public void deleteById(Long id) {
        // 1) Existenz prüfen -> 404
        if (!projectRepository.existsById(id)) {
            throw new ResourceNotFoundException("Projekt " + id + " nicht gefunden");
        }
        // 2) Löschen
        projectRepository.deleteById(id);
    }

    @Transactional
    public ProjectResponseDto update(Long id, ProjectCreateDto dto) {
        ProjectEntity entity;
        try{
            entity = projectRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Projekt mit ID " + id + " konnte nicht gefunden werden."));

            validateProjectData(dto, entity);
            validateEmployeeExists(dto.getVerantwortlicherMitarbeiterId());
            validateNoConflicts(entity);

            updateEntityFromDto(entity, dto);

        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (ConflictException e) {
            throw new ConflictException(e.getMessage());
        } catch (UnprocessableEntityException e) {
            throw new UnprocessableEntityException(e.getMessage());
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }


        return mapEntityToResponseDto(projectRepository.save(entity));
    }

    private void validateProjectData(ProjectCreateDto dto, ProjectEntity existingEntity) {
        if (dto.getGeplantesEnddatum() != null && !dto.getStartdatum().isBefore(dto.getGeplantesEnddatum())) {
            throw new UnprocessableEntityException("Das Startdatum muss vor dem geplanten Enddatum liegen.");
        }
        if (dto.getKundenId() == null || dto.getKundenId() <= 0) {
            throw new UnprocessableEntityException("kundenId ist ungültig.");
        }
        if (dto.getStartdatum().isBefore(existingEntity.getStartdatum())) {
            throw new UnprocessableEntityException("Das Startdatum kann nicht in die Vergangenheit verschoben werden.");
        }
        validateChanges(dto, existingEntity);
    }

    private void validateChanges(ProjectCreateDto dto, ProjectEntity existingEntity) {
        if (dto.getStartdatum().isBefore(existingEntity.getStartdatum())) {
            throw new UnprocessableEntityException("Das Startdatum kann nicht in die Vergangenheit verschoben werden.");
        }
    }

    private void validateNoConflicts(ProjectEntity existingEntity) {
        if (existingEntity.getGeplantesEnddatum() != null &&
                existingEntity.getGeplantesEnddatum().isBefore(LocalDate.now())) {
            throw new ConflictException("Das Projekt ist bereits abgeschlossen und kann nicht mehr bearbeitet werden.");
        }
    }

    private void validateEmployeeExists(Long mitarbeiterId) {
        try {
            String authHeader = request.getHeader("Authorization");
            employeeClient.assertEmployeeExists(mitarbeiterId, authHeader);
        } catch (RuntimeException e) {
            throw new ResourceNotFoundException("Mitarbeiter mit ID " + mitarbeiterId + " existiert nicht: " + e.getMessage());
        }
    }

    private void updateEntityFromDto(ProjectEntity entity, ProjectCreateDto dto) {
        entity.setBezeichnung(dto.getBezeichnung());
        entity.setKundenId(dto.getKundenId());
        entity.setVerantwortlicherMitarbeiterId(dto.getVerantwortlicherMitarbeiterId());
        entity.setStartdatum(dto.getStartdatum());
        entity.setGeplantesEnddatum(dto.getGeplantesEnddatum());
        entity.setBeschreibung(dto.getBeschreibung());
    }

    private ProjectResponseDto mapEntityToResponseDto(ProjectEntity entity) {
        ProjectResponseDto dto = new ProjectResponseDto();
        dto.setId(entity.getId());
        dto.setBezeichnung(entity.getBezeichnung());
        dto.setKundenId(entity.getKundenId());
        dto.setVerantwortlicherMitarbeiterId(entity.getVerantwortlicherMitarbeiterId());
        dto.setStartdatum(entity.getStartdatum());
        dto.setGeplantesEnddatum(entity.getGeplantesEnddatum());
        dto.setBeschreibung(entity.getBeschreibung());
        return dto;
    }
}
