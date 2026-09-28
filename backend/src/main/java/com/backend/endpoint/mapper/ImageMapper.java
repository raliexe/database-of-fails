package com.backend.endpoint.mapper;

import com.backend.endpoint.dto.ImageDto;
import com.backend.entity.FailImage;
import com.backend.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
@Mapper
@Slf4j
public class ImageMapper {

    public FailImage multipartFileToImage(MultipartFile multipartImage) {
        if (multipartImage == null) {
            return null;
        }
        byte[] content;
        try {
            content = multipartImage.getBytes();
        } catch (IOException e) {
            log.debug("ValidationException: Failed getting the bytes of the file");
            throw new ValidationException("Failed getting the bytes of the file", e);
        }
        String contentType = multipartImage.getContentType();
        if (contentType == null || (!contentType.equals("image/jpeg") && !contentType.equals("image/png"))) {
            log.debug("ValidationException: Unwanted contentType " + multipartImage.getContentType());
            throw new ValidationException("Unwanted contentType " + multipartImage.getContentType());
        }
        return new FailImage("", multipartImage.getOriginalFilename(), contentType, content);
    }

    public ImageDto imageEntityToDto(FailImage image) {
        if (image == null) {
            return null;
        }
        return new ImageDto(image.getId(), image.getImageName(), image.getFileName(),
                image.getContentType(), image.getContent(), image.getIsMain());
    }
}
