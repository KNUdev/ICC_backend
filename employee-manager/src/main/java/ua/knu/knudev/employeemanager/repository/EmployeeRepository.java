package ua.knu.knudev.employeemanager.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.support.PageableExecutionUtils;
import ua.knu.knudev.employeemanager.domain.Employee;
import ua.knu.knudev.employeemanager.domain.QEmployee;
import ua.knu.knudev.employeemanager.domain.QSector;
import ua.knu.knudev.employeemanager.domain.QSpecialty;
import ua.knu.knudev.employeemanager.domain.embeddable.WorkHours;
import ua.knu.knudev.icccommon.constant.EmployeeAdministrativeRole;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

import static ua.knu.knudev.icccommon.config.QEntityManagerUtil.getQueryFactory;

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {

    QEmployee qEmployee = QEmployee.employee;
    QSpecialty qSpecialty = QSpecialty.specialty;
    QSector qSector = QSector.sector;

    default Page<Employee> findAllBySearchQuery(
            Pageable pageable, String searchQuery, String email, String phoneNumber,
            LocalDateTime createdAt, LocalDateTime updatedAt, Double salaryInUAH, Boolean isStudent,
            String avatar, LocalDate contractEndDate, WorkHours workHours, EmployeeAdministrativeRole role,
            String specialtyName, String sectorName
    ) {
        BooleanBuilder predicate = new BooleanBuilder();

        addBySearchQuery(predicate, searchQuery);
        addIfNotNull(predicate, qEmployee.email, email);
        addIfNotNull(predicate, qEmployee.phoneNumber, phoneNumber);
        addIfNotNull(predicate, qEmployee.createdAt, createdAt);
        addIfNotNull(predicate, qEmployee.updatedAt, updatedAt);
        addIfNotNull(predicate, qEmployee.salaryInUAH, salaryInUAH);
        addIfNotNull(predicate, qEmployee.isStudent, isStudent);
        addIfNotNull(predicate, qEmployee.avatar, avatar);
        addIfNotNull(predicate, qEmployee.contractEndDate, contractEndDate);
        addIfNotNull(predicate, qEmployee.workHours.startTime, workHours.getStartTime());
        addIfNotNull(predicate, qEmployee.workHours.endTime, workHours.getEndTime());
        addIfNotNull(predicate, qEmployee.role, role);
        addByLocalizedName(predicate, qSpecialty.name.en, qSpecialty.name.uk, specialtyName);
        addByLocalizedName(predicate, qSector.name.en, qSector.name.uk, sectorName);

        JPAQuery<Employee> query = getQueryFactory().selectFrom(qEmployee)
                .where(predicate)
                .orderBy(qEmployee.name.middleName.asc(), qEmployee.name.firstName.asc())
                .offset(pageable.isUnpaged() ? 0 : pageable.getOffset())
                .limit(pageable.isUnpaged() ? Integer.MAX_VALUE : pageable.getPageSize());

        return PageableExecutionUtils.getPage(query.fetch(), pageable, query::fetchCount);
    }

    private void addBySearchQuery(BooleanBuilder predicate, String searchQuery) {
        if (StringUtils.isNotBlank(searchQuery)) {
            Arrays.stream(searchQuery.split("\\s+"))
                    .map(word ->
                            qEmployee.name.firstName.contains(word)
                                    .or(qEmployee.name.middleName.contains(word))
                                    .or(qEmployee.name.lastName.contains(word))
                    )
                    .reduce(BooleanExpression::and)
                    .ifPresent(predicate::and);
        }
    }

    private void addByLocalizedName(BooleanBuilder predicate, StringPath en, StringPath uk, String name) {
        if (name != null) {
            predicate.and(en.eq(name).or(uk.eq(name)));
        }
    }

    private <T> void addIfNotNull(BooleanBuilder predicate, SimpleExpression<T> field, T value) {
        if (value != null) {
            predicate.and(field.eq(value));
        }
    }
}
