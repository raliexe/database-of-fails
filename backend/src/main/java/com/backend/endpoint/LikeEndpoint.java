package com.backend.endpoint;

import com.backend.endpoint.dto.FailDto;
import com.backend.endpoint.mapper.FailMapper;
import com.backend.entity.AppUser;
import com.backend.entity.Fail;
import com.backend.service.like.LikeService;
import com.backend.service.user.UserService;
import com.backend.util.AuthenticationManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/likes")
public class LikeEndpoint {

    private final LikeService likeService;
    private final FailMapper failMapper;

    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    @Secured("ROLE_USER")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/like")
    public ResponseEntity<FailDto> like(@RequestParam Long failId) {
        String email = authenticationManager.getLoggedInUser();
        AppUser user = userService.findAppUserByEmail(email);

        Fail likedFail = likeService.likeFail(failId, user);

        return ResponseEntity.status(HttpStatus.OK)
                .body(failMapper.entityToFailDto(likedFail, false, user));
    }

    @Secured("ROLE_USER")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/unlike")
    public ResponseEntity<FailDto> unlike(@RequestParam("failId") Long failId) {
        String email = authenticationManager.getLoggedInUser();
        AppUser user = userService.findAppUserByEmail(email);

        Fail likedFail = likeService.unlikeFail(failId, user);

        return ResponseEntity.status(HttpStatus.OK)
                .body(failMapper.entityToFailDto(likedFail, false, user));
    }

    @PermitAll
    @ResponseStatus(HttpStatus.OK)
    @GetMapping()
    public boolean getIsFailLikedByUser(@RequestParam Long failId, @RequestParam Long currUserId) {
        return likeService.getIsFailLikedByUser(failId, currUserId);
    }

}
