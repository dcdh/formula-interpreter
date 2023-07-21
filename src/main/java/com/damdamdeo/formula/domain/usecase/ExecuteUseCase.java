package com.damdamdeo.formula.domain.usecase;

import com.damdamdeo.formula.domain.ExecutionException;
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
    public ExecutionResult execute(final ExecuteCommand command) throws ExecutionException {
        return executor.execute(command.formula(), command.structuredData());
    }
}
