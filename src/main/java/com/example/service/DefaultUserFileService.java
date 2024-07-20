package com.example.service;

import com.example.entity.UserFile;
import com.example.repository.UserFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DefaultUserFileService implements UserFileService {

    private final UserFileRepository userFileRepository;

    @Override
    public Iterable<UserFile> findAllUserFiles(Pageable pageable) {
        return this.userFileRepository.findAll(pageable);
    }

    @Override
    public UserFile createUserFile(String data, String title, String description, Date creationDate) {
        return this.userFileRepository.save(new UserFile(null, title, description, creationDate, data));
    }

    @Override
    public Optional<UserFile> findUserFile(Long id) {
        return this.userFileRepository.findById(id);
    }
}
