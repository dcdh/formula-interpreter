package com.damdamdeo.formula.infrastructure;

import com.damdamdeo.formula.domain.NumericalContext;
import com.damdamdeo.formula.domain.ProcessedAt;
import com.damdamdeo.formula.domain.spi.*;
import com.damdamdeo.formula.domain.usecase.EvaluateUseCase;
import com.damdamdeo.formula.domain.usecase.SuggestUseCase;
import com.damdamdeo.formula.domain.usecase.ValidateUseCase;
import com.damdamdeo.formula.infrastructure.evaluation.antlr.DefaultAntlrLoaded;
import com.damdamdeo.formula.infrastructure.evaluation.expression.DefaultAntlrMappingExpressionLoaded;
import com.damdamdeo.formula.infrastructure.parser.antlr.AntlrSuggestCompletion;
import com.damdamdeo.formula.infrastructure.parser.antlr.AntlrSyntaxError;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

import java.time.ZonedDateTime;

public class Application {

    private static final class ZonedDateTimeProcessedAtProvider implements ProcessedAtProvider {
        @Override
        public ProcessedAt now() {
            return new ProcessedAt(ZonedDateTime.now());
        }
    }

    @Produces
    @ApplicationScoped
    public ProcessedAtProvider processedAtProviderProducer() {
        return new ZonedDateTimeProcessedAtProvider();
    }

    @Produces
    @ApplicationScoped
    public EvaluateUseCase<DefaultAntlrLoaded, DefaultAntlrMappingExpressionLoaded> evaluateUseCaseProducer(final EvaluationPipeline<DefaultAntlrLoaded> antlrEvaluationPipeline,
                                                                                                            final EvaluationPipeline<DefaultAntlrMappingExpressionLoaded> expressionEvaluationPipeline,
                                                                                                            final CacheRepository cacheRepository,
                                                                                                            final ProcessedAtProvider processedAtProvider) {
        return new EvaluateUseCase<>(antlrEvaluationPipeline, expressionEvaluationPipeline, cacheRepository, processedAtProvider,
                new NumericalContext());
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
