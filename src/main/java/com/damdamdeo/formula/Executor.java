package com.damdamdeo.formula;

import com.damdamdeo.formula.structuredreference.StructuredData;
import com.damdamdeo.formula.syntax.SyntaxErrorException;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.List;
import java.util.Objects;

public final class Executor {

    private final ExecutionIdGenerator executionIdGenerator;
    private final ExecutionLogger executionLogger;
    private final Validator validator;
    private final NumericalContext numericalContext;

    public Executor(final ExecutionIdGenerator executionIdGenerator,
                    final ExecutionLogger executionLogger,
                    final Validator validator,
                    final NumericalContext numericalContext) {
        this.executionIdGenerator = Objects.requireNonNull(executionIdGenerator);
        this.executionLogger = Objects.requireNonNull(executionLogger);
        this.validator = Objects.requireNonNull(validator);
        this.numericalContext = Objects.requireNonNull(numericalContext);
    }

    public ExecutionResult execute(final Formula formula,
                                   final StructuredData structuredData) throws SyntaxErrorException {
        final ParseTree tree = validator.validate(formula);
        final ExecutionId executionId = executionIdGenerator.generate();
        final EvalVisitor visitor = new EvalVisitor(executionId, executionLogger, structuredData, numericalContext);
        final Result value = visitor.visit(tree);
        if (value == null) {
            throw new IllegalStateException("Should not be null - a response is expected");
        }
        final List<Execution> executions = executionLogger.getByExecutionId(executionId);
        return new ExecutionResult(value, executions);
    }

}
