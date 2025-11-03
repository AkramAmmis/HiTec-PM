package de.szut.lf8_starter.project;
import de.szut.lf8_starter.exceptionHandling.ResourceNotFoundException;
import de.szut.lf8_starter.exceptionHandling.UnprocessableEntityException;
import de.szut.lf8_starter.project.DTO.ProjectCreateDto;
import de.szut.lf8_starter.project.DTO.ProjectResponseDto;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
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
        //  Datum prüfen (start < end wenn gesetzt) -> 422
        if (dto.getGeplantesEnddatum() != null && !dto.getStartdatum().isBefore(dto.getGeplantesEnddatum())) {
            throw new UnprocessableEntityException("Das Startdatum muss vor dem geplanten Enddatum liegen.");
        }

        // Kunden-ID Dummy prüfen -> 422 bei Ungültigkeit
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
        ProjectResponseDto r = new ProjectResponseDto();
        r.setId(saved.getId());
        r.setBezeichnung(saved.getBezeichnung());
        r.setKundenId(saved.getKundenId());
        r.setVerantwortlicherMitarbeiterId(saved.getVerantwortlicherMitarbeiterId());
        r.setStartdatum(saved.getStartdatum());
        r.setGeplantesEnddatum(saved.getGeplantesEnddatum());
        r.setBeschreibung(saved.getBeschreibung());
        return r;
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
    public void deleteById(Long id) {
        // 1) Existenz prüfen -> 404
        if (!projectRepository.existsById(id)) {
            throw new ResourceNotFoundException("Projekt " + id + " nicht gefunden");
        }
        // 2) Löschen
        projectRepository.deleteById(id);}
}
