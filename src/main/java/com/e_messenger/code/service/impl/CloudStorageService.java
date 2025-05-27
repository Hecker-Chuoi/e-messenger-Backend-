package com.e_messenger.code.service.impl;

import com.cloudinary.Cloudinary;
import com.e_messenger.code.entity.enums.MediaType;
import com.e_messenger.code.exception.AppException;
import com.e_messenger.code.exception.StatusCode;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CloudStorageService {
    Cloudinary cloudinary;

    List<String> allowedContentType = List.of("image/jpeg", "image/png", "audio/mpeg");

    public Map uploadFile(MultipartFile file) throws IOException {
        String contentType = file.getContentType().toLowerCase();
        if(!allowedContentType.contains(contentType))
            throw new AppException(StatusCode.UNCATEGORIZED);

        Map<String, Object> config = new HashMap<>();
        config.put(
                "resource_type",
                MediaType
                        .valueOf(contentType.split("/")[0]
                        .toUpperCase())
                        .getUploadOption());
        return cloudinary.uploader().upload(file.getBytes(), config);
    }
}
