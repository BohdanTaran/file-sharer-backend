package com.bohdan.filesharing.auth.domain;

import com.bohdan.filesharing.auth.api.dto.AuthRequestDto;
import com.bohdan.filesharing.auth.api.dto.AuthResponseDto;
import com.bohdan.filesharing.auth.security.JwtUtil;
import com.bohdan.filesharing.common.exception.UserAuthenticationException;
import com.bohdan.filesharing.user.db.User;
import com.bohdan.filesharing.user.db.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    public static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public AuthResponseDto signUp(AuthRequestDto req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            log.error("User with email: {} already exists", req.getEmail());
            throw new UserAuthenticationException("User with email '" + req.getEmail() + "' already exists!");
        }

        var encodedPassword = passwordEncoder.encode(req.getPassword());

        User user = User.builder()
                .email(req.getEmail())
                .password(encodedPassword)
                .createdAt(LocalDate.now())
                .build();

        userRepository.save(user);

        String accessToken = jwtUtil.generateToken(user.getEmail());

        return AuthResponseDto.builder()
                .accessToken(accessToken)
                .build();
    }

    @Transactional
    public AuthResponseDto login(AuthRequestDto req) {
        User user = null;

        if (req.getEmail().matches(EMAIL_REGEX)) {
            user = userRepository.findByEmail(req.getEmail()).orElseThrow(
                    () -> new UserAuthenticationException("Invalid credentials. Email does not exist!")
            );
        }

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new UserAuthenticationException("Invalid credentials. Password does not match!");
        }

        String accessToken = jwtUtil.generateToken(user.getEmail());

        return AuthResponseDto.builder()
                .accessToken(accessToken)
                .build();
    }
}
