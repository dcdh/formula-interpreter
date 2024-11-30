package com.damdamdeo.formula.domain.usecase;

import com.damdamdeo.formula.domain.*;
import com.damdamdeo.formula.domain.evaluation.AntlrLoaded;
import com.damdamdeo.formula.domain.evaluation.AntlrMappingExpressionLoaded;
import com.damdamdeo.formula.domain.spi.CacheRepository;
import com.damdamdeo.formula.domain.spi.EvaluationPipeline;
import com.damdamdeo.formula.domain.spi.ProcessedAtProvider;
import com.damdamdeo.formula.domain.usecase.resolver.EvaluateUseCaseTestResolver;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
@ExtendWith(EvaluateUseCaseTest.DomainEvaluateUseCaseTestResolver.class)
class EvaluateUseCaseTest {

    public static class TestAntlrLoaded implements AntlrLoaded {
    }

    public static class TestAntlrMappingExpressionLoaded implements AntlrMappingExpressionLoaded {
    }

    @Mock
    private EvaluationPipeline<TestAntlrLoaded> antlrEvaluationPipeline;

    @Mock
    private EvaluationPipeline<TestAntlrMappingExpressionLoaded> expressionEvaluationPipeline;

    private NumericalContext numericalContext;

    private EvaluateUseCase<TestAntlrLoaded, TestAntlrMappingExpressionLoaded> evaluateUseCase;

    @BeforeEach
    void setup(final DebugPartEvaluationListener debugPartEvaluationListener,
               final NoOpPartEvaluationListener noOpPartEvaluationListener,
               final ProcessedAtProvider processedAtProvider) {
        final CacheRepository cacheRepository = new CacheRepository() {
            @Override
            public <V> V get(final FormulaCacheKey formulaCacheKey, final Function<FormulaCacheKey, V> valueLoader) {
                return valueLoader.apply(formulaCacheKey);
            }
        };
        numericalContext = new NumericalContext();
        evaluateUseCase = new EvaluateUseCase<>(
                antlrEvaluationPipeline, expressionEvaluationPipeline, cacheRepository, processedAtProvider,
                debugPartEvaluationListener, noOpPartEvaluationListener,
                numericalContext);
    }

    @Nested
    class EvaluateUsingAntlr {
        private final EvaluateOn givenEvaluateOn = EvaluateOn.ANTLR;
        private final TestAntlrLoaded givenTestAntlrLoaded = new TestAntlrLoaded();

        @EvaluateUseCaseTestResolver.DebugFeatureActiveTest
        void shouldEvaluateWithDebugFeatureActive(final EvaluateUseCaseTestResolver.GivenHappyPath givenHappyPath,
                                                  final PartEvaluationListener givenPartEvaluationListener) {
            // Given
            doReturn(givenTestAntlrLoaded).when(antlrEvaluationPipeline).load(givenHappyPath.formula());
            doReturn(givenHappyPath.evaluated())
                    .when(antlrEvaluationPipeline).evaluate(givenTestAntlrLoaded, givenPartEvaluationListener, List.of(), numericalContext);

            // When
            final Uni<EvaluationResult> executed = evaluateUseCase.execute(new EvaluateCommand(givenHappyPath.formula(), List.of(),
                    givenHappyPath.debugFeature(), givenEvaluateOn));

            // Then
            final UniAssertSubscriber<EvaluationResult> subscriber = executed.subscribe()
                    .withSubscriber(UniAssertSubscriber.create());

            subscriber.assertCompleted().assertItem(givenHappyPath.evaluationResult());
        }

        @EvaluateUseCaseTestResolver.DebugFeatureInactiveTest
        void shouldEvaluateWithDebugFeatureInactive(final EvaluateUseCaseTestResolver.GivenHappyPath givenHappyPath,
                                                    final PartEvaluationListener givenPartEvaluationListener) {
            // Given
            doReturn(givenTestAntlrLoaded).when(antlrEvaluationPipeline).load(givenHappyPath.formula());
            doReturn(givenHappyPath.evaluated())
                    .when(antlrEvaluationPipeline).evaluate(givenTestAntlrLoaded, givenPartEvaluationListener, List.of(), numericalContext);

            // When
            final Uni<EvaluationResult> executed = evaluateUseCase.execute(new EvaluateCommand(givenHappyPath.formula(), List.of(),
                    givenHappyPath.debugFeature(), givenEvaluateOn));

            // Then
            final UniAssertSubscriber<EvaluationResult> subscriber = executed.subscribe()
                    .withSubscriber(UniAssertSubscriber.create());

            subscriber.assertCompleted().assertItem(givenHappyPath.evaluationResult());
        }

