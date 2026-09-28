package com.backend.util;

import com.backend.entity.AppUser;

public interface AuthenticationManager {

    /**
     * Get the user who is authenticated in the website.
     *
     * @return the user who is currently in the system or null if there is no such user
     */
    AppUser getAuthenticatedUser();

    /**
     * Indicates if there is an authenticated user in this session.
     *
     * @return true if the user is authenticated, and false otherwise
     */
    boolean isAuthenticated();

    /**
     * Get the user who is authenticated in the website.
     *
     * @return the user who is currently in the system or null if there is no such user
     */
    String getLoggedInUser();
}