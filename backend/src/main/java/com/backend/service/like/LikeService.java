package com.backend.service.like;

import com.backend.entity.AppUser;
import com.backend.entity.Fail;

public interface LikeService {

    /**
     * Increases likes of fail with the given id.
     *
     * @param failId - id of fail
     * @param user - id of currently logged user
     */
    Fail likeFail(Long failId, AppUser user);

    /**
     * Decreases likes of fail with the given id.
     *
     * @param failId - id of fail
     */
    Fail unlikeFail(Long failId, AppUser user);

    /**
     * Checks if user liked fail.
     *
     * @param failId - id of fail
     * @param currUserId - id of currently logged user
     * @return the created image
     */
    boolean getIsFailLikedByUser(Long failId, Long currUserId);

}
