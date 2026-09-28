package com.backend.service.fail;

import com.backend.endpoint.dto.FailDto;
import com.backend.endpoint.dto.FailFilterDto;
import com.backend.endpoint.mapper.FailMapper;
import com.backend.entity.AppUser;
import com.backend.entity.Fail;
import com.backend.entity.FailImage;
import com.backend.entity.Pdf;
import com.backend.exception.AlreadyExistsException;
import com.backend.exception.NotFoundException;
import com.backend.exception.PersistenceException;
import com.backend.exception.UnauthorizedException;
import com.backend.repository.FailRepository;
import com.backend.repository.ImageRepository;
import com.backend.service.pdf.PdfService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FailServiceImpl implements FailService {

    private final FailRepository failRepository;
    private final ImageRepository imageRepository;
    private final PdfService pdfService;
    private final FailMapper failMapper;

    @Override
    public FailDto addFail(FailDto failDto, FailImage image, AppUser user) {
        try {
            Fail fail = failMapper.failDtoToEntity(failDto);
            fail.setUser(user);

            image.setIsMain(true);
            image.setFail(fail);
            fail.getImages().add(image);

            if (failRepository.findFailByName(fail.getName()) != null) {
                throw new AlreadyExistsException("Fail with that name already exists!");
            }

            fail = failRepository.save(fail);

            Pdf pdf = pdfService.createPdfForFail(fail);
            fail.setPdf(pdf);

            return failMapper.entityToFailDto(fail, true, user);
        } catch (DataAccessException e) {
            throw new PersistenceException("A server error occured", e);
        }
    }

    @Override
    public FailDto updateFail(FailDto failDto, FailImage image,
                              List<FailImage> images, Long[] deletedImages, AppUser user) {
        try {
            Fail currFail = failRepository.findFailById(failDto.getId());
            if (currFail == null) {
                throw new NotFoundException("Fail not found!");
            }
            if (! user.getIsAdmin() && ! currFail.getUser().getId().equals(user.getId())) {
                throw new UnauthorizedException("Unauthorized operation!");
            }

            Fail duplicateFail = failRepository.findFailByName(failDto.getName());
            if (duplicateFail != null && ! duplicateFail.getId().equals(failDto.getId())) {
                throw new AlreadyExistsException("Fail with that name already exists!");
            }

            currFail.setName(failDto.getName());
            currFail.setDescription(failDto.getDescription());
            currFail.setDate(failDto.getDate());

            /*List<Image> images;
            if (currFail.getImages() != null && currFail.getImages().size() > 0
                    && updatedFail.getImages() != null && updatedFail.getImages().size() > 0) {
                images = new ArrayList<>();
                currFail.setImages(images);
                for (Image updatedImage : updatedFail.getImages()) {
                    Image imgToAdd = null;
                    for (Image currImage : currFail.getImages()) {
                        if (currImage.getImageName().equals(updatedImage.getImageName())
                                && currImage.getFileName().equals(updatedImage.getFileName())
                                && currImage.getContentType().equals(updatedImage.getContentType())
                                && Arrays.equals(currImage.getContent(), updatedImage.getContent())
                                && currImage.getIsMain() == updatedImage.getIsMain()) {
                            imgToAdd = currImage;
                            break;
                        }
                    }
                    if (imgToAdd != null) {
                        imgToAdd.setFail(currFail);
                        currFail.getImages().add(imgToAdd);
                    } else {
                        updatedImage.setFail(currFail);
                        currFail.getImages().add(updatedImage);
                    }
                }
            }*/

            if (image != null) {
                for (FailImage img : currFail.getImages()) {
                    if (img.getIsMain()) {
                        currFail.getImages().remove(img);
                        imageRepository.delete(img);
                        break;
                    }
                }

                image.setIsMain(true);
                image.setFail(currFail);
                currFail.getImages().add(image);
            }

            if (images != null) {
                for (FailImage img : images) {
                    img.setIsMain(false);
                    img.setFail(currFail);
                    currFail.getImages().add(img);
                }
            }

            if (deletedImages != null) {
                for (Long delId : deletedImages) {
                    FailImage delImage = currFail.getImages().stream()
                            .filter(i -> i.getId().equals(delId)).findFirst().orElse(null);
                    if (delImage != null) {
                        currFail.getImages().remove(delImage);
                        imageRepository.delete(delImage);
                    }
                }
            }

            currFail = failRepository.save(currFail);

            return failMapper.entityToFailDto(currFail, true, user);

        } catch (DataAccessException e) {
            throw new PersistenceException("A server error occured", e);
        }
    }

    @Override
    public Fail getById(Long id) {
        try {
            Fail fail = failRepository.findFailById(id);
            if (fail == null) {
                throw new NotFoundException("Fail not found!");
            }
            return fail;
        } catch (DataAccessException e) {
            throw new PersistenceException("A server error occured", e);
        }
    }

    @Override
    public List<Fail> getByCurrentUser(AppUser user) {
        try {
            return failRepository.findFailsByUser(user);
        } catch (DataAccessException e) {
            throw new PersistenceException("A server error occured", e);
        }
    }

    @Override
    public List<Fail> getAll() {
        try {
            return failRepository.findAll();
        } catch (DataAccessException e) {
            throw new PersistenceException("A server error occured", e);
        }
    }

    @Override
    @Transactional
    public void deleteFailById(Long id, AppUser user) {
        try {
            Fail fail = failRepository.findFailById(id);
            if (fail == null) {
                throw new NotFoundException("Fail not found!");
            }
            if (! user.getIsAdmin() && ! fail.getUser().getId().equals(user.getId())) {
                throw new UnauthorizedException("Unauthorized operation!");
            }
            failRepository.deleteById(id);
        } catch (DataAccessException e) {
            throw new PersistenceException("A server error occured", e);
        }
    }

    @Override
    public Page<Fail> getAllMatchingFails(FailFilterDto failFilterDto) {
        try {
            Page<Fail> fails = failRepository.findAllMatchingFails(failFilterDto);
            return fails;
        } catch (DataAccessException e) {
            log.debug("DataAccessException occured", e);
            throw new PersistenceException("A server error occured", e);
        }
    }

}
