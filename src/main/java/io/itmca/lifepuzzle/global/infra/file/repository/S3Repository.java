package io.itmca.lifepuzzle.global.infra.file.repository;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import io.itmca.lifepuzzle.global.constant.FileConstant;
import io.itmca.lifepuzzle.global.infra.file.CustomFile;
import io.itmca.lifepuzzle.global.util.FileUtil;
import java.io.File;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class S3Repository implements FileRepository {
  private final AmazonS3Client amazonS3Client;
  @Value("${cloud.aws.s3.bucket}")
  private String bucket;

  public void upload(CustomFile customFile) throws IOException {
    var tempFolder = FileConstant.TEMP_FOLDER_PATH + File.separator + customFile.getBase();

    if (!FileUtil.isExistFolder(tempFolder)) {
      FileUtil.createAllFolder(tempFolder);
    }

    var localFile = FileUtil.saveMultiPartFileInLocal(customFile.getBytes(),
        tempFolder + File.separator + customFile.getFileName());

    amazonS3Client.putObject(new PutObjectRequest(bucket,
        customFile.getBase() + customFile.getFileName(),
        localFile));

    localFile.delete();
  }

  public void delete(String base) throws IOException {
    var listObjectsV2Result = amazonS3Client.listObjectsV2(bucket, base);
    for (var objectSummary : listObjectsV2Result.getObjectSummaries()) {
      amazonS3Client.deleteObject(bucket, objectSummary.getKey());
    }
  }
}
