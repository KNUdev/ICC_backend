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

import java.util.Arrays;
import java.util.UUID;

import static ua.knu.knudev.icccommon.config.QEntityManagerUtil.getQueryFactory;

public interface SectorRepository extends JpaRepository<Sector, UUID> {

    QSector qSector = QSector.sector;
    QSpecialty qSpecialty = QSpecialty.specialty;

    default Page<Sector> findAllBySearchQuery(Pageable pageable, SectorReceivingRequest request) {
        BooleanBuilder predicate = new BooleanBuilder();

        addBySearchQuery(predicate, request.searchQuery());
        if (request.specialtyName() != null) {
            predicate.and(qSpecialty.name.en.containsIgnoreCase(request.specialtyName())
                    .or(qSpecialty.name.uk.containsIgnoreCase(request.specialtyName())));
        }
        if (request.createdAt() != null) {
            predicate.and(qSector.createdAt.eq(request.createdAt()));
        }
        if (request.updatedAt() != null) {
            predicate.and(qSector.updatedAt.eq(request.updatedAt()));
        }

        JPAQuery<Sector> query = getQueryFactory().selectFrom(qSector)
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
}
