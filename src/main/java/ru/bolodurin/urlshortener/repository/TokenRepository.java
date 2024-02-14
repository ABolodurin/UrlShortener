package ru.bolodurin.urlshortener.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.bolodurin.urlshortener.model.Token;

@Repository
public interface TokenRepository extends CrudRepository<Token, String> {
    Token findByLongUrl(String longUrl);

}
