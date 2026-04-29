package com.gymapp.storage.service;

import com.gymapp.storage.dto.FileUploadResponse;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    FileUploadResponse uploadFile(MultipartFile file);
}
