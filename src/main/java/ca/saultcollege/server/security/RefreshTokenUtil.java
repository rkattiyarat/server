package ca.saultcollege.server.security;

import ca.saultcollege.server.data.RefreshToken;
import ca.saultcollege.server.repositories.AccountRepository;
import ca.saultcollege.server.repositories.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Component
public class RefreshTokenUtil {
    private static final long EXPIRE_DURATION = 60 * 60 * 1000 * 24; // 1 day
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private AccountRepository accountRepository;
    public RefreshToken findByToken(String token) {
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByToken(token);
        return refreshToken.orElse(null);
    }
    public RefreshToken createRefreshToken(Integer accountId) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setAccount(accountRepository.findById(accountId).get());
        refreshToken.setExpiryDate(Instant.now().plusMillis(EXPIRE_DURATION));
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }
    public boolean verifyExpiration(RefreshToken token) throws Exception {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new Exception("RefreshTokenExpired");
        }
        return true;
    }
    public int deleteByAccountId(Integer accountId) {
        return refreshTokenRepository.deleteByAccount(accountRepository.findById(accountId).get());
    }

}


