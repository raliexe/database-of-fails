package com.backend.repository;

import com.backend.endpoint.dto.FailFilterDto;
import com.backend.entity.Fail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
public class FailCustomRepositoryImpl implements FailCustomRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<Fail> findAllMatchingFails(FailFilterDto failFilterDto) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Fail> query = criteriaBuilder.createQuery(Fail.class);
        Root<Fail> root = query.from(Fail.class);

        List<Predicate> predicates = new ArrayList<>();
        if (failFilterDto.getName() != null && !failFilterDto.getName().isEmpty() && !failFilterDto.getName().isBlank()) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + failFilterDto.getName().toLowerCase() + "%"));
        }

        if (!predicates.isEmpty()) {
            query.where(criteriaBuilder.or(predicates.toArray(new Predicate[0])));
        } else {
            query.where(predicates.toArray(new Predicate[0]));
        }

        TypedQuery<Fail> typedQuery = em.createQuery(query);
        typedQuery.setFirstResult(failFilterDto.getPage() * failFilterDto.getSize());
        typedQuery.setMaxResults(failFilterDto.getSize());
        List<Fail> resultList = typedQuery.getResultList();

        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Fail> failRootCount = countQuery.from(Fail.class);
        countQuery.select(criteriaBuilder.countDistinct(failRootCount))
            .where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
        Long count = em.createQuery(countQuery).getSingleResult();

        Pageable pageable = PageRequest.of(failFilterDto.getPage(), failFilterDto.getSize());
        return new PageImpl<>(resultList, pageable, count);
    }

}