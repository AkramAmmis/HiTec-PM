package de.szut.lf8_starter.project;
import de.szut.lf8_starter.exceptionHandling.UnprocessableEntityException;
import de.szut.lf8_starter.project.DTO.ProjectCreateDto;
import de.szut.lf8_starter.project.DTO.ProjectResponseDto;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }


    public List<ProjectResponseDto> findAllProjects() {
        return projectRepository.findAll()
                .stream()
                .map(this::mapEntityToResponseDto)
                .collect(Collectors.toList());
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

        return mapEntityToResponseDto(saved);
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
