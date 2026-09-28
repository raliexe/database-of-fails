package com.backend.service.image;

import com.backend.entity.FailImage;

public interface ImageService {

    /**
     * Creates an image.
     *
     * @param image - image to be created
     * @return the created image
     */
    FailImage addImage(FailImage image);

}
