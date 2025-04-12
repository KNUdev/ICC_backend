package ua.knu.knudev.icccommon.config;

import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;

@Component
public class QEntityManagerUtil {

    @PersistenceContext
    private EntityManager entityManager;

    private static JPAQueryFactory queryFactory;

    @PostConstruct
    private void init() {
        queryFactory = new JPAQueryFactory(JPQLTemplates.DEFAULT, entityManager);
    }

    public static JPAQueryFactory getQueryFactory() {
        return queryFactory;
    }

}

