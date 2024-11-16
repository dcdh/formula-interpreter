package com.damdamdeo.formula.domain.usecase;

import com.damdamdeo.formula.domain.*;
import com.damdamdeo.formula.domain.evaluation.AntlrLoaded;
import com.damdamdeo.formula.domain.evaluation.AntlrMappingExpressionLoaded;
import com.damdamdeo.formula.domain.evaluation.Loaded;
import com.damdamdeo.formula.domain.spi.CacheRepository;
import com.damdamdeo.formula.domain.spi.EvaluationPipeline;
import com.damdamdeo.formula.domain.spi.ProcessedAtProvider;
import io.smallrye.mutiny.Uni;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public final class EvaluateUseCase<A extends AntlrLoaded, E extends AntlrMappingExpressionLoaded> implements UseCase<EvaluationResult, EvaluateCommand> {

    private final EvaluationPipeline<A> antlrEvaluationPipeline;
    private final EvaluationPipeline<E> expressionEvaluationPipeline;
    private final CacheRepository cacheRepository;
    private final ProcessedAtProvider processedAtProvider;
    private final NumericalContext numericalContext;

    public EvaluateUseCase(final EvaluationPipeline<A> antlrEvaluationPipeline,
                           final EvaluationPipeline<E> expressionEvaluationPipeline,
                           final CacheRepository cacheRepository,
                           final ProcessedAtProvider processedAtProvider,
                           final NumericalContext numericalContext) {
        this.antlrEvaluationPipeline = Objects.requireNonNull(antlrEvaluationPipeline);
        this.expressionEvaluationPipeline = Objects.requireNonNull(expressionEvaluationPipeline);
        this.cacheRepository = Objects.requireNonNull(cacheRepository);
        this.processedAtProvider = Objects.requireNonNull(processedAtProvider);
        this.numericalContext = Objects.requireNonNull(numericalContext);
    }

    @Override
    public Uni<EvaluationResult> execute(final EvaluateCommand command) {
        final PartEvaluationListener partEvaluationListener = switch (command.debugFeature()) {
            case ACTIVE -> new DebugPartEvaluationListener(processedAtProvider);
            case INACTIVE -> new NoOpPartEvaluationListener();
        };
        final List<StructuredReference> structuredReferences = command.structuredReferences();
        final EvaluatePipelineExecution<A> antlrEvaluatePipelineExecution = new EvaluatePipelineExecution<>(
                antlrEvaluationPipeline, partEvaluationListener, cacheRepository, processedAtProvider, structuredReferences, numericalContext);
        final EvaluatePipelineExecution<E> expressionEvaluatePipelineExecution = new EvaluatePipelineExecution<>(
                expressionEvaluationPipeline, partEvaluationListener, cacheRepository, processedAtProvider, structuredReferences, numericalContext);
        final EvaluateOn evaluateOn = command.evaluateOn();
        final Formula formula = command.formula();
        return switch (evaluateOn) {
            case ANTLR -> antlrEvaluatePipelineExecution.execute(formula, evaluateOn);
            case ANTLR_MAPPING_DOMAIN_EVAL -> expressionEvaluatePipelineExecution.execute(formula, evaluateOn);
        };
    }

    public static final class EvaluatePipelineExecution<T extends Loaded> {

        private final EvaluationPipeline<T> evaluationPipeline;
        private final PartEvaluationListener partEvaluationListener;
        private final CacheRepository cacheRepository;
        private final ProcessedAtProvider processedAtProvider;
        private final List<StructuredReference> structuredReferences;
        private final NumericalContext numericalContext;

        public EvaluatePipelineExecution(final EvaluationPipeline<T> evaluationPipeline,
                                         final PartEvaluationListener partEvaluationListener,
                                         final CacheRepository cacheRepository,
                                         final ProcessedAtProvider processedAtProvider,
                                         final List<StructuredReference> structuredReferences,
                                         final NumericalContext numericalContext) {
            this.evaluationPipeline = Objects.requireNonNull(evaluationPipeline);
            this.partEvaluationListener = Objects.requireNonNull(partEvaluationListener);
            this.cacheRepository = Objects.requireNonNull(cacheRepository);
            this.processedAtProvider = Objects.requireNonNull(processedAtProvider);
            this.structuredReferences = Objects.requireNonNull(structuredReferences);
            this.numericalContext = Objects.requireNonNull(numericalContext);
        }

        public Uni<EvaluationResult> execute(final Formula formula, final EvaluateOn evaluateOn) {
            return Uni.createFrom().item(() -> {
                        final ProcessedAtStart processedAtStart = processedAtProvider.now();
                        final AtomicReference<FormulaCacheRetrieval> formulaCacheRetrieval = new AtomicReference<>();
                        formulaCacheRetrieval.set(FormulaCacheRetrieval.IN);
                        final T loaded = cacheRepository.get(new CacheRepository.FormulaCacheKey(formula, evaluateOn),
                                (__) -> {
                                    formulaCacheRetrieval.set(FormulaCacheRetrieval.MISSED);
                                    return evaluationPipeline.load(formula);
                                });
                        final ProcessedAtEnd processedAtEnd = processedAtProvider.now();
                        return new LoadedStageResult<>(loaded,
                                formulaCacheRetrieval.get(),
                                new EvaluationLoadingProcessedIn(processedAtStart, processedAtEnd));
                    })
                    .flatMap(loadedStageResult -> Uni.createFrom().item(() -> {
                        final ProcessedAtStart processedAtStart = processedAtProvider.now();
                        final Evaluated evaluated = evaluationPipeline.evaluate(loadedStageResult.loaded, partEvaluationListener,
                                structuredReferences, numericalContext);
                        final ProcessedAtEnd processedAtEnd = processedAtProvider.now();
                        return new EvaluatedStageResult<>(evaluated,
                                loadedStageResult.formulaCacheRetrieval,
                                loadedStageResult.evaluationLoadingProcessedIn,
                                new EvaluationProcessedIn(processedAtStart, processedAtEnd));
                    }))
                    .map(evaluatedStageResult -> new EvaluationResult(
                            evaluatedStageResult.value(),
                            partEvaluationListener.intermediateResults(),
                            new ProcessingMetrics(
                                    evaluatedStageResult.formulaCacheRetrieval,
                                    evaluatedStageResult.evaluationLoadingProcessedIn,
                                    evaluatedStageResult.evaluationProcessedIn
                            )
                    ))
                    .onFailure(Exception.class)
                    .transform(EvaluationException::new);
        }
    }

    record LoadedStageResult<T extends Loaded>(T loaded, FormulaCacheRetrieval formulaCacheRetrieval,
                                               EvaluationLoadingProcessedIn evaluationLoadingProcessedIn) {
        LoadedStageResult {
            Objects.requireNonNull(loaded);
            Objects.requireNonNull(formulaCacheRetrieval);
            Objects.requireNonNull(evaluationLoadingProcessedIn);
        }
    }

    record EvaluatedStageResult<T extends Loaded>(Evaluated evaluated,
                                                  FormulaCacheRetrieval formulaCacheRetrieval,
                                                  EvaluationLoadingProcessedIn evaluationLoadingProcessedIn,
                                                  EvaluationProcessedIn evaluationProcessedIn) {
        EvaluatedStageResult {
            Objects.requireNonNull(evaluated);
            Objects.requireNonNull(formulaCacheRetrieval);
            Objects.requireNonNull(evaluationLoadingProcessedIn);
            Objects.requireNonNull(evaluationProcessedIn);
        }

        public Value value() {
            return evaluated.value();
        }
    }
}
