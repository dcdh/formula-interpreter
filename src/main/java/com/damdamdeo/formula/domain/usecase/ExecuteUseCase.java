package com.damdamdeo.formula.domain.usecase;

import com.damdamdeo.formula.domain.ExecutionResult;
import com.damdamdeo.formula.domain.Executor;
import com.damdamdeo.formula.domain.UseCase;
import io.smallrye.mutiny.Uni;

import java.util.Objects;

public final class ExecuteUseCase implements UseCase<ExecutionResult, ExecuteCommand> {
    private final Executor executor;

    public ExecuteUseCase(final Executor executor) {
        this.executor = Objects.requireNonNull(executor);
    }

    @Override
    public Uni<ExecutionResult> execute(final ExecuteCommand command) {
        return executor.execute(command.formula(), command.structuredData(), command.debugFeature());
    }
}
