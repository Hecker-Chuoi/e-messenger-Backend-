package com.e_messenger.code.controller;

import com.e_messenger.code.service.impl.CloudStorageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CloudController {
    CloudStorageService storageService;

    @PostMapping
    public String uploadImage(@RequestParam MultipartFile file) throws IOException {
        Map result = storageService.uploadFile(file);
        return result.get("url").toString();
    }
}
