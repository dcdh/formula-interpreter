package com.damdamdeo.formula.domain.usecase;

import com.damdamdeo.formula.domain.*;
import com.damdamdeo.formula.domain.spi.ExecutedAtProvider;
import com.damdamdeo.formula.domain.spi.Executor;
import io.smallrye.mutiny.Uni;

import java.util.Objects;

public final class ExecuteUseCase implements UseCase<ExecutionResult, ExecuteCommand> {
    private final Executor executor;
    private final ExecutedAtProvider executedAtProvider;
    private final NumericalContext numericalContext;

    public ExecuteUseCase(final Executor executor,
                          final ExecutedAtProvider executedAtProvider,
                          final NumericalContext numericalContext) {
        this.executor = Objects.requireNonNull(executor);
        this.executedAtProvider = Objects.requireNonNull(executedAtProvider);
        this.numericalContext = Objects.requireNonNull(numericalContext);
    }

    @Override
    public Uni<ExecutionResult> execute(final ExecuteCommand command) {
        final PartExecutionCallbackListener partExecutionCallbackListener = switch (command.debugFeature()) {
            case ACTIVE -> new LoggingPartExecutionCallbackListener(executedAtProvider);
            case INACTIVE -> new NoOpPartExecutionCallbackListener();
        };
        return executor.execute(command.formula(),
                new PartExecutionCallback(partExecutionCallbackListener, numericalContext, command.structuredReferences()));
    }
}
