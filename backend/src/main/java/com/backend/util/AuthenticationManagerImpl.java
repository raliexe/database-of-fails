package com.backend.util;

import com.backend.entity.AppUser;
import com.backend.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthenticationManagerImpl implements AuthenticationManager {

    private final UserRepository userRepository;

    @Override
    public AppUser getAuthenticatedUser() {
        log.info("Getting currently authenticated User");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        if (name == null) {
            return null;
        }
        return userRepository.findAppUserByEmail(name);
    }

    @Override
    public boolean isAuthenticated() {
        log.info("Checking if User is authenticated");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        if (name == null) {
            return false;
        }
        AppUser user = userRepository.findAppUserByEmail(name);
        return user != null;
    }

    @Override
    public String getLoggedInUser() {
        log.info("Getting currently logged in User");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getPrincipal().toString();
        return email;
    }
}