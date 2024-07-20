package com.example.controller;

import com.example.controller.payload.UserFilePayload;
import com.example.entity.UserFile;
import com.example.service.UserFileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Locale;
import java.util.NoSuchElementException;

@Slf4j
@RestController
@RequestMapping("file-server-api/files")
@RequiredArgsConstructor
public class UserFileRestController {

    private final MessageSource messageSource;

    private final UserFileService userFileService;

    @PostMapping
    public ResponseEntity<Object> createUserFile(@Valid @RequestBody UserFilePayload payload,
                                                 BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        }

        Long id = this.userFileService.createUserFile(
                        payload.data(),
                        payload.title(),
                        payload.description(),
                        payload.creationDate())
                .getId();

        return new ResponseEntity<>(new HashMap<>() {{
            put("id", id);
        }}, HttpStatus.CREATED);
    }

    @GetMapping("/{fileId}")
    public UserFile findUserFile(@PathVariable(name = "fileId") Long fileId) {
        return this.userFileService.findUserFile(fileId)
                .orElseThrow(() -> new NoSuchElementException("file-server.errors.file.not_found"));
    }

    @GetMapping("/page/{page}")
    public Iterable<UserFile> findAll(
            @PathVariable(name = "page") int pageNumber,
            @RequestParam(name = "size", required = false, defaultValue = "5") int pageSize) {
        Pageable pageable = PageRequest
                .of(pageNumber, pageSize, Sort.by("creationDate")
                        .descending());
        return this.userFileService.findAllUserFiles(pageable);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ProblemDetail> handleNoSuchElementException(NoSuchElementException exception,
                                                                      Locale locale) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,
                        this.messageSource.getMessage(exception.getMessage(), new Object[0],
                                exception.getMessage(), locale)));
    }
}
