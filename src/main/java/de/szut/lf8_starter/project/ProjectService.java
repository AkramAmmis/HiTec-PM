package de.szut.lf8_starter.project;

import de.szut.lf8_starter.exceptionHandling.ResourceNotFoundException;
import de.szut.lf8_starter.exceptionHandling.UnprocessableEntityException;
import de.szut.lf8_starter.project.DTO.ProjectCreateDto;
import de.szut.lf8_starter.project.DTO.ProjectResponseDto;
import de.szut.lf8_starter.project.client.EmployeeClient;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectAssignmentRepository projectAssignmentRepository; // wird später für die Zuweisung gebraucht
    private final EmployeeClient employeeClient;

    // Konstruktor-Injection (Spring erstellt die Instanz und befüllt die Felder)
    public ProjectService(ProjectRepository projectRepository,
                          ProjectAssignmentRepository projectAssignmentRepository,
                          EmployeeClient employeeClient) {
        this.projectRepository = projectRepository;
        this.projectAssignmentRepository = projectAssignmentRepository;
        this.employeeClient = employeeClient;
    }

    public ProjectResponseDto getById(Long id) {
        Optional<ProjectEntity> project = projectRepository.findById(id);
        if (project.isEmpty()) {
            throw new ResourceNotFoundException("Projekt mit ID " + id + " konnte nicht gefunden werden.");
        }
        return mapEntityToResponseDto(project.get());
    }

    @Transactional
    public ProjectResponseDto create(ProjectCreateDto dto) {
        // 422: Start < Ende (falls Ende gesetzt)
        if (dto.getGeplantesEnddatum() != null && !dto.getStartdatum().isBefore(dto.getGeplantesEnddatum())) {
            throw new UnprocessableEntityException("Das Startdatum muss vor dem geplanten Enddatum liegen.");
        }

        // 422: Kunden-ID plausibel?
        if (dto.getKundenId() == null || dto.getKundenId() <= 0) {
            throw new UnprocessableEntityException("kundenId ist ungültig.");
        }

        // DTO -> Entity
        ProjectEntity entity = new ProjectEntity();
        entity.setBezeichnung(dto.getBezeichnung());
        entity.setKundenId(dto.getKundenId());
        entity.setVerantwortlicherMitarbeiterId(dto.getVerantwortlicherMitarbeiterId());
        entity.setStartdatum(dto.getStartdatum());
        entity.setGeplantesEnddatum(dto.getGeplantesEnddatum());
        entity.setBeschreibung(dto.getBeschreibung());

        // speichern
        ProjectEntity saved = projectRepository.save(entity);

        // Entity -> ResponseDto
        return mapEntityToResponseDto(saved);
    }

    public void deleteById(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new ResourceNotFoundException("Projekt " + id + " nicht gefunden");
        }
        projectRepository.deleteById(id);
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

    @Transactional
    public void assignEmployeeToProject(Long projectId, Long employeeId, Long roleId) {
        // 1️ Projekt ?
        var project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Projekt mit ID " + projectId + " nicht gefunden."));

        // Mitarbeiter ?
        if (!employeeClient.exists(employeeId)) {
            throw new ResourceNotFoundException("Mitarbeiter mit ID " + employeeId + " nicht gefunden.");
        }

        //  Hat der Mitarbeiter die Qualifikation (roleId)?
        var qualifications = employeeClient.getQualifications(employeeId);
        boolean hasQualification = qualifications.stream().anyMatch(q -> q.getId().equals(roleId));
        if (!hasQualification) {
            throw new UnprocessableEntityException("Mitarbeiter hat die benötigte Qualifikation nicht.");
        }

        // Schon in diesem Projekt?
        if (projectAssignmentRepository.existsByProjectIdAndEmployeeId(projectId, employeeId)) {
            throw new UnprocessableEntityException("Mitarbeiter ist bereits diesem Projekt zugeordnet.");
        }

        // 5️⃣ Neues Assignment erstellen
        var assignment = new de.szut.lf8_starter.project.assignment.ProjectAssignment();
        assignment.setProject(project);
        assignment.setEmployeeId(employeeId);
        assignment.setRoleId(roleId);

        projectAssignmentRepository.save(assignment);
    }

}
