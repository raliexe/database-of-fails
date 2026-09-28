package com.backend.endpoint;

import com.backend.endpoint.dto.FailDto;
import com.backend.endpoint.mapper.FailMapper;
import com.backend.endpoint.mapper.ImageMapper;
import com.backend.entity.AppUser;
import com.backend.entity.Fail;
import com.backend.entity.FailImage;
import com.backend.service.fail.FailService;
import com.backend.service.pdf.PdfService;
import com.backend.service.user.UserService;
import com.backend.util.AuthenticationManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.PermitAll;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/fails")
public class FailEndpoint {

    private final FailService failService;
    private final PdfService pdfService;
    private final FailMapper failMapper;
    private final ImageMapper imageMapper;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    @Secured("ROLE_USER")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<FailDto> create(@RequestParam String name,
                                          @RequestParam String description,
                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                          @RequestParam MultipartFile image) {
        String email = authenticationManager.getLoggedInUser();
        AppUser user = userService.findAppUserByEmail(email);

        FailDto failDto = new FailDto(null, name, description, date);
        FailImage img = imageMapper.multipartFileToImage(image);

        failDto = failService.addFail(failDto, img, user);

        return ResponseEntity.status(HttpStatus.CREATED).body(failDto);
    }

    @Secured("ROLE_USER")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<FailDto> update(@RequestParam Long id,
                                          @RequestParam String name,
                                          @RequestParam String description,
                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                          @RequestParam(required = false) MultipartFile image,
                                          @RequestParam(required = false) MultipartFile[] images,
                                          @RequestParam(required = false) Long[] deletedImages) {
        String email = authenticationManager.getLoggedInUser();
        AppUser user = userService.findAppUserByEmail(email);

        FailDto failDto = new FailDto(id, name, description, date);
        FailImage img = image != null ? imageMapper.multipartFileToImage(image) : null;
        List<FailImage> imgs = new ArrayList<>();
        if (images != null && images.length > 0) {
            for (MultipartFile file : images) {
                imgs.add(imageMapper.multipartFileToImage(file));
            }
        }

        failDto = failService.updateFail(failDto, img, imgs, deletedImages, user);

        return ResponseEntity.status(HttpStatus.OK).body(failDto);
    }

    @PermitAll
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{id}")
    public ResponseEntity<FailDto> getById(@PathVariable Long id) {
        String email = authenticationManager.getLoggedInUser();
        AppUser user = userService.findAppUserByEmail(email);
        return ResponseEntity.status(HttpStatus.OK).
                body(failMapper.entityToFailDto(failService.getById(id), true, user));
    }

    @PermitAll
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/getAll", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<FailDto>> getAll() {
        List<Fail> fails = failService.getAll();

        String email = authenticationManager.getLoggedInUser();
        AppUser user = userService.findAppUserByEmail(email);
        List<FailDto> result = new ArrayList<>();
        for (Fail fail : fails) {
            result.add(failMapper.entityToFailDto(fail, false, user));
        }
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Secured("ROLE_USER")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/getAllByCurrentUser", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<FailDto>> getByCurrentUser() {
        String email = authenticationManager.getLoggedInUser();
        AppUser user = userService.findAppUserByEmail(email);

        List<Fail> fails = failService.getByCurrentUser(user);
        List<FailDto> result = new ArrayList<>();
        for (Fail fail : fails) {
            result.add(failMapper.entityToFailDto(fail, false, user));
        }
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Secured("ROLE_USER")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}")
    void delete(@PathVariable("id") Long id) {
        String email = authenticationManager.getLoggedInUser();
        AppUser user = userService.findAppUserByEmail(email);
        failService.deleteFailById(id, user);
    }

    @PermitAll
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{id}/invoice")
    public ResponseEntity<Resource> getFailPDF(@PathVariable("id") Long failId) {
        byte[] pdfInvoice = pdfService.findPdfByFailId(failId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        Resource resource = new ByteArrayResource(pdfInvoice);
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

}
