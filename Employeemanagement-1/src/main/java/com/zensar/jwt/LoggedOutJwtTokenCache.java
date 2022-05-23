package com.zensar.jwt;
import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.event.OnUserLogoutSuccessEvent;

import lombok.extern.slf4j.Slf4j;
import net.jodah.expiringmap.ExpiringMap;

@Slf4j
public class LoggedOutJwtTokenCache {
	private ExpiringMap<String, OnUserLogoutSuccessEvent> tokenEventMap;
    private JwtProvider tokenProvider;
    
    public void setTokenProvider(JwtProvider jwtProvider) {
    	this.tokenProvider = jwtProvider;
    }
    
    public LoggedOutJwtTokenCache() {
        this.tokenEventMap = ExpiringMap.builder()
                .variableExpiration()
                .maxSize(1000)
                .build();
    }

    public void markLogoutEventForToken(OnUserLogoutSuccessEvent event) {
        String token = event.getToken();
        if (tokenEventMap.containsKey(token)) {
            log.info(String.format("Log out token for user [%s] is already present in the cache", event.getUserEmail()));

        } else {
            Date tokenExpiryDate = tokenProvider.getTokenExpiryFromJWT(token);
            long ttlForToken = getTTLForToken(tokenExpiryDate);
            log.info(String.format("Logout token cache set for [%s] with a TTL of [%s] seconds. Token is due expiry at [%s]", event.getUserEmail(), ttlForToken, tokenExpiryDate));
            tokenEventMap.put(token, event, ttlForToken, TimeUnit.SECONDS);
        }
    }

    public OnUserLogoutSuccessEvent getLogoutEventForToken(String token) {
        return tokenEventMap.get(token);
    }

    private long getTTLForToken(Date date) {
        long secondAtExpiry = date.toInstant().getEpochSecond();
        long secondAtLogout = Instant.now().getEpochSecond();
        return Math.max(0, secondAtExpiry - secondAtLogout);
    }
}
