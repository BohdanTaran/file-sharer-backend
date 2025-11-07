package com.bohdan.filesharing.auth.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponseDto {
    @Schema(description = "JWT access token", example = "jwt-access-token")
    private String accessToken;
}
