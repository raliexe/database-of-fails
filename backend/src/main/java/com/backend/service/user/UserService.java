package com.backend.service.user;

import com.backend.endpoint.dto.user.ResetPasswordDto;
import com.backend.endpoint.dto.user.UserFilterDto;
import com.backend.entity.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService extends UserDetailsService {

    /**
     * Find a user in the context of Spring Security based on the email address
     * <br>
     * For more information have a look at this tutorial:
     * https://www.baeldung.com/spring-security-authentication-with-a-database
     *
     * @param email the email address
     * @return a Spring Security user
     * @throws UsernameNotFoundException is thrown if the specified user does not exists
     */
    @Override
    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;

    /**
     * Find an application user based on the email address.
     *
     * @param email the email address
     * @return an application user
     */
    AppUser findAppUserByEmail(String email);

    AppUser addAppUser(AppUser applicationUser);

    AppUser findUserById(Long id);

    AppUser updateUser(AppUser applicationUser, Long id);

    void deleteUserById(Long id);

    Page<AppUser> getUnLockedUsers(UserFilterDto userFilterDto);

    void resetPassword(ResetPasswordDto resetPasswordDto);

    void incrementFailedAttempts(String email);

    void unlockUser(String email);

    void changeLockStatus(Long id);
}
