package com.damdamdeo.formula.domain.usecase;

import com.damdamdeo.formula.domain.*;
import com.damdamdeo.formula.domain.spi.ExecutedAtProvider;
import com.damdamdeo.formula.domain.spi.Executor;
import io.smallrye.mutiny.Uni;

import java.util.Objects;

public final class ExecuteUseCase implements UseCase<ExecutionResult, ExecuteCommand> {
    private final Executor executor;
    private final ExecutedAtProvider executedAtProvider;

    public ExecuteUseCase(final Executor executor,
                          final ExecutedAtProvider executedAtProvider) {
        this.executor = Objects.requireNonNull(executor);
        this.executedAtProvider = Objects.requireNonNull(executedAtProvider);
    }

    @Override
    public Uni<ExecutionResult> execute(final ExecuteCommand command) {
        final ExecutionWrapper executionWrapper = switch (command.debugFeature()) {
            case ACTIVE -> new LoggingExecutionWrapper(executedAtProvider);
            case INACTIVE -> new NoOpExecutionWrapper();
        };
        return executor.execute(command.formula(), command.structuredReferences(), executionWrapper);
    }
}
