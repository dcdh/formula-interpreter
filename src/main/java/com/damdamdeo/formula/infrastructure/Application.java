package com.damdamdeo.formula.infrastructure;

import com.damdamdeo.formula.domain.*;
import com.damdamdeo.formula.domain.usecase.ExecuteUseCase;
import com.damdamdeo.formula.domain.usecase.SuggestUseCase;
import com.damdamdeo.formula.domain.usecase.ValidateUseCase;
import com.damdamdeo.formula.infrastructure.antlr.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

import java.time.ZonedDateTime;

public class Application {

    private static final class NowExecutedAtProvider implements ExecutedAtProvider {
        @Override
        public ExecutedAt now() {
            return new ExecutedAt(ZonedDateTime.now());
        }
    }

    @Produces
    @ApplicationScoped
    public ExecutedAtProvider executedAddProviderProducer() {
        return new NowExecutedAtProvider();
    }

    @Produces
    @ApplicationScoped
    public AntlrParseTreeGenerator antlrParseTreeGenerator() {
        return new AntlrParseTreeGenerator();
    }

    @Produces
    @ApplicationScoped
    public Validator<AntlrSyntaxError> validatorProducer(final AntlrParseTreeGenerator antlrParseTreeGenerator) {
        return new AntlrValidator(antlrParseTreeGenerator);
    }

    @Produces
    @ApplicationScoped
    public Executor executorProducer(final ExecutedAtProvider executedAtProvider,
                                     final AntlrParseTreeGenerator antlrParseTreeGenerator) {
        return new AntlrExecutor(executedAtProvider, new NumericalContext(), antlrParseTreeGenerator);
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
