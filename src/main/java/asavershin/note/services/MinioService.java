package asavershin.note.services;

import asavershin.note.dto.ImageByteResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MinioService extends FileService<String, MultipartFile>{
}
