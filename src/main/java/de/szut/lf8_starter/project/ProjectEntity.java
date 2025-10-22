package de.szut.lf8_starter.project;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@Entity
@Table(name = "projects")
public class ProjectEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false) private String bezeichnung;
    @Column(nullable=false) private Long kundenId;
    @Column(nullable=false) private Long verantwortlicherMitarbeiterId;

    @Column(nullable=false) private LocalDate startdatum;

    private LocalDate geplantesEnddatum;
    @Column(columnDefinition = "text") private String beschreibung;

}