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

  public void upload(CustomFile customFile, String base) throws IOException {
    var tempFolder = FileConstant.TEMP_FOLDER_PATH + File.separator + customFile.getBase();

    if (!FileUtil.isExistFolder(tempFolder)) {
      FileUtil.createAllFolder(tempFolder);
    }

    var localFile = FileUtil.saveMultiPartFileInLocal(customFile.getBytes(),
        tempFolder + File.separator + customFile.getFileName());

    amazonS3Client.putObject(new PutObjectRequest(bucket,
        base + File.separator + customFile.getBase() + File.separator + customFile.getFileName(),
        localFile));

    System.out.println(amazonS3Client.getUrl(bucket,
            base + File.separator + customFile.getBase()
                + File.separator + customFile.getFileName())
        .toString());

    localFile.delete();
  }
}
