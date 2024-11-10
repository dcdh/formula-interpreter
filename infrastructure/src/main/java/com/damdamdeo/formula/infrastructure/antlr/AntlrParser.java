package com.damdamdeo.formula.infrastructure.antlr;

import com.damdamdeo.formula.domain.*;
import com.damdamdeo.formula.domain.spi.EvaluatedAtProvider;
import com.damdamdeo.formula.domain.spi.Parser;
import io.quarkus.cache.Cache;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.unchecked.Unchecked;

import java.util.List;
import java.util.Objects;

public final class AntlrParser implements Parser {

    private static final String PUT_IN_CACHE = "putInCache" + AntlrParser.class.getName();

    private final EvaluatedAtProvider evaluatedAtProvider;
    private final AntlrParseTreeGenerator antlrParseTreeGenerator;
    private final ParserMapping parserMapping;
    private final Cache cache;

    public AntlrParser(final EvaluatedAtProvider evaluatedAtProvider,
                       final AntlrParseTreeGenerator antlrParseTreeGenerator,
                       final ParserMapping parserMapping,
                       final Cache cache) {
        this.evaluatedAtProvider = Objects.requireNonNull(evaluatedAtProvider);
        this.antlrParseTreeGenerator = Objects.requireNonNull(antlrParseTreeGenerator);
        this.parserMapping = Objects.requireNonNull(parserMapping);
        this.cache = Objects.requireNonNull(cache);
    }

    @Override
    public Uni<EvaluationResult> process(final Formula formula,
                                         final PartEvaluationCallback partEvaluationCallback) {
        return antlrParseTreeGenerator.generate(formula)
                .chain(generatorResult ->
                        Uni.createFrom().item(Unchecked.supplier(() -> {
                            final EvaluatedAtStart evaluatedAtStart = evaluatedAtProvider.now();
                            final AntlrEvalVisitor antlrEvalVisitor = new AntlrEvalVisitor(partEvaluationCallback);
                            final Evaluated evaluated = antlrEvalVisitor.visit(generatorResult.parseTree());
                            if (evaluated == null) {
                                throw new IllegalStateException("Should not be null - a response is expected");
                            }
                            final List<IntermediateResult> intermediateResults = partEvaluationCallback.intermediateResults();
                            final EvaluatedAtEnd evaluatedAtEnd = evaluatedAtProvider.now();
                            return new EvaluationResult(evaluated.value(),
                                    generatorResult.parserEvaluationProcessedIn(),
                                    intermediateResults,
                                    new EvaluationProcessedIn(evaluatedAtStart, evaluatedAtEnd));
                        }))
                )
                .onFailure(Exception.class)
                .transform(EvaluationException::new);
    }

    @Override
    public Uni<MappingResult> mapToExpression(final Formula formula) {
        return Uni.createFrom().context(context -> {
            context.put(PUT_IN_CACHE, false);
            return cache.getAsync(formula, (__) -> {
                context.put(PUT_IN_CACHE, true);
                return parserMapping.map(formula);
            });
        });
    }
}
