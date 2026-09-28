package com.backend.service.comment;

import com.backend.endpoint.dto.CommentWithParentDto;
import com.backend.entity.AppUser;
import com.backend.entity.Comment;

import java.util.List;

public interface CommentService {

    /**
     * Creates a comment.
     *
     * @param comment - comment to be created
     * @return the created comment
     */
    Comment addComment(CommentWithParentDto comment, AppUser user);

    /**
     * Get comments for fail with given id.
     *
     * @param failId - id of fail
     * @return the comments of the fail
     */
    List<Comment> getCommentsByFailId(Long failId);

}
