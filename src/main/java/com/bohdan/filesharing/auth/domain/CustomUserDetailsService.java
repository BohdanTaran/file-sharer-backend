package com.bohdan.filesharing.auth.domain;

import com.bohdan.filesharing.user.db.User;
import com.bohdan.filesharing.user.db.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);

        if (!user.isPresent()) {
            throw new UsernameNotFoundException("User was not found with email: " + email);
        }

        return new org.springframework.security.core.userdetails.User(
                user.get().getEmail(),
                user.get().getPassword(),
                Collections.emptyList()
        );
    }
}
