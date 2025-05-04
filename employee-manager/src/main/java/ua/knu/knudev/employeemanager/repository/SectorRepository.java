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
import ua.knu.knudev.employeemanager.domain.Sector;
import ua.knu.knudev.employeemanagerapi.request.SectorReceivingRequest;
import ua.knu.knudev.icccommon.dto.MultiLanguageFieldDto;

import java.util.Arrays;
import java.util.UUID;

import static ua.knu.knudev.icccommon.config.QEntityManagerUtil.getQueryFactory;

public interface SectorRepository extends JpaRepository<Sector, UUID> {

    QSector qSector = QSector.sector;
    QSpecialty qSpecialty = QSpecialty.specialty;

    default Page<Sector> findAllBySearchQuery(Pageable pageable, SectorReceivingRequest request) {
        BooleanBuilder predicate = new BooleanBuilder();

        addBySearchQuery(predicate, request.searchQuery());
        addBySpecialtyName(predicate, request.specialtyName());
        if (request.createdBefore() != null) {
            predicate.and(qSector.createdAt.before(request.createdBefore()));
        }
        if (request.createdAfter() != null) {
            predicate.and(qSector.createdAt.after(request.createdAfter()));
        }
        if (request.updatedBefore() != null) {
            predicate.and(qSector.updatedAt.before(request.updatedBefore()));
        }
        if (request.updatedAfter() != null) {
            predicate.and(qSector.updatedAt.after(request.updatedAfter()));
        }

        JPAQuery<Sector> query = getQueryFactory().selectFrom(qSector)
                .leftJoin(qSector.specialties, qSpecialty)
                .fetchJoin()
                .where(predicate)
                .orderBy(qSector.name.en.asc(), qSector.name.uk.asc())
                .offset(pageable.isUnpaged() ? 0 : pageable.getOffset())
                .limit(pageable.isUnpaged() ? Integer.MAX_VALUE : pageable.getPageSize());

        return PageableExecutionUtils.getPage(query.fetch(), pageable, query::fetchCount);
    }


    private void addBySearchQuery(BooleanBuilder predicate, String searchQuery) {
        if (StringUtils.isNotBlank(searchQuery)) {
            Arrays.stream(searchQuery.split("\\s+"))
                    .map(word ->
                            qSector.name.en.containsIgnoreCase(word)
                                    .or(qSector.name.uk.containsIgnoreCase(word))
                    )
                    .reduce(BooleanExpression::and)
                    .ifPresent(predicate::and);
        }
    }

    private void addBySpecialtyName(BooleanBuilder predicate, MultiLanguageFieldDto specialtyName) {
        if (specialtyName != null) {
            if (specialtyName.getEn() != null) {
                predicate.and(qSector.specialties.any().name.en.containsIgnoreCase(specialtyName.getEn()));
            }
            if (specialtyName.getUk() != null) {
                predicate.and(qSector.specialties.any().name.uk.containsIgnoreCase(specialtyName.getUk()));
            }
        }
    }
}
