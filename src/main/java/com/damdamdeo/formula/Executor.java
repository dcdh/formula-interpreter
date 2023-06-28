package com.damdamdeo.formula;

import com.damdamdeo.formula.result.ExecutionResult;
import com.damdamdeo.formula.structuredreference.StructuredData;
import com.damdamdeo.formula.syntax.SyntaxErrorException;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.Objects;

public final class Executor {

    private final Validator validator;
    private final NumericalContext numericalContext;

    public Executor(final Validator validator,
                    final NumericalContext numericalContext) {
        this.validator = Objects.requireNonNull(validator);
        this.numericalContext = Objects.requireNonNull(numericalContext);
    }

    public ExecutionResult execute(final Formula formula, final StructuredData structuredData) throws SyntaxErrorException {
        final ParseTree tree = validator.validate(formula);
        final EvalVisitor visitor = new EvalVisitor(structuredData, numericalContext);
        visitor.visit(tree);
        return new ExecutionResult(visitor.result());
    }

}
