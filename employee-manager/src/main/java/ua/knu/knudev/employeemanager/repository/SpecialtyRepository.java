package ua.knu.knudev.employeemanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.knu.knudev.employeemanager.domain.Specialty;

import java.util.UUID;

public interface SpecialtyRepository extends JpaRepository<Specialty, UUID> {

}
