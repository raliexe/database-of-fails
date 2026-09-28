package com.backend.endpoint;

import com.backend.endpoint.dto.FailDto;
import com.backend.endpoint.dto.FailFilterDto;
import com.backend.endpoint.dto.common.GeneralSearchResponseDto;
import com.backend.endpoint.dto.common.SearchCategory;
import com.backend.endpoint.mapper.FailMapper;
import com.backend.entity.Fail;
import com.backend.service.fail.FailService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.PermitAll;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/search")
public class GeneralSearchEndpoint {

    private final FailService failService;
    private final FailMapper failMapper;

    @PermitAll
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    @Operation(summary = "Get all matching fails")
    public List<GeneralSearchResponseDto> getAllMatching(String term) {
        Pageable pageable = PageRequest.of(0, 5);

        FailFilterDto failFilterDto = new FailFilterDto(term);
        failFilterDto.setPage(pageable.getPageNumber());
        failFilterDto.setSize(pageable.getPageSize());

        Page<Fail> failPage = failService.getAllMatchingFails(failFilterDto);
        List<FailDto> fails = failPage.getContent()
                .stream()
                .map(fail -> failMapper.entityToFailDto(fail, true, fail.getUser()))
                .collect(Collectors.toList());

        List<GeneralSearchResponseDto> response = new LinkedList<>();
        for (FailDto fail : fails) {
            response.add(new GeneralSearchResponseDto(fail.getId(), fail.getName(), SearchCategory.ARTISTS));
        }
        return response;
    }
}