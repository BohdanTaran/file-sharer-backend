package com.bohdan.filesharing.file.db;

import com.bohdan.filesharing.user.db.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "files")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "s3Key", unique = true)
    private String s3Key;

    @Column(name = "fileSize")
    private Long fileSize;

    @Column(name = "contentType")
    private String contentType;

    @Column(name = "isPublic")
    private Boolean isPublic;

    @Column(name = "fileUrl")
    private String fileUrl;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;
}
