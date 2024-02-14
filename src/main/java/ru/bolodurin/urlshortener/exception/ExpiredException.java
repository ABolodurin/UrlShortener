package ru.bolodurin.urlshortener.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.GONE)
public class ExpiredException extends RuntimeException {
    public ExpiredException(String token) {
        super("Url is expired: " + token);
    }

}
