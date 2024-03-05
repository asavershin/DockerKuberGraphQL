package asavershin.note.repositories.iml;

import asavershin.note.repositories.CacheRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CacheRepositoryIml implements CacheRepository {
    private final RedisTemplate<String, Object> redisTemplate;
    @Override
    public void addCache(String key, String token, long expiration) {
        redisTemplate.opsForValue().set(key, token, expiration, TimeUnit.MILLISECONDS);
    }

    @Override
    public String getCache(String key) {
        return (String) redisTemplate.opsForValue().get(key);
    }

    @Override
    public void deleteCache(String key) {
        redisTemplate.delete(key);
    }
}