        @EvaluateUseCaseTestResolver.DebugFeatureActiveTest
        @Tag("IllegalStateException")
        void shouldFailToLoadWhenFormulaIsInvalid(final EvaluateUseCaseTestResolver.GivenFailing givenFailing) {
            // Given
            final EvaluateCommand givenEvaluateCommand = new EvaluateCommand(
                    givenFailing.formula(), List.of(), givenFailing.debugFeature(), givenEvaluateOn);

            // When
            final Uni<EvaluationResult> executed = evaluateUseCase.execute(givenEvaluateCommand);

            // Then
            final UniAssertSubscriber<EvaluationResult> subscriber = executed.subscribe()
                    .withSubscriber(UniAssertSubscriber.create());

            final Throwable failure = subscriber.assertFailed().getFailure();
            assertAll(
                    () -> assertThat(failure).isInstanceOf(EvaluationException.class),
                    () -> assertThat(failure).hasCause(givenFailing.cause())
            );
        }
    }

    @Nested
    class EvaluateUsingAntlrMappingDomainEval {
        final EvaluateOn givenEvaluateOn = EvaluateOn.ANTLR_MAPPING_DOMAIN_EVAL;
        final TestAntlrMappingExpressionLoaded givenTestAntlrMappingExpressionLoaded = new TestAntlrMappingExpressionLoaded();

        @EvaluateUseCaseTestResolver.DebugFeatureActiveTest
        void shouldEvaluateWithDebugFeatureActive(final EvaluateUseCaseTestResolver.GivenHappyPath givenHappyPath,
                                                  final PartEvaluationListener givenPartEvaluationListener) {
            // Given
            doReturn(givenTestAntlrMappingExpressionLoaded).when(expressionEvaluationPipeline).load(givenHappyPath.formula());
            doReturn(givenHappyPath.evaluated())
                    .when(expressionEvaluationPipeline).evaluate(givenTestAntlrMappingExpressionLoaded, givenPartEvaluationListener, List.of(), numericalContext);

            // When
            final Uni<EvaluationResult> executed = evaluateUseCase.execute(new EvaluateCommand(givenHappyPath.formula(), List.of(),
                    givenHappyPath.debugFeature(), givenEvaluateOn));

            // Then
            final UniAssertSubscriber<EvaluationResult> subscriber = executed.subscribe()
                    .withSubscriber(UniAssertSubscriber.create());

            // Expected
            // <EvaluationResult[value=Value{value='false'}, intermediateResults=[IntermediateResult[value=Value{value='false'}, positionedAt=PositionedAt[positionStart=PositionStart[start=0], positionEnd=PositionEnd[end=7]], inputs=[Input[name=InputName[name=left], value=Value{value='0'}, positionedAt=PositionedAt[positionStart=PositionStart[start=4], positionEnd=PositionEnd[end=4]]], Input[name=InputName[name=right], value=Value{value='0'}, positionedAt=PositionedAt[positionStart=PositionStart[start=6], positionEnd=PositionEnd[end=6]]]], evaluationProcessedIn=EvaluationProcessedIn[processedAtStart=ProcessedAt[at=2023-12-25T10:15:03+01:00[Europe/Paris]], processedAtEnd=ProcessedAt[at=2023-12-25T10:15:08+01:00[Europe/Paris]]]], IntermediateResult[value=Value{value='0'}, positionedAt=PositionedAt[positionStart=PositionStart[start=4], positionEnd=PositionEnd[end=4]], inputs=[], evaluationProcessedIn=EvaluationProcessedIn[processedAtStart=ProcessedAt[at=2023-12-25T10:15:04+01:00[Europe/Paris]], processedAtEnd=ProcessedAt[at=2023-12-25T10:15:05+01:00[Europe/Paris]]]], IntermediateResult[value=Value{value='0'}, positionedAt=PositionedAt[positionStart=PositionStart[start=6], positionEnd=PositionEnd[end=6]], inputs=[], evaluationProcessedIn=EvaluationProcessedIn[processedAtStart=ProcessedAt[at=2023-12-25T10:15:06+01:00[Europe/Paris]], processedAtEnd=ProcessedAt[at=2023-12-25T10:15:07+01:00[Europe/Paris]]]]], processingMetrics=ProcessingMetrics[formulaCacheRetrieval=MISSED, evaluationLoadingProcessedIn=EvaluationLoadingProcessedIn[processedAtStart=ProcessedAt[at=2023-12-25T10:15+01:00[Europe/Paris]], processedAtEnd=ProcessedAt[at=2023-12-25T10:15:01+01:00[Europe/Paris]]], evaluationProcessedIn=EvaluationProcessedIn[processedAtStart=ProcessedAt[at=2023-12-25T10:15:02+01:00[Europe/Paris]], processedAtEnd=ProcessedAt[at=2023-12-25T10:15:09+01:00[Europe/Paris]]]]]>
            // but received
            // <EvaluationResult[value=Value{value='false'}, intermediateResults=[], processingMetrics=ProcessingMetrics[formulaCacheRetrieval=MISSED, evaluationLoadingProcessedIn=EvaluationLoadingProcessedIn[processedAtStart=ProcessedAt[at=2023-12-25T10:15+01:00[Europe/Paris]], processedAtEnd=ProcessedAt[at=2023-12-25T10:15:01+01:00[Europe/Paris]]], evaluationProcessedIn=EvaluationProcessedIn[processedAtStart=ProcessedAt[at=2023-12-25T10:15:02+01:00[Europe/Paris]], processedAtEnd=ProcessedAt[at=2023-12-25T10:15:03+01:00[Europe/Paris]]]]]>
            subscriber.assertCompleted().assertItem(givenHappyPath.evaluationResult());
        }

