package com.backend.repository;

import com.backend.entity.AppUser;
import com.backend.entity.Fail;
import com.backend.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    /**
     * Finds the like containing the given user and fail.
     *
     * @param user - search user for the like
     * @param fail - search fail for the like
     * @return Like for the given user and fail
     */
    Like findLikeByUserAndFail(AppUser user, Fail fail);

}
