package asavershin.note.security;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class JwtCache {
    private final String refreshKey = "REFRESH_TOKEN_";
    private final String accessKey = "ACCESS_TOKEN_";
}
