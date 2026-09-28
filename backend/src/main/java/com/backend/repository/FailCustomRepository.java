package com.backend.repository;

import com.backend.endpoint.dto.FailFilterDto;
import com.backend.entity.Fail;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

@Repository
public interface FailCustomRepository {

    /**
     * Searches for all fails matching criteria.
     *
     * @param failFilterDto - dto with all search criteria for the fail applied
     * @return fails that match the search criteria as pages
     */
    Page<Fail> findAllMatchingFails(FailFilterDto failFilterDto);

}
