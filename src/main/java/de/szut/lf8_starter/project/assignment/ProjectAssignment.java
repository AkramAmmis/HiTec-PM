package de.szut.lf8_starter.project.assignment;
import de.szut.lf8_starter.project.ProjectEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Entity
@Table(name = "project_assignment",
        uniqueConstraints = @UniqueConstraint(columnNames = {"project_id", "employee_id"}))
public class ProjectAssignment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false) @JoinColumn(name = "project_id")
    private ProjectEntity project;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "role_id", nullable = false)
    private Long roleId;

}
