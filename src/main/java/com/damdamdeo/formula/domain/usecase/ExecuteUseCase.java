package com.damdamdeo.formula.domain.usecase;

import com.damdamdeo.formula.infrastructure.antlr.SyntaxErrorException;
import com.damdamdeo.formula.domain.ExecutionResult;
import com.damdamdeo.formula.domain.Executor;
import com.damdamdeo.formula.domain.UseCase;

import java.util.Objects;

public final class ExecuteUseCase implements UseCase<ExecutionResult, ExecuteCommand> {
    private final Executor executor;

    public ExecuteUseCase(final Executor executor) {
        this.executor = Objects.requireNonNull(executor);
    }

    @Override
    public ExecutionResult execute(final ExecuteCommand command) throws SyntaxErrorException {
        return executor.execute(command.formula(), command.structuredData());
    }
}
