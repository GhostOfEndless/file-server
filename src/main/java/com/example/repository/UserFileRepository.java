package com.example.repository;

import com.example.entity.UserFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface UserFileRepository extends CrudRepository<UserFile, Long> {

    Page<UserFile> findAll(Pageable pageable);
}
