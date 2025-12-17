package com.soict.CodeArena.repository;

import com.soict.CodeArena.model.User;
import com.soict.CodeArena.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    Optional<UserProfile> findUserProfileByUser(User user);
}
