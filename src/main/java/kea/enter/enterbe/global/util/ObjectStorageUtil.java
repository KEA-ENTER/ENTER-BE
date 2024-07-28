package kea.enter.enterbe.global.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
import kea.enter.enterbe.global.common.exception.CustomException;
import kea.enter.enterbe.global.common.exception.ResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@RequiredArgsConstructor
@Component
@Slf4j
public class ObjectStorageUtil {
    @Value("${kakao.cloud.bucket.name}")
    private String bucketName;
    @Value("${kakao.cloud.cdn.endpoint}")
    private String cloudFrontDistribution;
    @Value("${spring.profiles.active}")
    private String profile;

    private final S3Client s3Client;

    public String uploadFileToS3(MultipartFile multipartFile) {
        File uploadFile;
        try {
            uploadFile = convert(multipartFile);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new CustomException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
        return upload(uploadFile, StringUtils.getFilenameExtension(multipartFile.getOriginalFilename()), multipartFile.getContentType());

    }

    private String upload(File uploadFile, String extension, String contentType) {
        String fileName = profile + "/" + UUID.randomUUID() + "." + extension;
        String uploadImageUrl = putS3(uploadFile, fileName, contentType);
        removeNewFile(uploadFile);
        return uploadImageUrl;
    }

    private String putS3(File uploadFile, String fileName, String contentType) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentType(contentType)
                .build();
        s3Client.putObject(putObjectRequest, RequestBody.fromFile(uploadFile));
        return cloudFrontDistribution + "/" +fileName;
    }

    private void removeNewFile(File targetFile) {
        if (!targetFile.delete())
            log.info("파일이 삭제되었습니다.");
    }

    private File convert(MultipartFile file) throws IOException {
        File convertFile = new File(System.getProperty("user.dir") + UUID.randomUUID());
        if (convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
        }
        return convertFile;
    }

}
