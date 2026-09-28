package com.backend.security;

import com.backend.configuration.SecurityProperties;
import com.backend.endpoint.dto.user.UserLoginDto;
import com.backend.exception.AccountLockedException;
import com.backend.exception.NotFoundException;
import com.backend.service.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenizer jwtTokenizer;
    private final UserService userService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, SecurityProperties securityProperties,
                                   JwtTokenizer jwtTokenizer, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenizer = jwtTokenizer;
        this.userService = userService;
        setFilterProcessesUrl(securityProperties.getLoginUri());
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
        throws AuthenticationException {
        UserLoginDto user = null;
        try {
            user = new ObjectMapper().readValue(request.getInputStream(), UserLoginDto.class);
            boolean isLocked = userService.findAppUserByEmail(user.getEmail()).getIsLocked();
            if (isLocked) {
                log.debug("AccountLockedException: Account locked!");
                throw new AccountLockedException("Account locked!");
            }
            //Compares the user with CustomUserDetailService#loadUserByUsername and check if the credentials are correct
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                user.getEmail(),
                user.getPassword()));
        } catch (NotFoundException e) {
            log.debug("BadCredentialsException: Bad Credentials!");
            throw new BadCredentialsException("Bad Credentials", e);
        } catch (IOException e) {
            log.debug("BadCredentialsException: Wrong API request or JSON schema!");
            throw new BadCredentialsException("Wrong API request or JSON schema", e);
        } catch (BadCredentialsException e) {
            if (user != null && user.getEmail() != null) {
                userService.incrementFailedAttempts(user.getEmail());
                log.debug("Unsuccessful authentication attempt for user {}", user.getEmail());
            }
            throw e;
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException {

        if (failed.getClass().equals(AccountLockedException.class)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        response.getWriter().write(failed.getMessage());
        log.debug("Invalid authentication attempt: {}", failed.getMessage());
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException {
        User user = ((User) authResult.getPrincipal());

        List<String> roles = user.getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());

        response.getWriter().write(jwtTokenizer.getAuthToken(user.getUsername(), roles));
        userService.unlockUser(user.getUsername());
        log.info("Successfully authenticated user {}", user.getUsername());
    }
}