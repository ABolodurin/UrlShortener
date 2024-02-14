package ru.bolodurin.urlshortener.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.bolodurin.urlshortener.model.Token;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class TokenRepositoryTest {
    @Autowired
    TokenRepository repository;

    @Test
    void shouldFindByLongUrl() {
        String longUrl = new Object().toString();
        Token expected = Token
                .builder()
                .token(new Object().toString())
                .longUrl(longUrl)
                .createdAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusDays(234L))
                .build();

        repository.save(expected);

        Token actual = repository.findByLongUrl(longUrl);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
        assertThat(repository.findByLongUrl("whatever")).isNull();
    }

}