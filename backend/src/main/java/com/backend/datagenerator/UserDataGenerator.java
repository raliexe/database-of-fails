package com.backend.datagenerator;

import com.backend.entity.AppUser;
import com.backend.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Profile("generateData")
@Component
@Slf4j
@RequiredArgsConstructor
public class UserDataGenerator {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    @DependsOn("DefaultAdminGenerator")
    private void generateUsers() {
        if (userRepository.findAll().size() == 0) {
            log.debug("Generating users...");
            final AppUser user = new AppUser("User", "user@gmail.com", passwordEncoder.encode("password"), false);
            final AppUser admin = new AppUser("Admin", "admin@gmail.com", passwordEncoder.encode("password"), true);
            log.debug("Persisting generated users in database...");
            userRepository.save(user);
            userRepository.save(admin);
            log.debug("Users inserted in database successfully");
        }
    }
}