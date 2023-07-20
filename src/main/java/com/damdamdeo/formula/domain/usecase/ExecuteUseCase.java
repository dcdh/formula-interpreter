package com.damdamdeo.formula.domain.usecase;

import com.damdamdeo.formula.domain.ExecutionException;
import com.damdamdeo.formula.domain.SyntaxErrorException;
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
        try {
            return executor.execute(command.formula(), command.structuredData());
        } catch (final SyntaxErrorException syntaxErrorException) {
            throw new ExecutionException(syntaxErrorException);
        } catch (final Exception exception) {
            throw new ExecutionException(exception);
        }
    }
}
