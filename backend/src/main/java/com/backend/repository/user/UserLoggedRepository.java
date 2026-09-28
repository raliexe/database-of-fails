package com.backend.repository.user;

import com.backend.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLoggedRepository extends JpaRepository<AppUser, Long> {

}
