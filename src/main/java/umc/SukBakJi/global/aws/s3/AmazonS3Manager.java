package umc.SukBakJi.global.aws.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import umc.SukBakJi.domain.common.entity.enums.EducationCertificateType;
import umc.SukBakJi.global.config.S3Config;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class AmazonS3Manager {
    private final AmazonS3 amazonS3;
    private final S3Config s3Config;

    public String uploadFile(String keyName, MultipartFile file) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());

        try {
            amazonS3.putObject(new PutObjectRequest(s3Config.getBucket(), keyName, file.getInputStream(), metadata));
        } catch (IOException e) {
            log.error("error at AmazonS3Manager uploadFile : {}", (Object) e.getStackTrace());
        }
        return getFileUrl(keyName);
    }

    public void deleteFile(String keyName) {
        try {
            amazonS3.deleteObject(s3Config.getBucket(), keyName);
            log.info("File deleted from S3: {}", keyName);
        } catch (Exception e) {
            log.error("Error deleting file from S3: {}", keyName, e);
        }
    }

    public String generateEducationCertificateKeyName(Long userId, EducationCertificateType type, String uuid) {
        return String.format("education-certificates/users/%d/%s/%s.jpg", userId, type.getValue(), uuid);
    }

    public String getFileUrl(String keyName) {
        return amazonS3.getUrl(s3Config.getBucket(), keyName).toString();
    }
}
