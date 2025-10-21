package de.szut.lf8_starter.project;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "projects")
public class ProjectEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false) private String bezeichnung;
    @Column(nullable=false) private Long kundenId;
    @Column(nullable=false) private Long verantwortlicherMitarbeiterId;

    public LocalDate getStartdatum() {
        return startdatum;
    }

    public void setStartdatum(LocalDate startdatum) {
        this.startdatum = startdatum;
    }

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

    @Column(nullable=false) private LocalDate startdatum;

    private LocalDate geplantesEnddatum;
    @Column(columnDefinition = "text") private String beschreibung;

}