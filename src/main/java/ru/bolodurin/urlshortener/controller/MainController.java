package ru.bolodurin.urlshortener.controller;

import org.springframework.ui.Model;

public interface MainController {
    String resolvePath(String link);

    String makeOrReturnUrl(Model model, String longUrl);

}
