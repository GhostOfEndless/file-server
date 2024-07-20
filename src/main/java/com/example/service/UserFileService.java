package com.example.service;

import com.example.entity.UserFile;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.Optional;

public interface UserFileService {

    Iterable<UserFile> findAllUserFiles(Pageable pageable);

    UserFile createUserFile(String data, String title, String description, Date creationDate);

    Optional<UserFile> findUserFile(Long id);
}
