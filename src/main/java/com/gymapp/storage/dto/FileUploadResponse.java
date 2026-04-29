package com.gymapp.storage.dto;

public class FileUploadResponse {

    private String fileName;
    private String fileUrl;
    private long size;
    private String contentType;

    public FileUploadResponse(String fileName, String fileUrl, long size, String contentType) {
        this.fileName = fileName;
        this.fileUrl = fileUrl;
        this.size = size;
        this.contentType = contentType;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public long getSize() {
        return size;
    }

    public String getContentType() {
        return contentType;
    }
}
