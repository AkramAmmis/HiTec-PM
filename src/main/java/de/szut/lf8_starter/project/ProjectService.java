package de.szut.lf8_starter.project;

import de.szut.lf8_starter.project.DTO.ProjectCreateDto;
import de.szut.lf8_starter.project.DTO.ProjectResponseDto;

public class ProjectService {
    public ProjectResponseDto create(ProjectCreateDto dto) {
        // 1) Datum prüfen (start < end wenn gesetzt) -> 422
        if (dto.getGeplantesEnddatum() != null && !dto.getStartdatum().isBefore(dto.getGeplantesEnddatum())) {
            return null;
        }
        return null;
        // 2) Employee-ID existiert? -> employeeClient.assertExists(id) -> 404 bei Nichtexistenz
        // 3) Kunden-ID Dummy-Gültigkeit prüfen -> 422
        // 4) DTO -> Entity, repo.save(entity)
        // 5) Entity -> ResponseDto zurück

    }
}
