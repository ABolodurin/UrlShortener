package ru.bolodurin.urlshortener.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TokenEncoderImplTest {
    TokenEncoder encoder;

    @BeforeEach
    void init() {
        this.encoder = new TokenEncoderImpl();
    }

    @Test
    void shouldEncodeCorrectHash() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 50; i++) {
            String s = new Object().toString();
            sb.append(s);
        }

        List<String> hashList = new ArrayList<>(2);

        hashList.add(encoder.encode(sb.substring(0, sb.length() / 2)));
        hashList.add(encoder.encode(sb.substring(sb.length() / 2)));

        hashList.forEach(hash -> {
            assertThat(hash.length()).isLessThan(16);
            assertThat(hash).matches("[a-zA-Z0-9]+");
        });

        assertThat(hashList.get(0)).isNotEqualTo(hashList.get(1));
    }

}