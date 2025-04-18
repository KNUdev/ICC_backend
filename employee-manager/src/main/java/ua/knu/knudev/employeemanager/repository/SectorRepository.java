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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

import static ua.knu.knudev.icccommon.config.QEntityManagerUtil.getQueryFactory;

public interface SectorRepository extends JpaRepository<Sector, UUID> {

    QSector qSector = QSector.sector;
    QSpecialty qSpecialty = QSpecialty.specialty;

    default Page<Sector> findAllBySearchQuery(
            Pageable pageable, String searchQuery, String specialtyName, LocalDateTime createdAt, LocalDateTime updatedAt
    ) {
        BooleanBuilder predicate = new BooleanBuilder();

        if (StringUtils.isNotBlank(searchQuery)) {
            Arrays.stream(searchQuery.split("\\s+"))
                    .map(word ->
                            qSector.name.en.containsIgnoreCase(word)
                                    .or(qSector.name.uk.containsIgnoreCase(word))
                    )
                    .reduce(BooleanExpression::and)
                    .ifPresent(predicate::and);
        }
        if (specialtyName != null) {
            predicate.and(qSpecialty.name.en.containsIgnoreCase(specialtyName)
                    .or(qSpecialty.name.uk.containsIgnoreCase(specialtyName)));
        }
        if (createdAt != null) {
            predicate.and(qSector.createdAt.eq(createdAt));
        }
        if (updatedAt != null) {
            predicate.and(qSector.updatedAt.eq(updatedAt));
        }

        JPAQuery<Sector> query = getQueryFactory().selectFrom(qSector)
                .where(predicate)
                .orderBy(qSector.name.en.asc(), qSector.name.uk.asc())
                .offset(pageable.isUnpaged() ? 0 : pageable.getOffset())
                .limit(pageable.isUnpaged() ? Integer.MAX_VALUE : pageable.getPageSize());

        return PageableExecutionUtils.getPage(query.fetch(), pageable, query::fetchCount);
    }
}
