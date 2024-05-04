package com.damdamdeo.formula.infrastructure;

import com.damdamdeo.formula.domain.*;
import com.damdamdeo.formula.domain.spi.ExecutedAtProvider;
import com.damdamdeo.formula.domain.spi.Executor;
import com.damdamdeo.formula.domain.spi.SuggestCompletion;
import com.damdamdeo.formula.domain.spi.Validator;
import com.damdamdeo.formula.domain.usecase.ExecuteUseCase;
import com.damdamdeo.formula.domain.usecase.SuggestUseCase;
import com.damdamdeo.formula.domain.usecase.ValidateUseCase;
import com.damdamdeo.formula.infrastructure.antlr.*;
import io.quarkus.cache.Cache;
import io.quarkus.cache.CacheName;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

import java.time.ZonedDateTime;

public class Application {

    private static final class ZonedDateTimeExecutedAtProvider implements ExecutedAtProvider {
        @Override
        public ExecutedAt now() {
            return new ExecutedAt(ZonedDateTime.now());
        }
    }

    @Produces
    @ApplicationScoped
    public ExecutedAtProvider executedAddProviderProducer() {
        return new ZonedDateTimeExecutedAtProvider();
    }

    @Produces
    @ApplicationScoped
    @DefaultGenerator
    public AntlrParseTreeGenerator defaultAntlrParseTreeGeneratorProducer(final ExecutedAtProvider executedAtProvider) {
        return new DefaultAntlrParseTreeGenerator(executedAtProvider);
    }

    @Produces
    @ApplicationScoped
    @CachedGenerator
    public AntlrParseTreeGenerator cachedAntlrParseTreeGeneratorProducer(@DefaultGenerator final AntlrParseTreeGenerator antlrParseTreeGenerator,
                                                                         @CacheName("formula") final Cache cache) {
        return new CachedAntlrParseTreeGenerator(antlrParseTreeGenerator, cache);
    }

    @Produces
    @ApplicationScoped
    public Validator<AntlrSyntaxError> validatorProducer(@DefaultGenerator final AntlrParseTreeGenerator antlrParseTreeGenerator) {
        return new AntlrValidator(antlrParseTreeGenerator);
    }

    @Produces
    @ApplicationScoped
    public Executor executorProducer(final ExecutedAtProvider executedAtProvider,
                                     @CachedGenerator final AntlrParseTreeGenerator antlrParseTreeGenerator) {
        return new AntlrExecutor(executedAtProvider, new NumericalContext(), antlrParseTreeGenerator);
    }

    @Produces
    @ApplicationScoped
    public ExecuteUseCase executeUseCaseProducer(final Executor executor,
                                                 final ExecutedAtProvider executedAtProvider) {
        return new ExecuteUseCase(executor, executedAtProvider);
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
    public ValidateUseCase<AntlrSyntaxError> validateUseCaseProducer(final Validator<AntlrSyntaxError> validator) {
        return new ValidateUseCase<>(validator);
    }

}
