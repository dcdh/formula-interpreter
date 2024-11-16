package com.damdamdeo.formula.infrastructure.parser.antlr;

import com.damdamdeo.formula.domain.*;
import com.damdamdeo.formula.domain.spi.EvaluatedAtProvider;
import io.quarkus.cache.Cache;
import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class AntlrParserTest extends AbstractFunctionTest {
    private AntlrParser antlrExecutor;

    @BeforeEach
    public void setup(final EvaluatedAtProvider evaluatedAtProvider) {
        antlrExecutor = new AntlrParser(evaluatedAtProvider,
                new DefaultAntlrParseTreeGenerator(evaluatedAtProvider),
                mock(ParserProcessing.class),
                mock(Cache.class));
    }

    @Test
    public void shouldFailOnUnrecognizedToken() {
        // Given

        // When
        final Uni<EvaluationResult> executionResult = antlrExecutor.process(new Formula("\""),
                new PartEvaluationCallback(new NoOpPartEvaluationListener(), new NumericalContext(), List.of()));

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

        // When
        final Uni<EvaluationResult> executionResult = antlrExecutor.process(new Formula("\"Hello\"\n\"World\""),
                new PartEvaluationCallback(new NoOpPartEvaluationListener(), new NumericalContext(), List.of()));

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
    public void shouldLogWhenDebugFeatureIsActive(final EvaluatedAtProvider evaluatedAtProvider) {
        // Given
        final PartEvaluationCallback givenPartEvaluationCallback = new PartEvaluationCallback(new DebugPartEvaluationListener(evaluatedAtProvider),
                new NumericalContext(), List.of());

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
        final PartEvaluationCallback givenPartEvaluationCallback = new PartEvaluationCallback(new NoOpPartEvaluationListener(),
                new NumericalContext(), List.of());

        // When
        final Uni<EvaluationResult> executionResult = antlrExecutor.process(new Formula("\"true\""), givenPartEvaluationCallback);

        // Then
        assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                assertThat(executionResultToAssert.intermediateResults()).isEmpty()
        );
    }
}
