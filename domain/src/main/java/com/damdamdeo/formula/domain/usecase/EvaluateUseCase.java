package com.damdamdeo.formula.domain.usecase;

import com.damdamdeo.formula.domain.*;
import com.damdamdeo.formula.domain.evaluation.Evaluator;
import com.damdamdeo.formula.domain.spi.EvaluatedAtProvider;
import com.damdamdeo.formula.domain.spi.Parser;
import io.smallrye.mutiny.Uni;

import java.util.List;
import java.util.Objects;

public final class EvaluateUseCase implements UseCase<EvaluationResult, EvaluateCommand> {
    private final Parser parser;
    private final EvaluatedAtProvider evaluatedAtProvider;
    private final NumericalContext numericalContext;

    public EvaluateUseCase(final Parser parser,
                           final EvaluatedAtProvider evaluatedAtProvider,
                           final NumericalContext numericalContext) {
        this.parser = Objects.requireNonNull(parser);
        this.evaluatedAtProvider = Objects.requireNonNull(evaluatedAtProvider);
        this.numericalContext = Objects.requireNonNull(numericalContext);
    }

    @Override
    public Uni<EvaluationResult> execute(final EvaluateCommand command) {
        final PartEvaluationListener partEvaluationListener = switch (command.debugFeature()) {
            case ACTIVE -> new DebugPartEvaluationListener(evaluatedAtProvider);
            case INACTIVE -> new NoOpPartEvaluationListener();
        };
        return switch (command.evaluateOn()) {
            case ANTLR ->
                    parser.process(command.formula(), new PartEvaluationCallback(partEvaluationListener, numericalContext, command.structuredReferences()));
            case ANTLR_MAPPING_DOMAIN_EVAL -> parser.process(command.formula())
                    .map(processingResult -> {
                        final EvaluatedAtStart evaluatedAtStart = evaluatedAtProvider.now();
                        final Evaluator evaluator = new Evaluator(new NumericalContext(), command.structuredReferences(), partEvaluationListener);
                        final Evaluated evaluated = processingResult.expression().accept(evaluator);
                        final List<IntermediateResult> intermediateResults = evaluator.intermediateResults();
                        final EvaluatedAtEnd evaluatedAtEnd = evaluatedAtProvider.now();
                        return new EvaluationResult(evaluated.value(),
                                processingResult.parserEvaluationProcessedIn(),
                                intermediateResults,
                                new EvaluationProcessedIn(evaluatedAtStart, evaluatedAtEnd));
                    });
        };
    }
}
