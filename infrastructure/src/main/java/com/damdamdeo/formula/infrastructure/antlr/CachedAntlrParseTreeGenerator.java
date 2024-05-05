package com.damdamdeo.formula.infrastructure.antlr;

import com.damdamdeo.formula.domain.Formula;
import io.quarkus.cache.Cache;
import io.smallrye.mutiny.Uni;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public final class CachedAntlrParseTreeGenerator implements AntlrParseTreeGenerator {
    private final AntlrParseTreeGenerator antlrParseTreeGenerator;
    private final Cache cache;

    public CachedAntlrParseTreeGenerator(final AntlrParseTreeGenerator antlrParseTreeGenerator,
                                         final Cache cache) {
        this.antlrParseTreeGenerator = Objects.requireNonNull(antlrParseTreeGenerator);
        this.cache = Objects.requireNonNull(cache);
    }

    @Override
    public Uni<GeneratorResult> generate(Formula formula) {
        final AtomicBoolean putInCache = new AtomicBoolean(false);
        return cache.getAsync(formula, (__) -> {
                    putInCache.set(true);
                    return antlrParseTreeGenerator.generate(formula);
                })
                .map(generatorResult -> new GeneratorResult(
                        generatorResult.formula(),
                        generatorResult.parseTree(),
                        generatorResult.antlrSyntaxErrorListener(),
                        Boolean.TRUE.equals(putInCache.get()) ? generatorResult.parserExecutionProcessedIn() : null // In this case I consider that retrieving from cache takes zero time
                ));
    }
}