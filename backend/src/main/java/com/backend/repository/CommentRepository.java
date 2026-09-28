package com.backend.repository;

import com.backend.entity.AppUser;
import com.backend.entity.Comment;
import com.backend.entity.Fail;
import com.backend.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * Finds the comment containing the given fail.
     *
     * @param fail - search fail for the comment
     * @return Comment for the given fail
     */
    List<Comment> findCommentsByFail(Fail fail);

    /**
     * Finds the comment containing the given user and fail.
     *
     * @param user - search user for the comment
     * @param fail - search fail for the comment
     * @return Comment for the given user and fail
     */
    List<Comment> findCommentsByUserAndFail(AppUser user, Fail fail);

}
