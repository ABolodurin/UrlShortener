package ru.bolodurin.urlshortener.service;

import io.seruco.encoding.base62.Base62;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
public class TokenEncoderImpl implements TokenEncoder {
    private final Base62 base62 = Base62.createInstance();

    @Override
    public String encode(String longUrl) {
        byte[] digest = DigestUtils.md5Digest(longUrl.getBytes());
        byte[] encoded = base62.encode(digest);
        return new String(encoded).substring(0, 15);
    }

}
