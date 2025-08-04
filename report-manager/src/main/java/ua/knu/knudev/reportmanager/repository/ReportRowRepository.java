package ua.knu.knudev.reportmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.knu.knudev.reportmanager.domain.ReportRowEntity;

public interface ReportRowRepository extends JpaRepository<ReportRowEntity, Long> {
}
