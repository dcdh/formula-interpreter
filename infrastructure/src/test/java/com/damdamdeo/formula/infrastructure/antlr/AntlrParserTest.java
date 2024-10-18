package com.damdamdeo.formula.infrastructure.antlr;

import com.damdamdeo.formula.domain.*;
import com.damdamdeo.formula.domain.spi.EvaluatedAtProvider;
import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class AntlrParserTest extends AbstractFunctionTest {
    private AntlrParser antlrExecutor;
    private EvaluatedAtProvider evaluatedAtProvider;

    @BeforeEach
    public void setup() {
        evaluatedAtProvider = mock(EvaluatedAtProvider.class);
        antlrExecutor = new AntlrParser(evaluatedAtProvider, new DefaultAntlrParseTreeGenerator(evaluatedAtProvider));
    }

    @AfterEach
    public void tearDown() {
        reset(evaluatedAtProvider);
    }

    @Test
    public void shouldFailOnUnrecognizedToken() {
        // Given
        when(evaluatedAtProvider.now())
                .thenReturn(new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")))
                .thenReturn(new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")));

        // When
        final Uni<EvaluationResult> executionResult = antlrExecutor.process(new Formula("\""),
                new PartEvaluationCallback(new NoOpPartEvaluationCallbackListener(), new NumericalContext(), new StructuredReferences()));

        // Then
        assertOnFailure(executionResult, throwableToAssert ->
                assertThat(throwableToAssert)
                        .isInstanceOf(EvaluationException.class)
                        .cause()
                        .isInstanceOf(AntlrSyntaxErrorException.class)
                        .hasFieldOrPropertyWithValue("syntaxError",
                                new AntlrSyntaxError(1, 1, "mismatched input '<EOF>' expecting {'ADD', 'SUB', 'DIV', 'MUL', 'GT', 'GTE', 'EQ', 'NEQ', 'LT', 'LTE', 'AND', 'OR', 'IF', 'IFERROR', 'ISNUM', 'ISLOGICAL', 'ISTEXT', 'ISBLANK', 'ISNA', 'ISERROR', 'IFNA', TRUE, FALSE, STRUCTURED_REFERENCE, VALUE, NUMERIC}"))
        );
    }

    @Test
    public void shouldFailWhenFormulaIsDefinedOnMultipleLines() {
        // Given
        when(evaluatedAtProvider.now())
                .thenReturn(new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")))
                .thenReturn(new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")));

        // When
        final Uni<EvaluationResult> executionResult = antlrExecutor.process(new Formula("\"Hello\"\n\"World\""),
                new PartEvaluationCallback(new NoOpPartEvaluationCallbackListener(), new NumericalContext(), new StructuredReferences()));

        // Then
        assertOnFailure(executionResult, throwableToAssert ->
                assertThat(throwableToAssert)
                        .isInstanceOf(EvaluationException.class)
                        .cause()
                        .isInstanceOf(AntlrSyntaxErrorException.class)
                        .hasFieldOrPropertyWithValue("syntaxError",
                                new AntlrSyntaxError(2, 0, "extraneous input '\"World\"' expecting <EOF>"))
        );
    }

    @Test
    public void shouldLogWhenDebugFeatureIsActive() {
        // Given
        final PartEvaluationCallback givenPartEvaluationCallback = new PartEvaluationCallback(new LoggingPartEvaluationCallbackListener(evaluatedAtProvider),
                new NumericalContext(), new StructuredReferences());
        doReturn(new EvaluatedAt(ZonedDateTime.now())).when(evaluatedAtProvider).now();

        // When
        final Uni<EvaluationResult> executionResult = antlrExecutor.process(new Formula("\"true\""), givenPartEvaluationCallback);

        // Then
        assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                assertThat(executionResultToAssert.intermediateResults()).isNotEmpty()
        );
    }

    @Test
    public void shouldNotLogWhenDebugFeatureIsInactive() {
        // Given
        final PartEvaluationCallback givenPartEvaluationCallback = new PartEvaluationCallback(new NoOpPartEvaluationCallbackListener(),
                new NumericalContext(), new StructuredReferences());
        doReturn(new EvaluatedAt(ZonedDateTime.now())).when(evaluatedAtProvider).now();

        // When
        final Uni<EvaluationResult> executionResult = antlrExecutor.process(new Formula("\"true\""), givenPartEvaluationCallback);

        // Then
        assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                assertThat(executionResultToAssert.intermediateResults()).isEmpty()
        );
    }
}
