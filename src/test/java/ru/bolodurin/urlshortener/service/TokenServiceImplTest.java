package ru.bolodurin.urlshortener.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import ru.bolodurin.urlshortener.exception.ExpiredException;
import ru.bolodurin.urlshortener.exception.NotFoundException;
import ru.bolodurin.urlshortener.model.Token;
import ru.bolodurin.urlshortener.repository.TokenRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenServiceImplTest {
    TokenService service;
    @Mock
    TokenRepository repository;

    @BeforeEach
    void init() {
        this.service = new TokenServiceImpl(repository, new TokenEncoderImpl());
        ReflectionTestUtils.setField(service, "expirationDays", 1);
    }

    @Test
    void shouldResolveLink() {
        Token valid = Token
                .builder()
                .token(new Object().toString())
                .longUrl(new Object().toString())
                .createdAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.MAX)
                .build();

        Token expired = Token
                .builder()
                .token(new Object().toString())
                .longUrl(new Object().toString())
                .createdAt(LocalDateTime.now().minusDays(2))
                .expiredAt(LocalDateTime.now().minusDays(1))
                .build();

        String notFound = new Object().toString();

        when(repository.findById(valid.getToken())).thenReturn(Optional.of(valid));
        when(repository.findById(expired.getToken())).thenReturn(Optional.of(expired));
        when(repository.findById(notFound)).thenReturn(Optional.empty());

        String actual = service.resolveLink(valid.getToken());

        verify(repository).findById(valid.getToken());
        assertThat(actual).isEqualTo(valid.getLongUrl());

        assertThatThrownBy(() -> service.resolveLink(expired.getToken()))
                .isInstanceOf(ExpiredException.class)
                .hasMessageContaining(expired.getToken());

        assertThatThrownBy(() -> service.resolveLink(notFound))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(notFound);
    }

    @Test
    void shouldMakeNewShortLink() {
        String notFound = new Object().toString();

        ArgumentCaptor<Token> tokenArgumentCaptor = ArgumentCaptor.forClass(Token.class);

        when(repository.findByLongUrl(notFound)).thenReturn(null);

        service.makeShortLink(notFound);

        verify(repository).findByLongUrl(notFound);
        verify(repository).save(tokenArgumentCaptor.capture());

        Token created = tokenArgumentCaptor.getValue();

        assertThat(created.getLongUrl()).isEqualTo(notFound);
        assertThat(created.getExpiredAt()).isAfter(LocalDateTime.now());
    }

    @Test
    void shouldReturnSameTokenWhenItsValid() {
        Token valid = Token
                .builder()
                .token(new Object().toString())
                .longUrl(new Object().toString())
                .createdAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.MAX)
                .build();

        when(repository.findByLongUrl(valid.getLongUrl())).thenReturn(valid);

        String actual = service.makeShortLink(valid.getLongUrl());

        verify(repository).findByLongUrl(valid.getLongUrl());
        assertThat(actual).isEqualTo(valid.getToken());
    }

    @Test
    void shouldUpdateWhenExpired() {
        Token expired = Token
                .builder()
                .token(new Object().toString())
                .longUrl(new Object().toString())
                .createdAt(LocalDateTime.now().minusDays(2))
                .expiredAt(LocalDateTime.now().minusDays(1))
                .build();

        ArgumentCaptor<Token> tokenArgumentCaptor = ArgumentCaptor.forClass(Token.class);

        when(repository.findByLongUrl(expired.getLongUrl())).thenReturn(expired);

        service.makeShortLink(expired.getLongUrl());

        verify(repository).findByLongUrl(expired.getLongUrl());
        verify(repository).save(tokenArgumentCaptor.capture());

        Token updated = tokenArgumentCaptor.getValue();

        assertThat(updated.getLongUrl()).isEqualTo(expired.getLongUrl());
        assertThat(updated.getCreatedAt()).isAfter(expired.getCreatedAt());
        assertThat(updated.getExpiredAt())
                .isAfter(expired.getExpiredAt())
                .isAfter(LocalDateTime.now());
    }

}