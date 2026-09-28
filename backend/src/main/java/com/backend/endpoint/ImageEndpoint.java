package com.backend.endpoint;

import com.backend.endpoint.dto.ImageDto;
import com.backend.endpoint.mapper.ImageMapper;
import com.backend.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.PermitAll;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/images")
public class ImageEndpoint {

    private final ImageService imageService;
    private final ImageMapper imageMapper;

    @PermitAll
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ImageDto create(@RequestParam("image") MultipartFile multipartImage) {
        log.info("POST /api/v1/images body: {}", multipartImage);
        ImageDto addedImage = imageMapper.imageEntityToDto(imageService.addImage(imageMapper.multipartFileToImage(multipartImage)));
        log.info("POST /api/v1/images added Image: {}", addedImage);
        return addedImage;
    }

}
