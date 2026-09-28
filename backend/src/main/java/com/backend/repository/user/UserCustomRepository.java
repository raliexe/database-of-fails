package com.backend.repository.user;

import com.backend.endpoint.dto.user.UserFilterDto;
import com.backend.entity.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCustomRepository {

    /**
     * Finds all users matching the given criteria except the logged-in user.
     *
     * @param userFilterDto   - filter criteria
     * @param loggedUserEmail - email of the logged-in user
     * @return Page with the find users
     */
    Page<AppUser> findAllMatchingUsers(UserFilterDto userFilterDto, String loggedUserEmail);
}