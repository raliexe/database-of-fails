package com.backend.service.image;

import com.backend.entity.FailImage;
import com.backend.exception.PersistenceException;
import com.backend.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    @Autowired
    private final ImageRepository imageRepository;

    @Override
    public FailImage addImage(FailImage image) {
        try {
            log.info("Adding new Image {}", image);
            if (image == null) {
                return null;
            }
            return imageRepository.save(image);
        } catch (DataAccessException e) {
            log.debug("DataAccessException occured", e);
            throw new PersistenceException("A server error occured", e);
        }
    }

}
