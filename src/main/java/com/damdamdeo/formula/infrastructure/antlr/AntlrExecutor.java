package com.damdamdeo.formula.infrastructure.antlr;

import com.damdamdeo.formula.domain.*;
import io.smallrye.mutiny.Uni;

import java.util.List;
import java.util.Objects;

public final class AntlrExecutor implements Executor {

    private final ExecutedAtProvider executedAtProvider;
    private final NumericalContext numericalContext;
    private final AntlrParseTreeGenerator antlrParseTreeGenerator;

    public AntlrExecutor(final ExecutedAtProvider executedAtProvider,
                         final NumericalContext numericalContext,
                         final AntlrParseTreeGenerator antlrParseTreeGenerator) {
        this.executedAtProvider = Objects.requireNonNull(executedAtProvider);
        this.numericalContext = Objects.requireNonNull(numericalContext);
        this.antlrParseTreeGenerator = Objects.requireNonNull(antlrParseTreeGenerator);
    }

    @Override
    public Uni<ExecutionResult> execute(final Formula formula,
                                        final StructuredData structuredData,
                                        final ExecutionWrapper executionWrapper) {
        final ExecutedAtStart executedAtStart = executedAtProvider.now();
        return antlrParseTreeGenerator.generate(formula)
                .onItem().transform(AntlrParseTreeGenerator.GeneratorResult::parseTree)
                .onItem().transformToUni(parseTree ->
                        Uni.createFrom().item(() -> {
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
