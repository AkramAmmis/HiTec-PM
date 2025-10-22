package de.szut.lf8_starter.project;
import de.szut.lf8_starter.exceptionHandling.UnprocessableEntityException;
import de.szut.lf8_starter.project.DTO.ProjectCreateDto;
import de.szut.lf8_starter.project.DTO.ProjectResponseDto;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {
    public ProjectResponseDto create(ProjectCreateDto dto) {
        // 1) Datum prüfen (start < end wenn gesetzt) -> 422
        if (dto.getGeplantesEnddatum() != null && !dto.getStartdatum().isBefore(dto.getGeplantesEnddatum())) {
            throw new UnprocessableEntityException("Das Startdatum muss vor dem geplanten Enddatum liegen.");
        }

        // 2) Employee-ID existiert? -> employeeClient.assertExists(id) -> 404 bei Nichtexistenz
        // 3) Kunden-ID Dummy-Gültigkeit prüfen -> 422
        // 4) DTO -> Entity, repo.save(entity)
        // 5) Entity -> ResponseDto zurück
        return null;

    }
}
