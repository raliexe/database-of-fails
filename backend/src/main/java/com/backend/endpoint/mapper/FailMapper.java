package com.backend.endpoint.mapper;

import com.backend.endpoint.dto.FailDto;
import com.backend.endpoint.dto.ImageDto;
import com.backend.entity.AppUser;
import com.backend.entity.Fail;
import com.backend.entity.FailImage;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Mapper
public class FailMapper {

    public Fail failDtoToEntity(FailDto failDto) {
        if (failDto == null) {
            return null;
        }
        return new Fail(failDto.getName(), failDto.getDescription(), failDto.getDate());
    }

    public FailDto entityToFailDto(Fail fail, boolean allImages, AppUser user) {
        if (fail == null) {
            return null;
        }
        ImageDto mainImage = null;
        List<ImageDto> images = null;
        if (fail.getImages() != null && fail.getImages().size() > 0) {
            images = new ArrayList<>();
            for (FailImage image : fail.getImages()) {
                ImageDto img = new ImageDto(image.getId(), image.getImageName(),
                        image.getFileName(), image.getContentType(), image.getContent(),
                        image.getIsMain());
                if (image.getIsMain()) {
                    mainImage = img;
                } else if (allImages) {
                    images.add(img);
                }
            }
        }
        boolean isLikedByUser = user != null &&
                fail.getLikes().stream().
                    anyMatch(l -> l.getUser().getId().equals(user.getId()));
        return new FailDto(fail.getId(), fail.getName(), fail.getDescription(), fail.getDate(),
                mainImage, images, fail.getLikes().size(), fail.getComments().size(), isLikedByUser);
    }

}
