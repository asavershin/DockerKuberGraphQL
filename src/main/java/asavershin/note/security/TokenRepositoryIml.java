package asavershin.note.security;

import asavershin.note.repositories.CacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TokenRepositoryIml implements TokenRepository {
    private final JwtCache jwtCache;
    private final CacheRepository cacheRepository;

    @Override
    public String getAccessToken(String email){
        return cacheRepository.getCache(jwtCache.getAccessKey() + email);
    }

    @Override
    public String getRefreshToken(String email){
        return cacheRepository.getCache(jwtCache.getRefreshKey() + email);
    }

    @Override
    public void saveRefreshToken(String username, String jwtToken, Long expiration) {
        cacheRepository.addCache(jwtCache.getRefreshKey() + username, jwtToken, expiration);
    }

    @Override
    public void saveAccessToken(String username, String jwtToken, Long expiration) {
        cacheRepository.addCache(jwtCache.getAccessKey() + username, jwtToken, expiration);
    }

    @Override
    public void deleteAllTokensByUserEmail(String username) {
        cacheRepository.deleteCache(jwtCache.getRefreshKey() + username);
        cacheRepository.deleteCache(jwtCache.getAccessKey() + username);
    }
}
