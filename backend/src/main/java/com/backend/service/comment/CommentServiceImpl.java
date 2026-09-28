package com.backend.service.comment;

import com.backend.endpoint.dto.CommentWithParentDto;
import com.backend.entity.AppUser;
import com.backend.entity.Comment;
import com.backend.entity.Fail;
import com.backend.exception.NotFoundException;
import com.backend.exception.PersistenceException;
import com.backend.repository.CommentRepository;
import com.backend.repository.FailRepository;
import com.backend.repository.user.UserRepository;
import com.backend.service.fail.FailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final FailService failService;
    private final UserRepository userRepository;
    private final FailRepository failRepository;
    private final CommentRepository commentRepository;

    @Override
    public Comment addComment(CommentWithParentDto comment, AppUser user) {
        try {
            Fail fail = failRepository.findFailById(comment.getFailId());
            if (fail == null) {
                throw new NotFoundException("Comment does not have fail!");
            }

            Comment newComment = new Comment();
            newComment.setFail(fail);
            newComment.setUser(user);
            newComment.setMessage(comment.getMessage());
            newComment.setCreated(LocalDate.now());

            List<Comment> commentsByFail = fail.getComments();
            if (comment.getParentId() != null) {
                for (Comment com : commentsByFail) {
                    if (com.getId().equals(comment.getParentId())) {
                        com.getComments().add(newComment);
                        newComment.setParent(com);
                    }
                }
            } else {
                fail.getComments().add(newComment);
            }

            return commentRepository.save(newComment);
        } catch (DataAccessException e) {
            throw new PersistenceException("A server error occured", e);
        }
    }

    @Override
    public List<Comment> getCommentsByFailId(Long failId) {
        try {
            Fail fail = failService.getById(failId);
            if (fail == null) {
                throw new NotFoundException("Fail not found!");
            }
            if (fail.getComments() == null) {
                throw new NotFoundException("Comments of fail not found!");
            }
            return fail.getComments();
        } catch (DataAccessException e) {
            throw new PersistenceException("A server error occured", e);
        }
    }

}
