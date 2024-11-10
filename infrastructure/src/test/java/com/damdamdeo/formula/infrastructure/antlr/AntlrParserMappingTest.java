package com.damdamdeo.formula.infrastructure.antlr;

import com.damdamdeo.formula.domain.*;
import com.damdamdeo.formula.domain.evaluation.Expression;
import com.damdamdeo.formula.domain.provider.StubbedEvaluatedAtProviderTestProvider;
import com.damdamdeo.formula.domain.spi.EvaluatedAtProvider;
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
class AntlrParserMappingTest {

    private AntlrParserMapping antlrParserProcessing;

    @BeforeEach
    void setup(final EvaluatedAtProvider evaluatedAtProvider) {
        antlrParserProcessing = new AntlrParserMapping(evaluatedAtProvider);
    }

    @ParameterizedTest
    @MethodSource({
            "com.damdamdeo.formula.domain.evaluation.provider.EvaluationTestProvider#provideExpressions"
    })
    void shouldMapExpression(final Formula formula, final Expression expression, final Value value) {
        // When
        final Uni<MappingResult> processed = antlrParserProcessing.map(formula);

        // Then
        assertOnExecutionResultReceived(processed, processedToAssert ->
                assertThat(processedToAssert).isEqualTo(
                        new MappingResult(expression,
                                new ParserEvaluationProcessedIn(
                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")),
                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]"))
                                )))
        );
    }

    protected void assertOnExecutionResultReceived(final Uni<MappingResult> processingResult, final Consumer<MappingResult> assertionLogic) {
        final UniAssertSubscriber<MappingResult> subscriber = processingResult
                .subscribe()
                .withSubscriber(UniAssertSubscriber.create());
        final MappingResult mappingResultToAssert = subscriber.awaitItem().getItem();
        assertionLogic.accept(mappingResultToAssert);
    }
}