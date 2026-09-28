package com.backend.endpoint.dto;

import com.backend.entity.Comment;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Builder
public class CommentTreeDto {

    private Long id;
    private String message;
    private String userName;
    private LocalDate created;
    private List<CommentTreeDto> children;

}
