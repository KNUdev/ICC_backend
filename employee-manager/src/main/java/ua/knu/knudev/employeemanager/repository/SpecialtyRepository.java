package ua.knu.knudev.employeemanager.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.support.PageableExecutionUtils;
import ua.knu.knudev.employeemanager.domain.QSector;
import ua.knu.knudev.employeemanager.domain.QSpecialty;
import ua.knu.knudev.employeemanager.domain.Specialty;
import ua.knu.knudev.employeemanagerapi.request.SpecialtyReceivingRequest;

import java.util.Arrays;
import java.util.UUID;

import static ua.knu.knudev.icccommon.config.QEntityManagerUtil.getQueryFactory;

public interface SpecialtyRepository extends JpaRepository<Specialty, UUID> {

    QSpecialty qSpecialty = QSpecialty.specialty;
    QSector qSector = QSector.sector;

    default Page<Specialty> getSpecialtiesByFilter(Pageable pageable, SpecialtyReceivingRequest specialtyReceivingRequest) {
        BooleanBuilder predicate = new BooleanBuilder();

        if (StringUtils.isNotBlank(specialtyReceivingRequest.searchQuery())) {
            Arrays.stream(specialtyReceivingRequest.searchQuery().split("\\s+"))
                    .map(word -> qSpecialty.name.en.containsIgnoreCase(word)
                            .or(qSpecialty.name.uk.containsIgnoreCase(word)))
                    .reduce(BooleanExpression::and)
                    .ifPresent(predicate::and);
        }
        if(specialtyReceivingRequest.sectorName() != null) {
            if(specialtyReceivingRequest.sectorName().getEn() != null) {
                predicate.and(qSpecialty.sectors.any().name.en.containsIgnoreCase(specialtyReceivingRequest.sectorName().getEn()));
            }
            if(specialtyReceivingRequest.sectorName().getUk() != null) {
                predicate.and(qSpecialty.sectors.any().name.uk.containsIgnoreCase(specialtyReceivingRequest.sectorName().getUk()));
            }
        }
        if (specialtyReceivingRequest.category() != null) {
            predicate.and(qSpecialty.category.eq(specialtyReceivingRequest.category()));
        }
        if (specialtyReceivingRequest.createdAfter() != null) {
            predicate.and(qSpecialty.createdAt.after(specialtyReceivingRequest.createdAfter()));
        }
        if (specialtyReceivingRequest.createdBefore() != null) {
            predicate.and(qSpecialty.createdAt.before(specialtyReceivingRequest.createdBefore()));
        }
        if (specialtyReceivingRequest.updatedAfter() != null) {
            predicate.and(qSpecialty.updatedAt.after(specialtyReceivingRequest.updatedAfter()));
        }
        if (specialtyReceivingRequest.updatedBefore() != null) {
            predicate.and(qSpecialty.updatedAt.before(specialtyReceivingRequest.updatedBefore()));
        }

        JPAQuery<Specialty> query = getQueryFactory().selectFrom(qSpecialty)
                .leftJoin(qSpecialty.sectors, qSector)
                .fetchJoin()
                .where(predicate)
                .orderBy(qSpecialty.name.en.asc(), qSpecialty.name.uk.asc())
                .offset(pageable.isUnpaged() ? 0 : pageable.getOffset())
                .limit(pageable.isUnpaged() ? Integer.MAX_VALUE : pageable.getPageSize());

        return PageableExecutionUtils.getPage(query.fetch(), pageable, query::fetchCount);
    }
}
