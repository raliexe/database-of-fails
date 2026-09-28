package com.backend.repository;

import com.backend.entity.FailImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<FailImage, Long> {

    /**
     * Find an image by id.
     *
     * @param id - id of the searched image
     * @return image with the given id
     */
    FailImage findImageById(Long id);

}