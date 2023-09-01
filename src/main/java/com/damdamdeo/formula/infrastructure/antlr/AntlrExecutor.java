package com.damdamdeo.formula.infrastructure.antlr;

import com.damdamdeo.formula.domain.*;
import io.smallrye.mutiny.Uni;

import java.util.List;
import java.util.Objects;

public final class AntlrExecutor implements Executor {

    private final ExecutedAtProvider executedAtProvider;
    private final AntlrValidator antlrValidator;
    private final NumericalContext numericalContext;

    public AntlrExecutor(final ExecutedAtProvider executedAtProvider,
                         final NumericalContext numericalContext) {
        this.executedAtProvider = Objects.requireNonNull(executedAtProvider);
        this.antlrValidator = new AntlrValidator();
        this.numericalContext = Objects.requireNonNull(numericalContext);
    }

    @Override
    public Uni<ExecutionResult> execute(final Formula formula,
                                        final StructuredData structuredData,
                                        final DebugFeature debugFeature) throws ExecutionException {
        final ExecutedAtStart executedAtStart = executedAtProvider.now();
        return antlrValidator.doValidate(formula)
                .onItem().transformToUni(parseTree ->
                        Uni.createFrom().item(() -> {
                            final ExecutionWrapper executionWrapper = switch (debugFeature) {
                                case ACTIVE -> new LoggingExecutionWrapper(executedAtProvider);
                                case INACTIVE -> new NoOpExecutionWrapper();
                            };
                            final EvalVisitor visitor = new EvalVisitor(executionWrapper, structuredData, numericalContext);
                            final Result result = visitor.visit(parseTree);
                            if (result == null) {
                                throw new IllegalStateException("Should not be null - a response is expected");
                            }
                            final List<ElementExecution> elementExecutions = executionWrapper.executions();
                            final ExecutedAtEnd executedAtEnd = executedAtProvider.now();
                            return new ExecutionResult(result,
                                    elementExecutions,
                                    new ExecutionProcessedIn(executedAtStart, executedAtEnd));
                        })
                )
                .onFailure(Exception.class)
                .transform(ExecutionException::new);
    }
}
