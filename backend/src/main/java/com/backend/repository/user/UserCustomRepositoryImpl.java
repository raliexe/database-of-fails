package com.backend.repository.user;

import com.backend.endpoint.dto.user.UserFilterDto;
import com.backend.entity.AppUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequiredArgsConstructor
@Slf4j
public class UserCustomRepositoryImpl implements UserCustomRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<AppUser> findAllMatchingUsers(UserFilterDto userFilterDto, String loggedUserEmail) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<AppUser> query = criteriaBuilder.createQuery(AppUser.class);
        Root<AppUser> root = query.from(AppUser.class);
        List<Predicate> predicates = new ArrayList<>();

        predicates.add(criteriaBuilder.notEqual(root.get("email"), loggedUserEmail));
        if (userFilterDto.getEmail() != null && !userFilterDto.getEmail().isEmpty() && !userFilterDto.getEmail().isBlank()) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + userFilterDto.getEmail().toLowerCase() + "%"));
        }
        if (userFilterDto.getIsLocked() != null) {
            if (userFilterDto.getIsLocked()) {
                predicates.add(criteriaBuilder.isTrue(root.get("isLocked")));
            } else {
                predicates.add(criteriaBuilder.isFalse(root.get("isLocked")));
            }
        }
        log.info("Executing createSpecification() finished");

        query.where(predicates.toArray(new Predicate[0]));
        TypedQuery<AppUser> typedQuery = em.createQuery(query);
        typedQuery.setFirstResult(userFilterDto.getPage() * userFilterDto.getSize());
        typedQuery.setMaxResults(userFilterDto.getSize());
        List<AppUser> resultList = typedQuery.getResultList();

        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<AppUser> userRootCount = countQuery.from(AppUser.class);
        countQuery.select(criteriaBuilder.countDistinct(userRootCount))
            .where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
        Long count = em.createQuery(countQuery).getSingleResult();

        Pageable pageable = PageRequest.of(userFilterDto.getPage(), userFilterDto.getSize());
        return new PageImpl<>(resultList, pageable, count);
    }
}