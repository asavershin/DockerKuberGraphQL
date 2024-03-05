package asavershin.note.services.iml;

import asavershin.note.configuration.MinIOProperties;
import asavershin.note.dto.ImageByteResponse;
import asavershin.note.exceptions.FileException;
import asavershin.note.services.MinioService;
import io.minio.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MinioServiceIml implements MinioService {
    private final MinioClient minioClient;
    private final MinIOProperties minioProperties;

    @Override
    public String saveFile(final MultipartFile image) {
        try {
            createBucket();
        } catch (Exception e) {
            throw new FileException("Image upload failed: "
                    + e.getMessage());
        }
        if (image.isEmpty() || image.getOriginalFilename() == null) {
            throw new FileException("Image must have name");
        }
        var link = generateFileName(image);
        InputStream inputStream;
        try {
            inputStream = image.getInputStream();
        } catch (Exception e) {
            throw new FileException("Image upload failed: "
                    + e.getMessage());
        }
        saveImage(inputStream, link);
        return link;
    }

    @Override
    public byte[] getFile(final String link) {
        if (link == null) {
            throw new FileException("Image download failed: link is nullable");
        }
        try {
            return IOUtils.toByteArray(minioClient.getObject(GetObjectArgs.builder()
                    .bucket(minioProperties.getBucket())
                    .object(link)
                    .build()));
        } catch (Exception e) {
            throw new FileException("Image download failed: " + e.getMessage());
        }
    }

    @Override
    public void deleteFiles(final List<String> links) {
        if (links == null || links.isEmpty()) {
            return;
        }
        if (!bucketExists(minioProperties.getBucket())) {
            throw new FileException("Minio bucket doesn't exist");
        }
        try {
            for (var link : links) {
                minioClient.removeObject(
                        RemoveObjectArgs.builder()
                                .bucket(minioProperties.getBucket())
                                .object(link)
                                .build());
            }
        } catch (Exception e) {
            throw new FileException("Failed to delete file: " + e.getMessage());
        }
    }

    @SneakyThrows
    private void createBucket() {
        boolean found = minioClient.bucketExists(BucketExistsArgs.builder()
                .bucket(minioProperties.getBucket())
                .build());
        if (!found) {
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(minioProperties.getBucket())
                    .build());
        }
    }

    @SneakyThrows
    private void saveImage(
            final InputStream inputStream,
            final String fileName
    ) {
        minioClient.putObject(PutObjectArgs.builder()
                .stream(inputStream, inputStream.available(), -1)
                .bucket(minioProperties.getBucket())
                .object(fileName)
                .build());
    }

    private String generateFileName(final MultipartFile file) {
        String extension = getExtension(file);
        return UUID.randomUUID() + "." + extension;
    }

    private String getExtension(final MultipartFile file) {
        return Objects.requireNonNull(file.getOriginalFilename())
                .substring(file.getOriginalFilename().lastIndexOf(".") + 1);
    }

    @SneakyThrows(Exception.class)
    public boolean bucketExists(String bucketName) {
        return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
    }
}
