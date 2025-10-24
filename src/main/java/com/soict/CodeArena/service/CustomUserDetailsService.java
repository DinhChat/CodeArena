package com.soict.CodeArena.service;

import com.soict.CodeArena.model.User;
import com.soict.CodeArena.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       User user = userRepository.findByUsername(username);
        if (user == null)
            throw new UsernameNotFoundException("Not found user: " + username);
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getHashedPassword(),
                Collections.singleton(() -> user.getRole().toString())
        );
    }
}
