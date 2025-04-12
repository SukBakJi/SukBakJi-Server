package umc.SukBakJi.global.security.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class JwtBlacklistService {
    private final RedisTemplate<String, String> redisTemplate;
    private static final String BLACKLIST_PREFIX = "blacklist:";

    public void addToBlacklist(String accessToken, long expirationMillis) {
        long currentMillis = System.currentTimeMillis();
        long ttl = expirationMillis - currentMillis;

        if (ttl > 0) {
            redisTemplate.opsForValue().set(BLACKLIST_PREFIX + accessToken, "logout", Duration.ofMillis(Math.max(ttl, 1000)));
        }
    }

    public boolean isBlacklisted(String accessToken) {
        return redisTemplate.hasKey(BLACKLIST_PREFIX + accessToken);
    }
}
