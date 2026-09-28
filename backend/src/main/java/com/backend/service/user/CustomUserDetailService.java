package com.backend.service.user;

import com.backend.endpoint.dto.user.ResetPasswordDto;
import com.backend.endpoint.dto.user.UserFilterDto;
import com.backend.entity.AppUser;
import com.backend.exception.AlreadyExistsException;
import com.backend.exception.NotFoundException;
import com.backend.exception.PersistenceException;
import com.backend.exception.ValidationException;
import com.backend.repository.user.UserRepository;
import com.backend.service.image.ImageService;
import com.backend.util.AuthenticationManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomUserDetailService implements UserService {

    private final UserRepository userRepository;
    //private final OrderRepository orderRepository;
    private final ImageService imageService;

    //private final OrderService orderService;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            log.info("Load user by email {}", email);
            try {
                AppUser appUser = findAppUserByEmail(email);

                List<GrantedAuthority> grantedAuthorities;
                if (appUser.getIsAdmin()) {
                    grantedAuthorities = AuthorityUtils.createAuthorityList("ROLE_ADMIN", "ROLE_USER");
                } else {
                    grantedAuthorities = AuthorityUtils.createAuthorityList("ROLE_USER");
                }
                return new User(appUser.getEmail(), appUser.getPassword(), grantedAuthorities);
            } catch (NotFoundException e) {
                log.debug("UsernameNotFoundException: " + e.getMessage());
                throw new UsernameNotFoundException(e.getMessage(), e);
            }
        } catch (DataAccessException e) {
            log.debug("DataAccessException occured", e);
            throw new PersistenceException("A server error occured", e);
        }
    }

    @Override
    public AppUser findAppUserByEmail(String email) {
        try {
            log.debug("Find application user by email {}", email);
            AppUser appUser = userRepository.findAppUserByEmail(email);
            if (appUser != null) {
                return appUser;
            }
            log.debug("NotFoundException: Could not find the user with the email address: " + email);
            throw new NotFoundException(String.format("Could not find the user with the email address %s", email));
        } catch (DataAccessException e) {
            log.debug("DataAccessException occured", e);
            throw new PersistenceException("A server error occured", e);
        }
    }

    @Override
    @Transactional(rollbackFor = PersistenceException.class)
    public AppUser addAppUser(AppUser appUser) {
        try {
            log.info("Adding User {}", appUser);
            if (userRepository.findAppUserByEmail(appUser.getEmail()) != null) {
                log.debug("AlreadyExistsException: User already exists!");
                throw new AlreadyExistsException("User already exists!");
            }
            return userRepository.save(appUser);
        } catch (DataAccessException e) {
            log.debug("DataAccessException occured", e);
            throw new PersistenceException("A server error occured", e);
        }
    }

    @Override
    public AppUser findUserById(Long id) {
        try {
            log.info("Find application user by id {}", id);
            AppUser appUser = userRepository.findAppUserById(id);
            if (appUser != null) {
                return appUser;
            }
            log.debug("NotFoundException: Could not find the user with id " + id);
            throw new NotFoundException("Could not find requested User!");
        } catch (DataAccessException e) {
            log.debug("DataAccessException occured", e);
            throw new PersistenceException("A server error occured", e);
        }
    }

    @Override
    @Transactional(rollbackFor = PersistenceException.class)
    public AppUser updateUser(AppUser userUpdates, Long id) {
        try {
            log.debug("Update user with id {} to {}", id, userUpdates);
            AppUser currentUser = userRepository.findAppUserById(id);
            if (currentUser == null) {
                log.debug("NotFoundException: User not found!");
                throw new NotFoundException("User not found!");
            }
            if (!isEmailUnique(userUpdates, id)) {
                log.debug("AlreadyExistsException: Email already exists!");
                throw new AlreadyExistsException("Email already exists!");
            }
            if (userUpdates.getPassword() != null && !userUpdates.getPassword().isEmpty() && !userUpdates.getPassword().isBlank()) {
                currentUser.setPassword(passwordEncoder.encode(userUpdates.getPassword()));
            }
            currentUser.setNickname(userUpdates.getNickname());
            return currentUser;
        } catch (DataAccessException e) {
            log.debug("DataAccessException occured", e);
            throw new PersistenceException("A server error occured", e);
        }
    }

    @Override
    @Transactional(rollbackFor = PersistenceException.class)
    public void deleteUserById(Long id) {
        try {
            log.info("Deleting application User by id: {}", id);
            if (userRepository.findAppUserById(id) == null) {
                log.debug("NotFoundException: User not found!");
                throw new NotFoundException("User not found!");
            }
            // Set user to null in the orders
            /*List<Order> orders = orderRepository.getOrderByUser(findUserById(id));
            for (Order order : orders) {
                if (order.getOrderStatus().equals(OrderStatus.CART)) {
                    order.setOrderStatus(OrderStatus.CANCELLED);
                    orderService.updateOrder(order);
                }
                order.setUser(null);
            }*/
            userRepository.deleteById(id);
        } catch (DataAccessException e) {
            log.debug("DataAccessException occured", e);
            throw new PersistenceException("A server error occured", e);
        }
    }

    @Override
    @Transactional(rollbackFor = PersistenceException.class)
    public void resetPassword(ResetPasswordDto resetPasswordDto) {
        try {
            log.info("Resetting password");
            AppUser user = userRepository.findAppUserByToken(resetPasswordDto.getToken());
            if (user == null) {
                log.debug("ValidationException: Token not valid!");
                throw new ValidationException("Token not valid!");
            }
            if (!resetPasswordDto.getPasswordConfirmation().equals(resetPasswordDto.getPassword())) {
                log.debug("ValidationException: Passwords do not match!");
                throw new ValidationException("Passwords do not match!");
            }
            if (resetPasswordDto.getPassword().length() > 32) {
                log.debug("ValidationException: Password too long!");
                throw new ValidationException("Password can only be 32 characters long!");
            }
            user.setPassword(passwordEncoder.encode(resetPasswordDto.getPassword()));
            if (user.getIsAdmin() && user.getIsLocked()) {
                user.setIsLocked(false);
            }
            user.setToken(null);
        } catch (DataAccessException e) {
            log.debug("DataAccessException occured", e);
            throw new PersistenceException("A server error occured", e);
        }
    }

    @Override
    public Page<AppUser> getUnLockedUsers(UserFilterDto userFilterDto) {
        try {
            log.info("Getting unlocked Users with filter {}", userFilterDto);
            String loggedUserEmail = authenticationManager.getLoggedInUser();
            return userRepository.findAllMatchingUsers(userFilterDto, loggedUserEmail);
        } catch (DataAccessException e) {
            log.debug("DataAccessException occured", e);
            throw new PersistenceException("A server error occured", e);
        }
    }

    @Override
    @Transactional(rollbackFor = PersistenceException.class)
    public void incrementFailedAttempts(String email) {
        try {
            AppUser user = userRepository.findAppUserByEmail(email);
            user.incrementFailedAttempts();
            if (user.getFailedAttempts() >= 5) {
                user.setIsLocked(true);
            }
        } catch (DataAccessException e) {
            log.debug("DataAccessException occured", e);
            throw new PersistenceException("A server error occured", e);
        }
    }

    @Override
    @Transactional(rollbackFor = PersistenceException.class)
    public void unlockUser(String email) {
        try {
            AppUser user = userRepository.findAppUserByEmail(email);
            user.setIsLocked(false);
            user.setFailedAttempts(0);
        } catch (DataAccessException e) {
            log.debug("DataAccessException occured", e);
            throw new PersistenceException("A server error occured", e);
        }
    }

    @Override
    @Transactional(rollbackFor = PersistenceException.class)
    public void changeLockStatus(Long id) {
        try {
            String loggedUserEmail = authenticationManager.getLoggedInUser();
            AppUser loggedUser = userRepository.findAppUserByEmail(loggedUserEmail);
            if (loggedUser.getId().equals(id)) {
                log.debug("ValidationException: User cannot lock/unlock self!");
                throw new ValidationException("User cannot lock/unlock self!");
            }
            AppUser user = userRepository.findAppUserById(id);
            if (user == null) {
                log.debug("NotFoundException: User not found!");
                throw new NotFoundException("User not found!");
            }
            if (user.getIsLocked()) {
                user.setIsLocked(false);
                user.setFailedAttempts(0);
            } else {
                user.setIsLocked(true);
            }
            userRepository.save(user);
        } catch (DataAccessException e) {
            log.debug("DataAccessException occured", e);
            throw new PersistenceException("A server error occured", e);
        }
    }

    private boolean isEmailUnique(AppUser appUser, Long id) {
        log.info("Checking if email {} is unique", appUser.getEmail());
        AppUser applicationUserByEmail = userRepository.findAppUserByEmail(appUser.getEmail());
        if (applicationUserByEmail == null) {
            return true;
        }
        return Objects.equals(id, applicationUserByEmail.getId());
    }
}