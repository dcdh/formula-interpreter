package com.damdamdeo.formula.infrastructure.cache.inmemory;

import com.damdamdeo.formula.domain.spi.CacheRepository;
import io.quarkus.cache.Cache;
import io.quarkus.cache.CacheName;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Objects;
import java.util.function.Function;

@ApplicationScoped
public class InMemoryCacheRepository implements CacheRepository {

    private final Cache cache;

    public InMemoryCacheRepository(@CacheName("formula") final Cache cache) {
        this.cache = Objects.requireNonNull(cache);
    }

    @Override
    public <V> V get(final FormulaCacheKey formulaCacheKey, final Function<FormulaCacheKey, V> valueLoader) {
        return cache.get(formulaCacheKey, valueLoader)
                .await().indefinitely();
    }
}
