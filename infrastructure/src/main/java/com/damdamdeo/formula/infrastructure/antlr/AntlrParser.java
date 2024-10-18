package com.damdamdeo.formula.infrastructure.antlr;

import com.damdamdeo.formula.domain.*;
import com.damdamdeo.formula.domain.spi.EvaluatedAtProvider;
import com.damdamdeo.formula.domain.spi.Parser;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.unchecked.Unchecked;

import java.util.List;
import java.util.Objects;

public final class AntlrParser implements Parser {

    private final EvaluatedAtProvider evaluatedAtProvider;
    private final AntlrParseTreeGenerator antlrParseTreeGenerator;

    public AntlrParser(final EvaluatedAtProvider evaluatedAtProvider,
                       final AntlrParseTreeGenerator antlrParseTreeGenerator) {
        this.evaluatedAtProvider = Objects.requireNonNull(evaluatedAtProvider);
        this.antlrParseTreeGenerator = Objects.requireNonNull(antlrParseTreeGenerator);
    }

    @Override
    public Uni<EvaluationResult> process(final Formula formula,
                                         final PartEvaluationCallback partEvaluationCallback) {
        return antlrParseTreeGenerator.generate(formula)
                .chain(generatorResult ->
                        Uni.createFrom().item(Unchecked.supplier(() -> {
                            final EvaluatedAtStart evaluatedAtStart = evaluatedAtProvider.now();
                            final AntlrEvalVisitor antlrEvalVisitor = new AntlrEvalVisitor(partEvaluationCallback);
                            final Result result = antlrEvalVisitor.visit(generatorResult.parseTree());
                            if (result == null) {
                                throw new IllegalStateException("Should not be null - a response is expected");
                            }
                            final List<IntermediateResult> intermediateResults = partEvaluationCallback.intermediateResults();
                            final EvaluatedAtEnd evaluatedAtEnd = evaluatedAtProvider.now();
                            return new EvaluationResult(result,
                                    generatorResult.parserEvaluationProcessedIn(),
                                    intermediateResults,// TODO should not be here ...
                                    new EvaluationProcessedIn(evaluatedAtStart, evaluatedAtEnd));
                        }))
                )
                .onFailure(Exception.class)
                .transform(EvaluationException::new);
    }
}
