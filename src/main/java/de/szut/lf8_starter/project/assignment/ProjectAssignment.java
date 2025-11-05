package de.szut.lf8_starter.project.assignment;
import de.szut.lf8_starter.project.ProjectEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(
        name = "project_employee_assignments",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_project_employee",
                columnNames = {"project_id", "employee_id"}  // verhindert doppelte Zuordnung
        )
)

@Entity
public class ProjectAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Beziehung zu einem Projekt
    @ManyToOne                // <--- das sagt: "Viele Zuordnungen gehören zu einem Projekt"
    @JoinColumn(name = "project_id") // <--- Name der Spalte in der Tabelle
    private ProjectEntity project;

    private Long employeeId;  // keine echte Beziehung, nur ID (kommt aus anderem Service)
    private Long roleId;
}

