package com.damdamdeo.formula.infrastructure.antlr;

import com.damdamdeo.formula.domain.*;
import com.damdamdeo.formula.domain.spi.ExecutedAtProvider;
import com.damdamdeo.formula.domain.spi.Executor;
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
        return antlrParseTreeGenerator.generate(formula)
                .chain(generatorResult ->
                        Uni.createFrom().item(() -> {
                            final ExecutedAtStart executedAtStart = executedAtProvider.now();
                            final EvalVisitor visitor = new EvalVisitor(executionWrapper, structuredData, numericalContext);
                            final Result result = visitor.visit(generatorResult.parseTree());
                            if (result == null) {
                                throw new IllegalStateException("Should not be null - a response is expected");
                            }
                            final List<ElementExecution> elementExecutions = executionWrapper.executions();
                            final ExecutedAtEnd executedAtEnd = executedAtProvider.now();
                            return new ExecutionResult(result,
                                    generatorResult.parserExecutionProcessedIn(),
                                    elementExecutions,
                                    new ExecutionProcessedIn(executedAtStart, executedAtEnd));
                        })
                )
                .onFailure(Exception.class)
                .transform(ExecutionException::new);
    }
}
