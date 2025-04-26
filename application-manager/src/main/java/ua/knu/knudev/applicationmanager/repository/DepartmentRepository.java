package ua.knu.knudev.applicationmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.knu.knudev.applicationmanager.domain.Department;
import java.util.UUID;

public interface DepartmentRepository extends JpaRepository<Department, UUID> {
}
