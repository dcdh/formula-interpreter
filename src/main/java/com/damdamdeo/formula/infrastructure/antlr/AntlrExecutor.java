package com.damdamdeo.formula.infrastructure.antlr;

import com.damdamdeo.formula.domain.*;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.List;
import java.util.Objects;

public final class AntlrExecutor implements Executor {

    private final ExecutionIdGenerator executionIdGenerator;
    private final ExecutionLogger executionLogger;
    private final ExecutedAtProvider executedAtProvider;
    private final AntlrValidator antlrValidator;
    private final NumericalContext numericalContext;

    public AntlrExecutor(final ExecutionIdGenerator executionIdGenerator,
                         final ExecutionLogger executionLogger,
                         final ExecutedAtProvider executedAtProvider,
                         final AntlrValidator antlrValidator,
                         final NumericalContext numericalContext) {
        this.executionIdGenerator = Objects.requireNonNull(executionIdGenerator);
        this.executionLogger = Objects.requireNonNull(executionLogger);
        this.executedAtProvider = Objects.requireNonNull(executedAtProvider);
        this.antlrValidator = Objects.requireNonNull(antlrValidator);
        this.numericalContext = Objects.requireNonNull(numericalContext);
    }

    @Override
    public ExecutionResult execute(final Formula formula,
                                   final StructuredData structuredData) throws SyntaxErrorException {
        final ParseTree tree = antlrValidator.doValidate(formula);
        final ExecutionId executionId = executionIdGenerator.generate();
        final EvalVisitor visitor = new EvalVisitor(executionId, executionLogger, executedAtProvider, structuredData, numericalContext);
        final Result value = visitor.visit(tree);
        if (value == null) {
            throw new IllegalStateException("Should not be null - a response is expected");
        }
        final List<Execution> executions = executionLogger.getByExecutionId(executionId);
        return new ExecutionResult(value, executions);
    }

}
