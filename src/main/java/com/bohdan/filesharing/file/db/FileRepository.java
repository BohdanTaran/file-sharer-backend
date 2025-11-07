package com.bohdan.filesharing.file.db;

import com.bohdan.filesharing.user.db.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<FileItem, Long> {
    /**
     * Find a file by s3Key
     *
     * @param s3Key file's s3Key
     * @return Optional<FileItem> - file or empty Optional
     */
    Optional<FileItem> findByS3Key(String s3Key);
    /**
     * Find all owner's files
     *
     * @param user user
     * @return List<FileItem> - list of files
     */
    List<FileItem> findAllByOwner(User user);
}
