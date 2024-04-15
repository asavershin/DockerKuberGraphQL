package asavershin.note.security;

public interface TokenRepository {
    String getAccessToken(String email);

    String getRefreshToken(String email);

    void saveRefreshToken(String username, String jwtToken, Long expiration);

    void saveAccessToken(String username, String jwtToken, Long expiration);

    void deleteAllTokensByUserEmail(String username);
}
