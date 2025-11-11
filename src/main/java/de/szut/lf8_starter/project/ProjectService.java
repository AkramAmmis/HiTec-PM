package de.szut.lf8_starter.project;

import de.szut.lf8_starter.exceptionHandling.ResourceNotFoundException;
import de.szut.lf8_starter.exceptionHandling.UnprocessableEntityException;
import de.szut.lf8_starter.project.DTO.ProjectCreateDto;
import de.szut.lf8_starter.project.DTO.ProjectResponseDto;
import de.szut.lf8_starter.project.assignment.ProjectAssignment;
import de.szut.lf8_starter.project.client.EmployeeClient;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.Optional;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectAssignmentRepository projectAssignmentRepository;
    private final EmployeeClient employeeClient;

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
        if (dto.getGeplantesEnddatum() != null && !dto.getStartdatum().isBefore(dto.getGeplantesEnddatum())) {
            throw new UnprocessableEntityException("Das Startdatum muss vor dem geplanten Enddatum liegen.");
        }
        if (dto.getKundenId() == null || dto.getKundenId() <= 0) {
            throw new UnprocessableEntityException("kundenId ist ungültig.");
        }

        ProjectEntity entity = new ProjectEntity();
        entity.setBezeichnung(dto.getBezeichnung());
        entity.setKundenId(dto.getKundenId());
        entity.setVerantwortlicherMitarbeiterId(dto.getVerantwortlicherMitarbeiterId());
        entity.setStartdatum(dto.getStartdatum());
        entity.setGeplantesEnddatum(dto.getGeplantesEnddatum());
        entity.setBeschreibung(dto.getBeschreibung());

        ProjectEntity saved = projectRepository.save(entity);
        return mapEntityToResponseDto(saved);
    }

    public void deleteById(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new ResourceNotFoundException("Projekt " + id + " nicht gefunden");
        }
        projectRepository.deleteById(id);
    }

    // ==== NEU: Zuweisung ====
    @Transactional
    public void assignEmployeeToProject(Long projectId, Long employeeId, Long roleId) {
        var project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Projekt mit ID " + projectId + " nicht gefunden."));

        if (!employeeClient.exists(employeeId)) {
            throw new ResourceNotFoundException("Mitarbeiter mit ID " + employeeId + " nicht gefunden.");
        }

        var qualifications = employeeClient.getQualifications(employeeId);
        boolean hasQualification = qualifications.stream().anyMatch(q -> q.getId() .equals(roleId));
        if (!hasQualification) {
            throw new UnprocessableEntityException("Mitarbeiter hat die benötigte Qualifikation nicht.");
        }

        if (projectAssignmentRepository.existsByProjectIdAndEmployeeId(projectId, employeeId)) {
            throw new UnprocessableEntityException("Mitarbeiter ist bereits diesem Projekt zugeordnet.");
        }

        ProjectAssignment pa = new ProjectAssignment();
        pa.setProject(project);
        pa.setEmployeeId(employeeId);
        pa.setRoleId(roleId);
        projectAssignmentRepository.save(pa);
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
