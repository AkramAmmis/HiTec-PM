package de.szut.lf8_starter.project.DTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class ProjectCreateDto {

    @NotBlank
    private String bezeichnung;

    @NotNull
    private Long kundenId;

    @NotNull
    private Long verantwortlicherMitarbeiterId;

    @NotNull
    private LocalDate startdatum;

    private LocalDate geplantesEnddatum;
    private String beschreibung;

    public LocalDate getGeplantesEnddatum() {
        return geplantesEnddatum;
    }

    public void setGeplantesEnddatum(LocalDate geplantesEnddatum) {
        this.geplantesEnddatum = geplantesEnddatum;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public Long getKundenId() {
        return kundenId;
    }

    public void setKundenId(Long kundenId) {
        this.kundenId = kundenId;
    }

    public Long getVerantwortlicherMitarbeiterId() {
        return verantwortlicherMitarbeiterId;
    }

    public void setVerantwortlicherMitarbeiterId(Long verantwortlicherMitarbeiterId) {
        this.verantwortlicherMitarbeiterId = verantwortlicherMitarbeiterId;
    }

    public LocalDate getStartdatum() {
        return startdatum;
    }

    public void setStartdatum(LocalDate startdatum) {
        this.startdatum = startdatum;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    // Getter und Setter
}

