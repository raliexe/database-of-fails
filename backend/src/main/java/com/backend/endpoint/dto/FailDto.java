package com.backend.endpoint.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Builder
public class FailDto {

    private Long id;
    private String name;
    private String description;
    private LocalDate date;
    private ImageDto mainImage;
    private List<ImageDto> images;
    private int likes;
    private int comments;
    private boolean likedByUser;

    public FailDto(Long id, String name, String description, LocalDate date) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.date = date;
        this.likes = 0;
        this.comments = 0;
    }
}
