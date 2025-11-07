package com.bohdan.filesharing.file.db;

import com.bohdan.filesharing.user.db.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<FileItem, Long> {
    List<FileItem> findAllByOwner(User user);
}
