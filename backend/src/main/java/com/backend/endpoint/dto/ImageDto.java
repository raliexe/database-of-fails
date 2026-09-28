package com.backend.endpoint.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageDto {

    private Long id;
    private String imageName;
    private String fileName;
    private String contentType;
    private byte[] content;
    private Boolean isMain;

    @Override
    public String toString() {
        return "ImageDto{"
                + "id=" + id
                + ", imageName='" + imageName + '\''
                + ", contentType='" + contentType + '\''
                + '}';
    }

}
