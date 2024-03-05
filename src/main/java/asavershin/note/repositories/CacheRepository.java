package asavershin.note.repositories;

public interface CacheRepository {
    void addCache(String key, String token, long expiration);

    String getCache(String key);

    void deleteCache(String key);
}
