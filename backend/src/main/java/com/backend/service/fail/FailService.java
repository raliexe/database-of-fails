package com.backend.service.fail;

import com.backend.endpoint.dto.FailDto;
import com.backend.endpoint.dto.FailFilterDto;
import com.backend.entity.AppUser;
import com.backend.entity.Fail;
import com.backend.entity.FailImage;
import org.springframework.data.domain.Page;

import java.util.List;

public interface FailService {

    /**
     * Creates a fail.
     *
     * @param failDto - fail to be created
     * @return the created fail
     */
    FailDto addFail(FailDto failDto, FailImage image, AppUser user);

    /**
     * Updates a fail.
     *
     * @param failDto - fail to be updated
     * @return the updated fail
     */
    FailDto updateFail(FailDto failDto, FailImage image, List<FailImage> images,
                       Long[] deletedImages, AppUser user);

    /**
     * Find a single fail by id.
     *
     * @param id id of fail
     * @return the searched fail
     */
    Fail getById(Long id);

    /**
     * Gets all fails by current user.
     *
     * @param user id of fail
     * @return all fails by current user
     */
    List<Fail> getByCurrentUser(AppUser user);

    /**
     * Gets all fails.
     *
     * @return all fails
     */
    List<Fail> getAll();

    /**
     * Deletes fail with the given id.
     *
     * @param id - id of fail
     */
    void deleteFailById(Long id, AppUser user);

    /**
     * Finds fails according to the given filter.
     *
     * @param failFilterDto - filter
     * @return Page of fails which match the filter
     */
    Page<Fail> getAllMatchingFails(FailFilterDto failFilterDto);

}
