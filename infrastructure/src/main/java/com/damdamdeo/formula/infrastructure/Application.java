package com.damdamdeo.formula.infrastructure;

import com.damdamdeo.formula.domain.*;
import com.damdamdeo.formula.domain.spi.EvaluatedAtProvider;
import com.damdamdeo.formula.domain.spi.Parser;
import com.damdamdeo.formula.domain.spi.SuggestCompletion;
import com.damdamdeo.formula.domain.spi.Validator;
import com.damdamdeo.formula.domain.usecase.EvaluateUseCase;
import com.damdamdeo.formula.domain.usecase.SuggestUseCase;
import com.damdamdeo.formula.domain.usecase.ValidateUseCase;
import com.damdamdeo.formula.infrastructure.antlr.*;
import io.quarkus.cache.Cache;
import io.quarkus.cache.CacheName;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

import java.time.ZonedDateTime;

public class Application {

    private static final class ZonedDateTimeEvaluatedAtProvider implements EvaluatedAtProvider {
        @Override
        public EvaluatedAt now() {
            return new EvaluatedAt(ZonedDateTime.now());
        }
    }

    @Produces
    @ApplicationScoped
    public EvaluatedAtProvider evaluatedAddProviderProducer() {
        return new ZonedDateTimeEvaluatedAtProvider();
    }

    @Produces
    @ApplicationScoped
    @DefaultGenerator
    public AntlrParseTreeGenerator defaultAntlrParseTreeGeneratorProducer(final EvaluatedAtProvider evaluatedAtProvider) {
        return new DefaultAntlrParseTreeGenerator(evaluatedAtProvider);
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
    public Parser parserProducer(final EvaluatedAtProvider evaluatedAtProvider,
                                 @CachedGenerator final AntlrParseTreeGenerator antlrParseTreeGenerator,
                                 final ParserMapping parserMapping,
                                 @CacheName("parser") final Cache cache) {
        return new AntlrParser(evaluatedAtProvider, antlrParseTreeGenerator, parserMapping, cache);
    }

    @Produces
    @ApplicationScoped
    public ParserMapping parserProcessingProducer(final EvaluatedAtProvider evaluatedAtProvider) {
        return new AntlrParserMapping(evaluatedAtProvider);
    }

    @Produces
    @ApplicationScoped
    public EvaluateUseCase evaluateUseCaseProducer(final Parser parser,
                                                   final EvaluatedAtProvider evaluatedAtProvider) {
        return new EvaluateUseCase(parser, evaluatedAtProvider, new NumericalContext());
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
