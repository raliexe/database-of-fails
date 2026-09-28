package com.backend.repository.user;

import com.backend.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long>, JpaSpecificationExecutor<AppUser>, UserCustomRepository {

    /**
     * Finds user with the given email.
     *
     * @param email - search criteria
     * @return user with the given email
     */
    AppUser findAppUserByEmail(String email);

    /**
     * Find a user by id.
     *
     * @param id - id of the searched user
     * @return user with the given id
     */
    AppUser findAppUserById(Long id);

    /**
     * Finds user by given token.
     *
     * @param token - token of the searched user
     * @return user with the given token
     */
    AppUser findAppUserByToken(String token);
}