package com.damdamdeo.formula.infrastructure.antlr;

import com.damdamdeo.formula.domain.Formula;
import io.quarkus.cache.Cache;
import io.smallrye.mutiny.Uni;

import java.util.Objects;

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
        return cache.getAsync(formula, antlrParseTreeGenerator::generate);
    }
}