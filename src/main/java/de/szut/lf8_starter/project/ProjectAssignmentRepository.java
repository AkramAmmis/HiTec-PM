package de.szut.lf8_starter.project;

import de.szut.lf8_starter.project.assignment.ProjectAssignment;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface ProjectAssignmentRepository extends JpaRepository<ProjectAssignment, Long> {
    boolean existsByProjectIdAndEmployeeId(Long projectId, Long employeeId);


}
