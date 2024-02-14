package ru.bolodurin.urlshortener;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.bolodurin.urlshortener.config.TestConfig;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = TestConfig.class)
class UrlShortenerApplicationTests {

    @Autowired
    WebTestClient webClient;
    @Autowired
    PostgreSQLContainer<?> container;

    @Test
    void shouldWork() {
        String username = container.getUsername();
        String password = container.getPassword();

        webClient.post().uri("/created")
                .exchange()
                .expectStatus().isCreated();

        String token = "~6uIfFYeAKUFRKiL"; //token for "created"

        webClient.get().uri("/" + token)
                .exchange()
                .expectStatus()
                .is3xxRedirection();

    }

}
