package com.damdamdeo.formula.infrastructure.cache.inmemory;

import com.damdamdeo.formula.domain.EvaluateOn;
import com.damdamdeo.formula.domain.Formula;
import com.damdamdeo.formula.domain.spi.CacheRepository;
import io.quarkus.cache.Cache;
import io.quarkus.cache.CacheName;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@QuarkusTest
class InMemoryCacheRepositoryTest {

    @Inject
    InMemoryCacheRepository inMemoryCacheRepository;

    @Inject
    @CacheName("formula")
    Cache cache;

    @BeforeEach
    @AfterEach
    public void setup() {
        cache.invalidateAll().await().indefinitely();
    }

    @Test
    void shouldStoreInCacheWhenNotInCache() {
        // Given
        final AtomicInteger calledCounter = new AtomicInteger(0);
        final CacheRepository.FormulaCacheKey givenKey = new CacheRepository.FormulaCacheKey(
                new Formula("ADD(10,20)"), EvaluateOn.ANTLR_MAPPING_DOMAIN_EVAL);

        // When
        final Integer value = inMemoryCacheRepository.get(givenKey, (__) -> calledCounter.incrementAndGet());

        // Then
        assertAll(
                () -> assertThat(value).isEqualTo(1),
                () -> assertThat(calledCounter.intValue()).isEqualTo(1)
        );
    }

    @Test
    void shouldNotStoreInCacheWhenInCache() {
        // Given
        final AtomicInteger calledCounter = new AtomicInteger(0);
        final CacheRepository.FormulaCacheKey givenKey = new CacheRepository.FormulaCacheKey(
                new Formula("ADD(10,20)"), EvaluateOn.ANTLR_MAPPING_DOMAIN_EVAL);
        inMemoryCacheRepository.get(givenKey, (__) -> calledCounter.incrementAndGet());

        // When
        final Integer value = inMemoryCacheRepository.get(givenKey, (__) -> calledCounter.incrementAndGet());

        // Then
        assertAll(
                () -> assertThat(value).isEqualTo(1),
                () -> assertThat(calledCounter.intValue()).isEqualTo(1)
        );
    }

}