package com.bohdan.filesharing.file.api;

import com.bohdan.filesharing.file.api.dto.FileItemDto;
import com.bohdan.filesharing.file.db.FileItem;
import com.bohdan.filesharing.file.domain.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@Slf4j
public class FileController {

    private final FileService fileService;

    /**
     * Upload a new file to AWS S3
     *
     * @param file User's file to upload
     * @param isPublic Set visibility permission for the file
     * @param auth Authentication context to get the user
     *
     * @return Response with s3Key & url to see the file
     */
    @Operation(summary = "Upload file", description = "Uploads file & returns details")
    @ApiResponse(
            responseCode = "200",
            description = "File is uploaded successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = FileItemDto.class)
            ))
    @ApiResponse(
            responseCode = "400",
            description = "User was not found",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            value = "User with email john@example.com was not found!"
                    )
            ))
    @ApiResponse(
            responseCode = "500",
            description = "Something went wrong on the upload process",
            content = @Content
    )
    @PostMapping("/upload")
    public ResponseEntity<FileItemDto> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("isPublic") Boolean isPublic,
            @Parameter(hidden = true) Authentication auth
    ) {
        FileItemDto response = fileService.uploadFile(file, isPublic, auth);
        log.info("File {} was uploaded successfully", file.getOriginalFilename());
        return ResponseEntity.ok(response);
    }
    /**
     * Delete a file from AWS S3 and the database.
     *
     * @param s3Key The unique S3 object key identifying the file to delete.
     * @return A success message if the file was deleted successfully.
     *
     * This endpoint deletes both the file object from AWS S3 and its record
     * from the database. Only files owned by the authenticated user can be deleted.
     */
    @Operation(
            summary = "Delete file",
            description = "Deletes the specified file from AWS S3 and removes its record from the database. " +
                    "The authenticated user must be the owner of the file.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "File deleted successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "File was deleted successfully")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid S3 key or file not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "File was not found")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "User is not allowed to delete this file",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "Access denied")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error during S3 deletion",
                            content = @Content
                    )
            }
    )
    @DeleteMapping
    public ResponseEntity<String> removeFile(@RequestParam("s3Key") String s3Key) {
        fileService.deleteExistedFile(s3Key);
        log.info("File {} was deleted successfully", s3Key);
        return ResponseEntity.ok("File was deleted successfully");
    }
    /**
     * Get all files by the authenticated user
     *
     * @return Returns all files uploaded by the current authenticated user
     */
    @Operation(summary = "Get my uploaded files",
            description = "Returns all files uploaded by the current authenticated user")
    @ApiResponse(
            responseCode = "200",
            description = "List of user's files",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = FileItemDto.class))
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "User was not found",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            value = "User with email john@example.com was not found!"
                    )
            ))
    @GetMapping("/my")
    public ResponseEntity<List<FileItem>> getMyFiles(Authentication auth) {
        List<FileItem> myFiles = fileService.getMyFiles(auth);
        return ResponseEntity.ok(myFiles);
    }
}
