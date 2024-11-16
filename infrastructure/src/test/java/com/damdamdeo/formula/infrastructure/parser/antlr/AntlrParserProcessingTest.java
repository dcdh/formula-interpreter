package com.damdamdeo.formula.infrastructure.parser.antlr;

import com.damdamdeo.formula.domain.provider.StubbedProcessedAtProviderTestProvider;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(StubbedProcessedAtProviderTestProvider.class)
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