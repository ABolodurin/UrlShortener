package ru.bolodurin.urlshortener.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bolodurin.urlshortener.exception.ExpiredException;
import ru.bolodurin.urlshortener.exception.NotFoundException;
import ru.bolodurin.urlshortener.model.Token;
import ru.bolodurin.urlshortener.repository.TokenRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final TokenRepository repository;
    private final TokenEncoder encoder;
    @Value("${application.token.expiration-days}")
    private int expirationDays;

    @Override
    public String resolveLink(String link) {
        Token token = repository.findById(link)
                .orElseThrow(() -> new NotFoundException(link));

        if (isExpired(token)) throw new ExpiredException(token.getToken());

        return token.getLongUrl();
    }

    @Override
    @Transactional
    public String makeShortLink(String longUrl) {
        Token token = repository.findByLongUrl(longUrl);

        if (token != null && !isExpired(token)) return token.getToken();

        LocalDateTime now = LocalDateTime.now();
        token = Token.builder()
                .longUrl(longUrl)
                .token(encoder.encode(longUrl))
                .createdAt(now)
                .expiredAt(now.plusDays(expirationDays))
                .build();

        repository.save(token);

        return token.getToken();
    }

    private boolean isExpired(Token token) {
        return LocalDateTime.now().isAfter(token.getExpiredAt());
    }

}
