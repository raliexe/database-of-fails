package com.backend.endpoint.dto;

import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Builder
public class CommentDto {

    private Long id;
    private String message;
    private String userName;
    private LocalDate created;

}
