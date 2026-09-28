package com.backend.service.like;

import com.backend.entity.AppUser;
import com.backend.entity.Fail;
import com.backend.entity.Like;
import com.backend.exception.AlreadyExistsException;
import com.backend.exception.NotFoundException;
import com.backend.exception.PersistenceException;
import com.backend.repository.FailRepository;
import com.backend.repository.LikeRepository;
import com.backend.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final FailRepository failRepository;
    @Autowired
    private final LikeRepository likeRepository;

    @Override
    public Fail likeFail(Long failId, AppUser user) {
        try {
            Fail fail = failRepository.findFailById(failId);
            if (fail == null) {
                throw new NotFoundException("Fail not found!");
            }

            for (Like like : fail.getLikes()) {
                if (like.getUser().getId().equals(user.getId())) {
                    throw new AlreadyExistsException("Like for this fail by this user already exists!");
                }
            }

            Like like = new Like();
            like.setFail(fail);
            like.setUser(user);
            fail.getLikes().add(like);
            return failRepository.save(fail);
        } catch (DataAccessException e) {
            throw new PersistenceException("A server error occured", e);
        }
    }

    @Override
    public Fail unlikeFail(Long failId, AppUser user) {
        try {
            Fail fail = failRepository.findFailById(failId);
            if (fail == null) {
                throw new NotFoundException("Fail not found!");
            }

            Like like = null;
            for (Like l : fail.getLikes()) {
                if (l.getUser().getId().equals(user.getId())) {
                    like = l;
                }
            }
            if (like == null) {
                throw new NotFoundException("Like for this fail by this user not found!");
            }

            like.setFail(null);
            like.setUser(null);
            fail.getLikes().remove(like);
            likeRepository.delete(like);
            return failRepository.save(fail);
        } catch (DataAccessException e) {
            throw new PersistenceException("A server error occured", e);
        }
    }

    @Override
    public boolean getIsFailLikedByUser(Long failId, Long currUserId) {
        try {
            AppUser user = userRepository.findAppUserById(currUserId);
            if (user == null) {
                throw new NotFoundException("Currently logged user not found!");
            }
            Fail fail = failRepository.findFailById(failId);
            if (fail == null) {
                throw new NotFoundException("Fail not found!");
            }
            return likeRepository.findLikeByUserAndFail(user, fail) != null;
        } catch (DataAccessException e) {
            throw new PersistenceException("A server error occured", e);
        }
    }

}
