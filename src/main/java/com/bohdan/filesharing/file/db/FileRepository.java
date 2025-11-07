package com.bohdan.filesharing.file.db;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileItem, Long> {
}
