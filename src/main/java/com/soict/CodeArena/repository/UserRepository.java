package com.soict.CodeArena.repository;

import com.soict.CodeArena.model.USER_ROLE;
import com.soict.CodeArena.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    Page<User> findAllByRole(USER_ROLE role, Pageable pageable);

    User findUserByUserId(Long userId);
}
