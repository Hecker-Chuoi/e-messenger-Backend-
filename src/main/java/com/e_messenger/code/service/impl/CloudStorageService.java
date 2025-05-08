package com.e_messenger.code.service.impl;

import com.cloudinary.Cloudinary;
import com.e_messenger.code.exception.AppException;
import com.e_messenger.code.exception.StatusCode;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CloudStorageService {
    Cloudinary cloudinary;

    public Map uploadImage(MultipartFile file, String directory) throws IOException {
        String contentType = file.getContentType().toLowerCase();
        if(!(contentType.equals("image/jpeg") || contentType.equals("image/png")))
            throw new AppException(StatusCode.UNCATEGORIZED);

        Map<String, Object> config = new HashMap<>();
        config.put("folder", directory);
        config.put("resource_type", "image");
        return cloudinary.uploader().upload(file.getBytes(), config);
    }

    public Map uploadAudio(MultipartFile file, String directory) throws IOException {
        String contentType = file.getContentType();
        if(!contentType.startsWith("audio/"))
            throw new AppException(StatusCode.UNCATEGORIZED);

        Map<String, Object> config = new HashMap<>();
        config.put("folder", directory);
        config.put("resource_type", "video");
        return cloudinary.uploader().upload(file.getBytes(), config);
    }

    public Map uploadVideo(MultipartFile file, String directory) throws IOException {
        String contentType = file.getContentType();
        if(!contentType.startsWith("video/"))
            throw new AppException(StatusCode.UNCATEGORIZED);

        Map<String, Object> config = new HashMap<>();
        config.put("folder", directory);
        config.put("resource_type", "video");
        return cloudinary.uploader().upload(file.getBytes(), config);
    }
}
