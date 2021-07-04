package org.pbms.pbmsserver.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
    ResponseEntity<String> Upload(String path, MultipartFile image);
}
