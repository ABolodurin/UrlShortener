package ru.bolodurin.urlshortener.service;

public interface TokenEncoder {

    String encode(String longUrl);

}
