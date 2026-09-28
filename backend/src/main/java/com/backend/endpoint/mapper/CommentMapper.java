package com.backend.endpoint.mapper;

import com.backend.endpoint.dto.CommentDto;
import com.backend.endpoint.dto.CommentTreeDto;
import com.backend.entity.Comment;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Mapper
public class CommentMapper {

    public CommentDto entityToCommentDto(Comment comment) {
        if (comment == null) {
            return null;
        }
        return new CommentDto(comment.getId(), comment.getMessage(),
                comment.getUser().getNickname(), comment.getCreated());
    }

    public List<CommentTreeDto> entityToTreeListDto(List<Comment> comments) {
        if (comments == null) {
            return new ArrayList<>();
        }
        List<CommentTreeDto> commentTreeDtos = new ArrayList<>();
        for (Comment comment : comments) {
            if (comment.getParent() == null) {
                CommentTreeDto commentTreeDto = entityToTreeDto(comment);
                commentTreeDtos.add(commentTreeDto);
            }
        }
        return commentTreeDtos;
    }

    public CommentTreeDto entityToTreeDto(Comment comment) {
        CommentTreeDto tree = new CommentTreeDto();
        tree.setId(comment.getId());
        tree.setMessage(comment.getMessage());
        tree.setUserName(comment.getUser().getNickname());
        tree.setCreated(comment.getCreated());
        if (comment.getComments() != null && comment.getComments().size() > 0) {
            tree.setChildren(new ArrayList<>());
            for (Comment child : comment.getComments()) {
                tree.getChildren().add(entityToTreeDto(child));
            }
            tree.getChildren().sort(new CommentComparator());
        }
        return tree;
    }

}

class CommentComparator implements java.util.Comparator<CommentTreeDto> {
    @Override
    public int compare(CommentTreeDto a, CommentTreeDto b) {
        return a.getId().compareTo(b.getId());
    }
}