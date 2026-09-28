package com.backend.endpoint;

import com.backend.endpoint.dto.CommentDto;
import com.backend.endpoint.dto.CommentTreeDto;
import com.backend.endpoint.dto.CommentWithParentDto;
import com.backend.endpoint.mapper.CommentMapper;
import com.backend.entity.AppUser;
import com.backend.entity.Comment;
import com.backend.service.comment.CommentService;
import com.backend.service.user.UserService;
import com.backend.util.AuthenticationManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/comments")
public class CommentEndpoint {

    private final CommentService commentService;
    private final CommentMapper commentMapper;

    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    @Secured("ROLE_USER")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping()
    public ResponseEntity<CommentDto> create(@Valid @RequestBody CommentWithParentDto commentDto) {
        String email = authenticationManager.getLoggedInUser();
        AppUser user = userService.findAppUserByEmail(email);

        Comment addedComment = commentService.addComment(commentDto, user);

        return ResponseEntity.status(HttpStatus.OK)
                .body(commentMapper.entityToCommentDto(addedComment));
    }

    @PermitAll
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{id}")
    public ResponseEntity<List<CommentTreeDto>> getCommentsByFailId(@PathVariable Long id) {
        List<Comment> comments = commentService.getCommentsByFailId(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(commentMapper.entityToTreeListDto(comments));
    }

}
