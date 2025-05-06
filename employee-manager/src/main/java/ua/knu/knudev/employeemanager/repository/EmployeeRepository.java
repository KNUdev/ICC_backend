package ua.knu.knudev.employeemanager.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.jpa.impl.JPAQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.support.PageableExecutionUtils;
import ua.knu.knudev.employeemanager.domain.Employee;
import ua.knu.knudev.employeemanager.domain.QEmployee;
import ua.knu.knudev.employeemanagerapi.request.EmployeeReceivingRequest;

import java.util.Arrays;
import java.util.UUID;

import static ua.knu.knudev.icccommon.config.QEntityManagerUtil.getQueryFactory;

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {

    QEmployee qEmployee = QEmployee.employee;

    default Page<Employee> findAllBySearchQuery(Pageable pageable, EmployeeReceivingRequest request) {
        BooleanBuilder predicate = new BooleanBuilder();

        addBySearchQuery(predicate, request.searchQuery());
        addIfNotNull(predicate, qEmployee.email, request.email());
        addIfNotNull(predicate, qEmployee.phoneNumber, request.phoneNumber());
        addIfNotNull(predicate, qEmployee.salaryInUAH, request.salaryInUAH());
        addIfNotNull(predicate, qEmployee.isStudent, request.isStudent());
        addIfNotNull(predicate, qEmployee.avatar, request.avatar());
        addIfNotNull(predicate, qEmployee.workHours.startTime, request.workHours().getStartTime());
        addIfNotNull(predicate, qEmployee.workHours.endTime, request.workHours().getEndTime());
        addIfNotNull(predicate, qEmployee.role, request.role());
        addIfNotNull(predicate, qEmployee.specialty.name.en, request.specialtyName().getEn());
        addIfNotNull(predicate, qEmployee.specialty.name.uk, request.specialtyName().getUk());
        addIfNotNull(predicate, qEmployee.sector.name.en, request.sectorName().getEn());
        addIfNotNull(predicate, qEmployee.sector.name.uk, request.sectorName().getUk());

        if (request.contractEndDateBefore() != null) {
            predicate.and(qEmployee.contractEndDate.before(request.contractEndDateBefore()));
        }
        if (request.contractEndDateAfter() != null) {
            predicate.and(qEmployee.contractEndDate.after(request.contractEndDateAfter()));
        }
        if (request.createdBefore() != null) {
            predicate.and(qEmployee.createdAt.before(request.createdBefore()));
        }
        if (request.createdAfter() != null) {
            predicate.and(qEmployee.createdAt.after(request.createdAfter()));
        }
        if (request.updatedBefore() != null) {
            predicate.and(qEmployee.updatedAt.before(request.updatedBefore()));
        }
        if (request.updatedAfter() != null) {
            predicate.and(qEmployee.updatedAt.after(request.updatedAfter()));
        }

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

    private <T> void addIfNotNull(BooleanBuilder predicate, SimpleExpression<T> field, T value) {
        if (value != null) {
            predicate.and(field.eq(value));
        }
    }
}
