package com.damdamdeo.formula.infrastructure;

import com.damdamdeo.formula.domain.*;
import com.damdamdeo.formula.domain.usecase.ExecuteUseCase;
import com.damdamdeo.formula.domain.usecase.SuggestUseCase;
import com.damdamdeo.formula.domain.usecase.ValidateUseCase;
import com.damdamdeo.formula.infrastructure.antlr.*;
import com.damdamdeo.formula.infrastructure.logger.InMemoryExecutionLogger;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

import java.time.ZonedDateTime;
import java.util.UUID;

public class Application {

    @Produces
    @ApplicationScoped
    public ExecutedAtProvider executedAddProviderProducer() {
        return () -> new ExecutedAt(ZonedDateTime.now());
    }

    @Produces
    @ApplicationScoped
    public ExecutionIdGenerator executionIdGeneratorProducer() {
        return () -> new ExecutionId(UUID.randomUUID());
    }

    @Produces
    @ApplicationScoped
    public ExecutionLogger executionLoggerProducer() {
        return new InMemoryExecutionLogger();
    }

    @Produces
    @ApplicationScoped
    public Validator<AntlrSyntaxError> validatorProducer() {
        return new AntlrValidator();
    }

    @Produces
    @ApplicationScoped
    public Executor executorProducer(final ExecutionIdGenerator executionIdGenerator,
                                     final ExecutionLogger executionLogger,
                                     final ExecutedAtProvider executedAtProvider) {
        return new AntlrExecutor(executionIdGenerator, executionLogger, executedAtProvider, new NumericalContext());
    }

    @Produces
    @ApplicationScoped
    public ExecuteUseCase executeUseCaseProducer(final Executor executor) {
        return new ExecuteUseCase(executor);
    }

    @Produces
    @ApplicationScoped
    public SuggestCompletion suggestCompletionProducer() {
        return new AntlrSuggestCompletion();
    }

    @Produces
    @ApplicationScoped
    public SuggestUseCase suggestUseCaseProducer(final SuggestCompletion suggestCompletion) {
        return new SuggestUseCase(suggestCompletion);
    }

    @Produces
    @ApplicationScoped
    public ValidateUseCase<AntlrSyntaxError> validateUseCase(final Validator<AntlrSyntaxError> validator) {
        return new ValidateUseCase<>(validator);
    }

}
