package com.example.controller;

import com.example.controller.payload.UserFilePayload;
import com.example.entity.UserFile;
import com.example.service.UserFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("file-server-api")
@RequiredArgsConstructor
public class UserFileRestController {

    private final UserFileService userFileService;

    @PostMapping
    public ResponseEntity<?> createUserFile(@RequestBody UserFilePayload payload) {
        this.userFileService.createUserFile(
          payload.data(),
          payload.title(),
          payload.description(),
          payload.creationDate()
        );

        return ResponseEntity.noContent()
                .build();
    }

    @GetMapping("/file")
    public UserFile findUserFile(@RequestParam(name = "id") Long id) {
        Optional<UserFile> userFile = this.userFileService.findUserFile(id);
        if (userFile.isPresent()) {
            return userFile.get();
        }
        return null;
    }

    @GetMapping("/file/page/{page}")
    public List<UserFile> findAll(
            @PathVariable(name = "page") int pageNumber,
            @RequestParam(name = "size" ,required = false, defaultValue = "5") int pageSize) {
        Pageable pageable = PageRequest
                .of(pageNumber, pageSize, Sort.by("creationDate")
                .descending());
        return this.userFileService.findAllUserFiles(pageable);
    }
}
