package com.damdamdeo.formula.infrastructure.antlr;

import com.damdamdeo.formula.domain.*;
import com.damdamdeo.formula.domain.spi.ExecutedAtProvider;
import com.damdamdeo.formula.domain.spi.Executor;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.unchecked.Unchecked;

import java.util.List;
import java.util.Objects;

public final class AntlrExecutor implements Executor {

    private final ExecutedAtProvider executedAtProvider;
    private final AntlrParseTreeGenerator antlrParseTreeGenerator;

    public AntlrExecutor(final ExecutedAtProvider executedAtProvider,
                         final AntlrParseTreeGenerator antlrParseTreeGenerator) {
        this.executedAtProvider = Objects.requireNonNull(executedAtProvider);
        this.antlrParseTreeGenerator = Objects.requireNonNull(antlrParseTreeGenerator);
    }

    @Override
    public Uni<ExecutionResult> execute(final Formula formula,
                                        final PartExecutionCallback partExecutionCallback) {
        return antlrParseTreeGenerator.generate(formula)
                .chain(generatorResult ->
                        Uni.createFrom().item(Unchecked.supplier(() -> {
                            final ExecutedAtStart executedAtStart = executedAtProvider.now();
                            final EvalVisitor visitor = new EvalVisitor(partExecutionCallback);
                            final Result result = visitor.visit(generatorResult.parseTree());
                            if (result == null) {
                                throw new IllegalStateException("Should not be null - a response is expected");
                            }
                            final List<IntermediateResult> intermediateResults = partExecutionCallback.intermediateResults();
                            final ExecutedAtEnd executedAtEnd = executedAtProvider.now();
                            return new ExecutionResult(result,
                                    generatorResult.parserExecutionProcessedIn(),
                                    intermediateResults,// TODO should not be here ...
                                    new ExecutionProcessedIn(executedAtStart, executedAtEnd));
                        }))
                )
                .onFailure(Exception.class)
                .transform(ExecutionException::new);
    }
}
