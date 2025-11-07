package com.bohdan.filesharing.file.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileItemDto {
    @Schema(description = "s3Key of saved file", example = "users/john@gmail.com/img.jpg")
    @NotBlank(message = "s3Key cannot be null or empty")
    private String s3Key;

    @Schema(description = "Url of saved file", example = "https://bucketName.s3.amazonaws.com/s3FileName")
    @NotBlank(message = "Url cannot be null or empty")
    private String url;
}
