package asavershin.note.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "minio")
public class MinIOProperties {
    private String bucket;
    private String url;
    private String user;
    private String password;
}
