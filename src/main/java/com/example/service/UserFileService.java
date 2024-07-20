package com.example.service;

import com.example.entity.BaseEntity;
import com.example.entity.UserFile;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface UserFileService {

    List<UserFile> findAllUserFiles(Pageable pageable);

    BaseEntity createUserFile(String data, String title, String description, Date creationDate);

    Optional<UserFile> findUserFile(Long id);
}
