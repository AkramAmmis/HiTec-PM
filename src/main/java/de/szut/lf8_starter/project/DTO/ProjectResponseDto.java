package de.szut.lf8_starter.project.DTO;

import java.time.LocalDate;

public class ProjectResponseDto {
        private Long id;
        private String bezeichnung;
        private Long kundenId;
        private Long verantwortlicherMitarbeiterId;
        private LocalDate startdatum;
        private LocalDate geplantesEnddatum;
        private String beschreibung;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDate getGeplantesEnddatum() {
        return geplantesEnddatum;
    }

    public void setGeplantesEnddatum(LocalDate geplantesEnddatum) {
        this.geplantesEnddatum = geplantesEnddatum;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }
}
