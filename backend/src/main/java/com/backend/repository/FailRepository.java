package com.backend.repository;

import com.backend.entity.AppUser;
import com.backend.entity.Fail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FailRepository extends JpaRepository<Fail, Long>, FailCustomRepository {

    /**
     * Finds fail with the given id.
     *
     * @param id - search criteria
     * @return fail with the given id
     */
    Fail findFailById(Long id);

    /**
     * Finds fail with the given name.
     *
     * @param name - search criteria
     * @return fail with the given name
     */
    Fail findFailByName(String name);

    /**
     * Finds fail belonging to the given user.
     *
     * @param user - search criteria
     * @return fail elonging to the given user
     */
    List<Fail> findFailsByUser(AppUser user);

}
