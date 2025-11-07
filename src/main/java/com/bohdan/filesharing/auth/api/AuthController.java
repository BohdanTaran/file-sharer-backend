package com.bohdan.filesharing.auth.api;

import com.bohdan.filesharing.auth.domain.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RequestMapping("/api/auth")

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    /**
     * Login user
     *
     * @param authReqDto
     *            The Authentication user's dto with email & password
     * @return Response with access token
     */
    @Operation(summary = "Login user", description = "Authenticates the user and returns JWT authentication token")
    @ApiResponse(
            responseCode = "200",
            description = "User logged successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AuthResponseDto.class)
            ))
    @ApiResponse(
            responseCode = "400",
            description = "Invalid credentials",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            value = "Invalid credentials. Email does not exist!"
                    )
            ))
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody @Valid AuthRequestDto authReqDto) {
        log.info("User {} is logging", authReqDto);
        AuthResponseDto response = authService.login(authReqDto);
        log.info("User {} logged successfully", authReqDto);
        return ResponseEntity.ok(response);
    }

    /**
     * Signup user
     *
     * @param authReqDto
     *            The Authentication user's dto with email & password
     * @return Response with access token
     */
    @Operation(summary = "Signup user", description = "Signs the user up and returns JWT authentication token")
    @ApiResponse(
            responseCode = "200",
            description = "User signed up successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AuthResponseDto.class)
            ))
    @ApiResponse(
            responseCode = "400",
            description = "Invalid credentials",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            value = "User with email john@example.com already exists!"
                    )
            ))
    @PostMapping("/signUp")
    public ResponseEntity<AuthResponseDto> signUp(@RequestBody @Valid AuthRequestDto authReqDto) {
        log.info("User {} is signing up", authReqDto);
        AuthResponseDto response = authService.signUp(authReqDto);
        log.info("User {} signed up successfully", authReqDto);
        return ResponseEntity.ok(response);
    }
}
