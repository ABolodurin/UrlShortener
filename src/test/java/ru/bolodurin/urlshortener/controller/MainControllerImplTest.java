package ru.bolodurin.urlshortener.controller;

import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.bolodurin.urlshortener.service.TokenService;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MainControllerImpl.class)
@ExtendWith(MockitoExtension.class)
class MainControllerImplTest {
    @Autowired
    MockMvc mvc;
    @MockBean
    TokenService service;
    @Value(value = "${application.token.prefix}")
    private String tokenPrefix;

    @BeforeEach
    void init() {
    }

    @Test
    void shouldResolvePath() throws Exception {
        String path = new Object().toString();

        try {
            mvc.perform(get("/" + path));
        } catch (ServletException ignore) {
        }

        verify(service, never()).resolveLink(path);

        try {
            mvc.perform(get("/" + tokenPrefix + path));
        } catch (ServletException ignore) {
        }

        verify(service).resolveLink(path);
    }

    @Test
    void shouldMakeOrReturnUrl() throws Exception {
        String path = new Object().toString();

        mvc.perform(post("/" + path))
                .andExpect(status().isCreated());

        verify(service).makeShortLink(path);
    }

}