package com.damdamdeo.formula.infrastructure;

import com.damdamdeo.formula.domain.DebugPartEvaluationListener;
import com.damdamdeo.formula.domain.NoOpPartEvaluationListener;
import com.damdamdeo.formula.domain.NumericalContext;
import com.damdamdeo.formula.domain.ProcessedAt;
import com.damdamdeo.formula.domain.spi.*;
import com.damdamdeo.formula.domain.usecase.EvaluateUseCase;
import com.damdamdeo.formula.domain.usecase.SuggestUseCase;
import com.damdamdeo.formula.domain.usecase.ValidateUseCase;
import com.damdamdeo.formula.infrastructure.evaluation.antlr.ParseTreeAntlrLoaded;
import com.damdamdeo.formula.infrastructure.evaluation.expression.DefaultAntlrMappingExpressionLoaded;
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
    public DebugPartEvaluationListener debugPartEvaluationListenerProducer(final ProcessedAtProvider processedAtProvider) {
        return new DebugPartEvaluationListener(processedAtProvider);
    }

    @Produces
    @ApplicationScoped
    public NoOpPartEvaluationListener noOpPartEvaluationListenerProducer() {
        return new NoOpPartEvaluationListener();
    }

    @Produces
    @ApplicationScoped
    public EvaluateUseCase<ParseTreeAntlrLoaded, DefaultAntlrMappingExpressionLoaded> evaluateUseCaseProducer(final EvaluationPipeline<ParseTreeAntlrLoaded> antlrEvaluationPipeline,
                                                                                                              final EvaluationPipeline<DefaultAntlrMappingExpressionLoaded> expressionEvaluationPipeline,
                                                                                                              final CacheRepository cacheRepository,
                                                                                                              final ProcessedAtProvider processedAtProvider,
                                                                                                              final DebugPartEvaluationListener debugPartEvaluationListener,
                                                                                                              final NoOpPartEvaluationListener noOpPartEvaluationListener) {
        return new EvaluateUseCase<>(antlrEvaluationPipeline, expressionEvaluationPipeline, cacheRepository, processedAtProvider,
                debugPartEvaluationListener, noOpPartEvaluationListener, new NumericalContext());
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
