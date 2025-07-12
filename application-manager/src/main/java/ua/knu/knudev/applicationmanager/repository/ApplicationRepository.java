package ua.knu.knudev.applicationmanager.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.jpa.impl.JPAQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.support.PageableExecutionUtils;
import ua.knu.knudev.applicationmanager.domain.Application;
import ua.knu.knudev.applicationmanager.domain.QApplication;
import ua.knu.knudev.applicationmanager.domain.QDepartment;
import ua.knu.knudev.applicationmanagerapi.request.ApplicationGetAllRequest;

import java.util.Arrays;
import java.util.UUID;

import static ua.knu.knudev.icccommon.config.QEntityManagerUtil.getQueryFactory;

public interface ApplicationRepository extends JpaRepository<Application, UUID> {

    QApplication qApplication = QApplication.application;
    QDepartment qDepartment = QDepartment.department;

    default Page<Application> findAllBySearchQuery(Pageable pageable, ApplicationGetAllRequest request) {
        BooleanBuilder predicate = new BooleanBuilder();

        if (StringUtils.isNotBlank((request.searchQuery()))) {
            Arrays.stream(request.searchQuery().split("\\s+"))
                    .map(word -> qApplication.applicantName.firstName.contains(word)
                            .or(qApplication.applicantName.middleName.contains(word))
                            .or(qApplication.applicantName.lastName.contains(word)))
                    .reduce(BooleanExpression::and)
                    .ifPresent(predicate::and);
        }
        addIfNotNull(predicate, qApplication.applicantEmail, request.applicantEmail());
        addIfNotNull(predicate, qApplication.problemDescription, request.problemDescription());
        addIfNotNull(predicate, qApplication.problemPhoto, request.problemPhoto());
        addIfNotNull(predicate, qApplication.department.name.en, request.departmentName().getEn());
        addIfNotNull(predicate, qApplication.department.name.uk, request.departmentName().getUk());
        addIfNotNull(predicate, qApplication.status, request.status());
        if (request.assignedEmployeeIds() != null && !request.assignedEmployeeIds().isEmpty()) {
            predicate.and(qApplication.assignedEmployeeIds.any().in(request.assignedEmployeeIds()));
        }

        if (request.receivedBefore() != null) {
            predicate.and(qApplication.receivedAt.before(request.receivedBefore()));
        }
        if (request.receivedAfter() != null) {
            predicate.and(qApplication.receivedAt.after(request.receivedAfter()));
        }
        if (request.completedBefore() != null) {
            predicate.and(qApplication.completedAt.before(request.completedBefore()));
        }
        if (request.completedAfter() != null) {
            predicate.and(qApplication.completedAt.after(request.completedAfter()));
        }

        JPAQuery<Application> query = getQueryFactory().selectFrom(qApplication)
                .leftJoin(qApplication.department, qDepartment)
                .fetchJoin()
                .where(predicate)
                .orderBy(qApplication.problemDescription.desc())
                .offset(pageable.isUnpaged() ? 0 : pageable.getOffset())
                .limit(pageable.isUnpaged() ? Integer.MAX_VALUE : pageable.getPageSize());

        return PageableExecutionUtils.getPage(query.fetch(), pageable, query::fetchCount);
    }

    private <T> void addIfNotNull(BooleanBuilder predicate, SimpleExpression<T> field, T value) {
        if (value != null) {
            predicate.and(field.eq(value));
        }
    }
}
