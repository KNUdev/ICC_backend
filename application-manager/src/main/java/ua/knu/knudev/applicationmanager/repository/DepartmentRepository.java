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
import ua.knu.knudev.applicationmanager.domain.Department;
import ua.knu.knudev.applicationmanager.domain.QApplication;
import ua.knu.knudev.applicationmanager.domain.QDepartment;
import ua.knu.knudev.applicationmanagerapi.dto.ApplicationDto;
import ua.knu.knudev.applicationmanagerapi.request.DepartmentGetAllRequest;

import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

import static ua.knu.knudev.icccommon.config.QEntityManagerUtil.getQueryFactory;

public interface DepartmentRepository extends JpaRepository<Department, UUID> {

    boolean existsByName_En(String nameEn);
    boolean existsByName_Uk(String nameUk);

    QDepartment qDepartment = QDepartment.department;
    QApplication qApplication = QApplication.application;

    default Page<Department> findAllBySearchQuery(Pageable pageable, DepartmentGetAllRequest request) {
        BooleanBuilder predicate = new BooleanBuilder();

        if(StringUtils.isNotBlank(request.searchQuery())){
            Arrays.stream(request.searchQuery().split("\\s+"))
                    .map(word -> qDepartment.name.en.containsIgnoreCase(word)
                            .or(qDepartment.name.uk.containsIgnoreCase(word)))
                    .reduce(BooleanExpression::and)
                    .ifPresent(predicate::and);
        }
        if (request.createdBefore() != null){
            predicate.and(qDepartment.createdAt.before(request.createdBefore()));
        }
        if (request.createdAfter() != null){
            predicate.and(qDepartment.createdAt.after(request.createdAfter()));
        }
        if (request.updatedBefore() != null){
            predicate.and(qDepartment.updatedAt.before(request.updatedBefore()));
        }
        if (request.updatedAfter() != null){
            predicate.and(qDepartment.updatedAt.after(request.updatedAfter()));
        }
        if (request.problemKeyword() != null && !request.problemKeyword().isBlank()) {
            predicate.and(qDepartment.applications.any().problemDescription.containsIgnoreCase(request.problemKeyword()));
        }

        JPAQuery<Department> query = getQueryFactory().selectFrom(qDepartment)
                .leftJoin(qDepartment.applications, qApplication)
                .fetchJoin()
                .where(predicate)
                .orderBy(qDepartment.name.en.asc(), qDepartment.name.uk.asc())
                .offset(pageable.isUnpaged() ? 0 : pageable.getOffset())
                .limit(pageable.isUnpaged() ? Integer.MAX_VALUE : pageable.getPageSize());

        return PageableExecutionUtils.getPage(query.fetch(), pageable, query::fetchCount);
    }
}
