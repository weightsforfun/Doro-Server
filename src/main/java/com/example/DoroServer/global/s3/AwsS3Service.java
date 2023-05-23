package com.example.DoroServer.global.s3;

import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface AwsS3Service {
    void deleteImage(String fileName);
    String upload(MultipartFile multipartFile,String dirName) throws IOException;
}
