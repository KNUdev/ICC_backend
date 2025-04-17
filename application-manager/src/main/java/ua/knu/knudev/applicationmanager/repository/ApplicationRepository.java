package ua.knu.knudev.applicationmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.knu.knudev.applicationmanager.domain.Application;

import java.util.UUID;

public interface ApplicationRepository extends JpaRepository<Application, UUID> {
}