        @EvaluateUseCaseTestResolver.DebugFeatureInactiveTest
        void shouldEvaluateWithDebugFeatureInactive(final EvaluateUseCaseTestResolver.GivenHappyPath givenHappyPath,
                                                    final PartEvaluationListener givenPartEvaluationListener) {
            // Given
            doReturn(givenTestAntlrMappingExpressionLoaded).when(expressionEvaluationPipeline).load(givenHappyPath.formula());
            doReturn(givenHappyPath.evaluated())
                    .when(expressionEvaluationPipeline).evaluate(givenTestAntlrMappingExpressionLoaded, givenPartEvaluationListener, List.of(), numericalContext);

            // When
            final Uni<EvaluationResult> executed = evaluateUseCase.execute(new EvaluateCommand(givenHappyPath.formula(), List.of(),
                    givenHappyPath.debugFeature(), givenEvaluateOn));

            // Then
            final UniAssertSubscriber<EvaluationResult> subscriber = executed.subscribe()
                    .withSubscriber(UniAssertSubscriber.create());

            subscriber.assertCompleted().assertItem(givenHappyPath.evaluationResult());
        }

        @EvaluateUseCaseTestResolver.DebugFeatureActiveTest
        @Tag("IllegalStateException")
        void shouldFailToLoadWhenFormulaIsInvalid(final EvaluateUseCaseTestResolver.GivenFailing givenFailing) {
            // Given
            final EvaluateCommand givenEvaluateCommand = new EvaluateCommand(
                    givenFailing.formula(), List.of(), givenFailing.debugFeature(), givenEvaluateOn);

            // When
            final Uni<EvaluationResult> executed = evaluateUseCase.execute(givenEvaluateCommand);

            // Then
            final UniAssertSubscriber<EvaluationResult> subscriber = executed.subscribe()
                    .withSubscriber(UniAssertSubscriber.create());

            final Throwable failure = subscriber.assertFailed().getFailure();
            assertAll(
                    () -> assertThat(failure).isInstanceOf(EvaluationException.class),
                    () -> assertThat(failure).hasCause(givenFailing.cause())
            );
        }
    }

    static class DomainEvaluateUseCaseTestResolver extends EvaluateUseCaseTestResolver {

        @Override
        protected Map<Formula, Throwable> givenFailings() {
            // FCK TODO
            return Map.of(
                    new Formula("true"),
                    new IllegalStateException("Something wrong happened")
            );
        }
    }
}