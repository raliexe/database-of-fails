package com.backend.endpoint.dto;

import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Builder
public class CommentWithParentDto {

    private String message;
    private Long parentId;
    private Long failId;

}
