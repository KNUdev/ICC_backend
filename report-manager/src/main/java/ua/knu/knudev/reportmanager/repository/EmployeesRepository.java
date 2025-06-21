package ua.knu.knudev.reportmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.knu.knudev.employeemanager.domain.Employee;

public interface EmployeesRepository extends JpaRepository<Employee, Long> {

}
