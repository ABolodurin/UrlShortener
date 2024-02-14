package ru.bolodurin.urlshortener.service;

public interface TokenService {

    String resolveLink(String link);

    String makeShortLink(String link);

}
