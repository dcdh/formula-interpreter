package com.damdamdeo.formula.infrastructure.antlr;

import com.damdamdeo.formula.domain.Value;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.concurrent.Callable;

public final class NoOpExecutionWrapper implements ExecutionWrapper {
    @Override
    public Value execute(final Callable<EvalVisitor.ExecutionResult> callable,
                         final ParserRuleContext parserRuleContext) {
        try {
            final EvalVisitor.ExecutionResult result = callable.call();
            return result.value();
        } catch (final Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
