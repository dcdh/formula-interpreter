package com.damdamdeo.formula.domain.usecase;

import com.damdamdeo.formula.domain.*;
import com.damdamdeo.formula.domain.spi.EvaluatedAtProvider;
import com.damdamdeo.formula.domain.spi.Parser;
import io.smallrye.mutiny.Uni;

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
        final PartEvaluationCallbackListener partEvaluationCallbackListener = switch (command.debugFeature()) {
            case ACTIVE -> new LoggingPartEvaluationCallbackListener(evaluatedAtProvider);
            case INACTIVE -> new NoOpPartEvaluationCallbackListener();
        };
        return parser.process(command.formula(),
                new PartEvaluationCallback(partEvaluationCallbackListener, numericalContext, command.structuredReferences()));
    }
}
