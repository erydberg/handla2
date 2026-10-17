package se.rydberg.handla.security;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Personlig API-nyckel, t.ex. för en genväg på telefonen/klockan.
 * Endast en SHA-256-hash av nyckeln sparas.
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "api_token")
public class ApiToken {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @Column(nullable = false, unique = true, length = 64)
    private String tokenHash;

    @ManyToOne(optional = false)
    @JoinColumn(name = "fk_user")
    private HandlaUser user;

    private Instant createdAt;

    private Instant lastUsedAt;

    private static final DateTimeFormatter DISPLAY = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            .withZone(ZoneId.of("Europe/Stockholm"));

    public String getCreatedText() {
        return createdAt == null ? "" : DISPLAY.format(createdAt);
    }

    public String getLastUsedText() {
        return lastUsedAt == null ? "aldrig" : DISPLAY.format(lastUsedAt);
    }
}
