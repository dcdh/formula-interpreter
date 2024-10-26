package com.damdamdeo.formula.infrastructure.antlr;

import com.damdamdeo.formula.domain.Formula;
import io.quarkus.cache.Cache;
import io.smallrye.mutiny.Uni;

import java.util.Objects;

public final class CachedAntlrParseTreeGenerator implements AntlrParseTreeGenerator {
    private static final String PUT_IN_CACHE = "putInCache" + CachedAntlrParseTreeGenerator.class.getName();

    private final AntlrParseTreeGenerator antlrParseTreeGenerator;
    private final Cache cache;

    public CachedAntlrParseTreeGenerator(final AntlrParseTreeGenerator antlrParseTreeGenerator,
                                         final Cache cache) {
        this.antlrParseTreeGenerator = Objects.requireNonNull(antlrParseTreeGenerator);
        this.cache = Objects.requireNonNull(cache);
    }

    @Override
    public Uni<GeneratorResult> generate(Formula formula) {
        return Uni.createFrom().context(context -> {
            context.put(PUT_IN_CACHE, false);
            return cache.getAsync(formula, (__) -> {
                        context.put(PUT_IN_CACHE, true);
                        return antlrParseTreeGenerator.generate(formula);
                    })
                    .map(generatorResult -> new GeneratorResult(
                            generatorResult.formula(),
                            generatorResult.parseTree(),
                            generatorResult.antlrSyntaxErrorListener(),
                            Boolean.TRUE.equals(context.get(PUT_IN_CACHE)) ? generatorResult.parserEvaluationProcessedIn() : null // In this case I consider that retrieving from cache takes zero time
                    ));
        });
    }
}