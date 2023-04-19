package com.example.DoroServer.global.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import com.example.DoroServer.global.exception.BaseException;
import com.example.DoroServer.global.exception.Code;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AwsS3ServiceImpl implements AwsS3Service{

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.url}")
    private String url;

    private final AmazonS3Client amazonS3Client;

    @Override
    public String upload(MultipartFile multipartFile, String dirName){
        String fileName = dirName+"/"+ UUID.randomUUID() + ".jpg";
        if (multipartFile.isEmpty()) {
            throw new BaseException(Code.BAD_REQUEST);
        }

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());

        try (InputStream inputStream = multipartFile.getInputStream()) {
            byte[] bytes = IOUtils.toByteArray(inputStream);
            objectMetadata.setContentLength(bytes.length);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

            amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, byteArrayInputStream, objectMetadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new BaseException(Code.UPLOAD_FAILED);
        }

        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    @Override
    public void deleteImage(String fileName) {
        int index=fileName.indexOf(url);
        String fileRoute=fileName.substring(index + url.length()+1);
        try {
            boolean isObjectExist = amazonS3Client.doesObjectExist(bucket, fileRoute);
            if (isObjectExist) {
                amazonS3Client.deleteObject(bucket,fileRoute);
            }
        } catch (Exception e) {
            throw new BaseException(Code.BAD_REQUEST);
        }
    }
}
