package com.damdamdeo.formula.infrastructure.parser.antlr;

import com.damdamdeo.formula.domain.*;
import com.damdamdeo.formula.infrastructure.evaluation.expression.Expression;
import com.damdamdeo.formula.domain.provider.StubbedEvaluatedAtProviderTestProvider;
import com.damdamdeo.formula.domain.spi.ProcessedAtProvider;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.ZonedDateTime;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(StubbedEvaluatedAtProviderTestProvider.class)
class AntlrParserProcessingTest {

//    private AntlrParserProcessing antlrParserProcessing;
//
//    @BeforeEach
//    void setup(final ProcessedAtProvider processedAtProvider) {
//        antlrParserProcessing = new AntlrParserProcessing(processedAtProvider);
//    }
//
//    @ParameterizedTest
//    @MethodSource({
//            "com.damdamdeo.formula.domain.evaluation.provider.EvaluationTestProvider#provideExpressions"
//    })
//    void shouldProcessExpression(final Formula formula, final Expression expression, final Value value) {
//        // When
//        final Uni<ProcessingResult> processed = antlrParserProcessing.process(formula);
//
//        // Then
//        assertOnExecutionResultReceived(processed, processedToAssert ->
//                assertThat(processedToAssert).isEqualTo(
//                        new ProcessingResult(expression,
//                                new EvaluationLoadingProcessedIn(
//                                        new ProcessedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")),
//                                        new ProcessedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]"))
//                                )))
//        );
//    }
//
//    protected void assertOnExecutionResultReceived(final Uni<ProcessingResult> processingResult, final Consumer<ProcessingResult> assertionLogic) {
//        final UniAssertSubscriber<ProcessingResult> subscriber = processingResult
//                .subscribe()
//                .withSubscriber(UniAssertSubscriber.create());
//        final ProcessingResult processingResultToAssert = subscriber.awaitItem().getItem();
//        assertionLogic.accept(processingResultToAssert);
//    }
}