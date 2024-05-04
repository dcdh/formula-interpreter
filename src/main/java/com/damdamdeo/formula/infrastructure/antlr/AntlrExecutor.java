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
        // I would like to do all the treatment in only one block.
        // However, it is not possible because the cache used regarding the antlrParseTreeGenerator is async and I cannot wait on it
        // because it is forbidden inside the event loop.
        // So the time to execute the formula do not reflect the reality because the processing can be hanged by processing
        // another formula
        // I would like to have a sync get value from cache, but it is not provided by quarkus cache.
        return Uni.createFrom().item(executedAtProvider::now)
                .chain(executedAtStart -> antlrParseTreeGenerator.generate(formula)
                        .chain(generatorResult ->
                                Uni.createFrom().item(() -> {
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
                        ))
                .onFailure(Exception.class)
                .transform(ExecutionException::new);
    }
}
