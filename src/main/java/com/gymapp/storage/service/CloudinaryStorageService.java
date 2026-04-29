package com.gymapp.storage.service;

import com.gymapp.common.exception.BadRequestException;
import com.gymapp.storage.dto.FileUploadResponse;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CloudinaryStorageService implements FileStorageService {

    @Override
    public FileUploadResponse uploadFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("File is required");
        }

        String originalName = file.getOriginalFilename() == null ? "file" : file.getOriginalFilename();
        String generatedName = UUID.randomUUID() + "-" + originalName;
        String fileUrl = "https://res.cloudinary.com/demo/image/upload/" + generatedName;

        return new FileUploadResponse(
                generatedName,
                fileUrl,
                file.getSize(),
                file.getContentType());
    }
}
