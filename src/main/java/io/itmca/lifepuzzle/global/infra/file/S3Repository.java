package io.itmca.lifepuzzle.global.infra.file;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@RequiredArgsConstructor
@Service
public class S3Repository implements FileRepository{
    private final AmazonS3Client amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Override
    public  String upload(File file) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, file.getName(), file));
        return amazonS3Client.getUrl(bucket, file.getName()).toString();
    }
}
