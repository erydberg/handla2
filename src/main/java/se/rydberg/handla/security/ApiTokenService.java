package se.rydberg.handla.security;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.HexFormat;
import java.util.List;
import java.util.Optional;

@Service
public class ApiTokenService {
    static final String PREFIX = "hk_";
    private static final int TOKEN_BYTES = 32;

    private final ApiTokenRepository apiTokenRepository;
    private final SecureRandom secureRandom = new SecureRandom();

    public ApiTokenService(ApiTokenRepository apiTokenRepository) {
        this.apiTokenRepository = apiTokenRepository;
    }

    /**
     * Skapar en ny nyckel och returnerar den i klartext. Det är enda gången den går att se.
     */
    @Transactional
    public String create(HandlaUser user, String name) {
        byte[] bytes = new byte[TOKEN_BYTES];
        secureRandom.nextBytes(bytes);
        String plain = PREFIX + Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        ApiToken token = ApiToken.builder()
                .name(name == null || name.isBlank() ? "API-nyckel" : name.trim())
                .tokenHash(hash(plain))
                .user(user)
                .createdAt(Instant.now())
                .build();
        apiTokenRepository.save(token);
        return plain;
    }

    @Transactional
    public Optional<HandlaUser> authenticate(String plain) {
        if (plain == null || !plain.startsWith(PREFIX)) {
            return Optional.empty();
        }
        return apiTokenRepository.findByTokenHash(hash(plain))
                .filter(token -> token.getUser().isEnabled())
                .map(token -> {
                    token.setLastUsedAt(Instant.now());
                    return token.getUser();
                });
    }

    public List<ApiToken> getTokensFor(HandlaUser user) {
        return apiTokenRepository.findByUserOrderByCreatedAtDesc(user);
    }

    @Transactional
    public void revoke(Long id, HandlaUser user) {
        apiTokenRepository.findById(id)
                .filter(token -> token.getUser().getId().equals(user.getId()))
                .ifPresent(apiTokenRepository::delete);
    }

    static String hash(String plain) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(digest.digest(plain.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 saknas", e);
        }
    }
}
