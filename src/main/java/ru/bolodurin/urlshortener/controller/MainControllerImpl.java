package ru.bolodurin.urlshortener.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.bolodurin.urlshortener.service.TokenService;

@Controller
@RequiredArgsConstructor
public class MainControllerImpl implements MainController {
    private final TokenService tokenService;
    @Value(value = "${application.token.prefix}")
    private String tokenPrefix;

    @GetMapping("/{link}")
    public String resolvePath(@PathVariable String link) {
        if (!link.startsWith(tokenPrefix)) return link;

        String token = link.replaceFirst(tokenPrefix, "");
        String resolved = tokenService.resolveLink(token);

        return "redirect:/" + resolved;
    }

    @PostMapping("/{longUrl}")
    @ResponseStatus(code = HttpStatus.CREATED)
    public String makeOrReturnUrl(Model model, @PathVariable String longUrl) {
        String token = tokenService.makeShortLink(longUrl);

        model.addAttribute("token", tokenPrefix + token);

        return "created";
    }

}
