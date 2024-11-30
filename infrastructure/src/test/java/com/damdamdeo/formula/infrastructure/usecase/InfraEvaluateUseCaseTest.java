package com.damdamdeo.formula.infrastructure.usecase;

import com.damdamdeo.formula.domain.EvaluateOn;
import com.damdamdeo.formula.domain.EvaluationException;
import com.damdamdeo.formula.domain.EvaluationResult;
import com.damdamdeo.formula.domain.Formula;
import com.damdamdeo.formula.domain.spi.ProcessedAtProvider;
import com.damdamdeo.formula.domain.usecase.EvaluateCommand;
import com.damdamdeo.formula.domain.usecase.EvaluateUseCase;
import com.damdamdeo.formula.domain.usecase.resolver.EvaluateUseCaseTestResolver;
import com.damdamdeo.formula.domain.usecase.resolver.ListOfProcessedAtParameterResolver;
import com.damdamdeo.formula.infrastructure.evaluation.antlr.ParseTreeAntlrLoaded;
import com.damdamdeo.formula.infrastructure.evaluation.expression.DefaultAntlrMappingExpressionLoaded;
import com.damdamdeo.formula.infrastructure.parser.antlr.AntlrSyntaxError;
import com.damdamdeo.formula.infrastructure.parser.antlr.AntlrSyntaxErrorException;
import io.quarkus.cache.Cache;
import io.quarkus.cache.CacheName;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.doReturn;

@ExtendWith(InfraEvaluateUseCaseTest.InfraEvaluateUseCaseTestResolver.class)
@ExtendWith(ListOfProcessedAtParameterResolver.class)
@QuarkusTest
class InfraEvaluateUseCaseTest {

    @Inject
    EvaluateUseCase<ParseTreeAntlrLoaded, DefaultAntlrMappingExpressionLoaded> evaluateUseCase;

    @InjectMock
    ProcessedAtProvider processedAtProvider;

    @Inject
    @CacheName("formula")
    Cache cache;

    @BeforeEach
    void setup(final ListOfProcessedAtParameterResolver.ListOfProcessedAt listOfProcessedAt) {
        cache.invalidateAll().await().indefinitely();

        doReturn(listOfProcessedAt.first(), (Object[]) listOfProcessedAt.next()).when(processedAtProvider).now();
    }

    @Nested
    class EvaluateUsingAntlr {

        private final EvaluateOn givenEvaluateOn = EvaluateOn.ANTLR;

        @EvaluateUseCaseTestResolver.DebugFeatureActiveTest
        void shouldEvaluateWithDebugFeatureActive(final EvaluateUseCaseTestResolver.GivenHappyPath givenHappyPath) {
            // Given
            final EvaluateCommand givenEvaluateCommand = new EvaluateCommand(
                    givenHappyPath.formula(), List.of(), givenHappyPath.debugFeature(), givenEvaluateOn);

            // When
            final Uni<EvaluationResult> executed = evaluateUseCase.execute(givenEvaluateCommand);

            // Then
            final UniAssertSubscriber<EvaluationResult> subscriber = executed.subscribe()
                    .withSubscriber(UniAssertSubscriber.create());

            subscriber.assertCompleted().assertItem(givenHappyPath.evaluationResult());
        }

        @EvaluateUseCaseTestResolver.DebugFeatureInactiveTest
        void shouldEvaluateWithDebugFeatureInactive(final EvaluateUseCaseTestResolver.GivenHappyPath givenHappyPath) {
            // Given
            final EvaluateCommand givenEvaluateCommand = new EvaluateCommand(
                    givenHappyPath.formula(), List.of(), givenHappyPath.debugFeature(), givenEvaluateOn);

            // When
            final Uni<EvaluationResult> executed = evaluateUseCase.execute(givenEvaluateCommand);

            // Then
            final UniAssertSubscriber<EvaluationResult> subscriber = executed.subscribe()
                    .withSubscriber(UniAssertSubscriber.create());
            subscriber.assertCompleted().assertItem(givenHappyPath.evaluationResult());
        }

        @EvaluateUseCaseTestResolver.DebugFeatureActiveTest
        @Tag("AntlrSyntaxErrorException")
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
        private final EvaluateOn givenEvaluateOn = EvaluateOn.ANTLR_MAPPING_DOMAIN_EVAL;

        @EvaluateUseCaseTestResolver.DebugFeatureActiveTest
        void shouldEvaluateWithDebugFeatureActive(final EvaluateUseCaseTestResolver.GivenHappyPath givenHappyPath) {
            // Given
            final EvaluateCommand givenEvaluateCommand = new EvaluateCommand(
                    givenHappyPath.formula(), List.of(), givenHappyPath.debugFeature(), givenEvaluateOn);

            // When
            final Uni<EvaluationResult> executed = evaluateUseCase.execute(givenEvaluateCommand);

            // Then
            final UniAssertSubscriber<EvaluationResult> subscriber = executed.subscribe()
                    .withSubscriber(UniAssertSubscriber.create());

            subscriber.assertCompleted().assertItem(givenHappyPath.evaluationResult());
        }

        @EvaluateUseCaseTestResolver.DebugFeatureInactiveTest
        void shouldEvaluateWithDebugFeatureInactive(final EvaluateUseCaseTestResolver.GivenHappyPath givenHappyPath) {
            // Given
            final EvaluateCommand givenEvaluateCommand = new EvaluateCommand(
                    givenHappyPath.formula(), List.of(), givenHappyPath.debugFeature(), givenEvaluateOn);

            // When
            final Uni<EvaluationResult> executed = evaluateUseCase.execute(givenEvaluateCommand);

            // Then
            final UniAssertSubscriber<EvaluationResult> subscriber = executed.subscribe()
                    .withSubscriber(UniAssertSubscriber.create());
            subscriber.assertCompleted().assertItem(givenHappyPath.evaluationResult());
        }

        @EvaluateUseCaseTestResolver.DebugFeatureActiveTest
        @Tag("AntlrSyntaxErrorException")
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

    // TODO reuse for api test side !
    public static class InfraEvaluateUseCaseTestResolver extends EvaluateUseCaseTestResolver {
        @Override
        protected Map<Formula, Throwable> givenFailings() {
            return Map.of(
                    new Formula("IF("),
                    new AntlrSyntaxErrorException(new Formula("IF("), new AntlrSyntaxError(
                            1, 3, "mismatched input '<EOF>' expecting {'GT', 'GTE', 'EQ', 'NEQ', 'LT', 'LTE', 'AND', 'OR', 'IF', 'IFERROR', 'ISNUM', 'ISLOGICAL', 'ISTEXT', 'ISBLANK', 'ISNA', 'ISERROR', 'IFNA', TRUE, FALSE, STRUCTURED_REFERENCE, VALUE, NUMERIC}"
                    ))
            );
        }
    }

}