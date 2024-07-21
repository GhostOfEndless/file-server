package com.example.service;

import com.example.entity.UserFile;
import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;
import java.util.Optional;

public interface UserFileService {

    Iterable<UserFile> findAllUserFiles(Pageable pageable);

    UserFile createUserFile(String data, String title, String description, Timestamp creationDate);

    Optional<UserFile> findUserFile(Long id);
}
