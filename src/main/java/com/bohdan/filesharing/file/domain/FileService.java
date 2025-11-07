package com.bohdan.filesharing.file.domain;

import com.bohdan.filesharing.common.exception.BadRequestException;
import com.bohdan.filesharing.common.exception.LoadFileToS3ClientException;
import com.bohdan.filesharing.common.exception.LoadFileToS3ServerException;
import com.bohdan.filesharing.common.exception.UserNotFoundException;
import com.bohdan.filesharing.file.api.dto.FileItemDto;
import com.bohdan.filesharing.file.db.FileItem;
import com.bohdan.filesharing.file.db.FileRepository;
import com.bohdan.filesharing.user.db.User;
import com.bohdan.filesharing.user.db.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {

    private final S3Client s3Client;
    private final FileRepository fileRepository;
    private final UserRepository userRepository;

    @Value("${aws.bucket-name}")
    private String bucketName;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Transactional
    public FileItemDto uploadFile(MultipartFile file, Boolean isPublic, Authentication auth) {
        User user = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found: " + auth.getName()));

        String generatedS3Key = "users/" + user.getEmail() + "/" + file.getOriginalFilename();

        Optional<FileItem> fileWithSameS3Key = fileRepository.findByS3Key(generatedS3Key);
        if (fileWithSameS3Key.isPresent()) {
            log.error("File with name {} is a duplication and could not be uploaded", file.getOriginalFilename());
            throw new LoadFileToS3ClientException("File with name: " + file.getOriginalFilename() + " already exists!");
        }

        String savedFileURL = loadFileToS3Bucket(file, generatedS3Key, isPublic);
        log.info("File {} was successfully uploaded to the AWS S3");

        FileItem fileToSave = FileItem.builder()
                .s3Key(generatedS3Key)
                .fileSize(file.getSize())
                .contentType(file.getContentType())
                .isPublic(isPublic)
                .fileUrl(savedFileURL)
                .owner(user)
                .build();

        FileItem savedFile = fileRepository.save(fileToSave);
        log.info("File saved: key - {}, url - {}", savedFile.getS3Key(), savedFile.getFileUrl());

        return FileItemDto.builder()
                .s3Key(savedFile.getS3Key())
                .url(savedFile.getFileUrl())
                .build();
    }

    @Transactional
    public void deleteExistedFile(String s3Key) {
        FileItem file = fileRepository.findByS3Key(s3Key)
                .orElseThrow(() -> new BadRequestException("File was not found"));

        try {
            s3Client.deleteObject(builder -> builder
                    .bucket(bucketName)
                    .key(s3Key)
                    .build());
            log.info("File {} successfully deleted from S3", s3Key);

            fileRepository.delete(file);
            log.info("File {} successfully deleted from database", s3Key);
        } catch (Exception e) {
            log.error("Error deleting file {} from S3: {}", s3Key, e.getMessage());
            throw new LoadFileToS3ServerException("Error deleting file from S3. Please try again.");
        }
    }

    private String loadFileToS3Bucket(MultipartFile file, String s3Key, Boolean isPublic) {
        try {
            ObjectCannedACL acl = isPublic
                    ? ObjectCannedACL.PUBLIC_READ
                    : ObjectCannedACL.PRIVATE;
            String fileContentType = file.getContentType();
            byte[] fileBytes = file.getBytes();

            s3Client.putObject(PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(s3Key)
                            .acl(acl)
                            .contentType(fileContentType)
                            .build(),
                    RequestBody.fromBytes(fileBytes));

            return "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + s3Key;
        } catch (Exception e) {
            log.error("Error during file loading to AWS S3: {}", e.getMessage());
            throw new LoadFileToS3ServerException("Error during file loading. Please try again.");
        }
    }
}
