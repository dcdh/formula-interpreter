package com.damdamdeo.formula.infrastructure.antlr;

import com.damdamdeo.formula.domain.*;
import com.damdamdeo.formula.domain.spi.ExecutedAtProvider;
import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class AntlrExecutorTest extends AbstractFunctionTest {
    private AntlrExecutor antlrExecutor;
    private ExecutedAtProvider executedAtProvider;

    @BeforeEach
    public void setup() {
        executedAtProvider = mock(ExecutedAtProvider.class);
        antlrExecutor = new AntlrExecutor(executedAtProvider, new NumericalContext(), new DefaultAntlrParseTreeGenerator());
    }

    @AfterEach
    public void tearDown() {
        reset(executedAtProvider);
    }

    @Test
    public void shouldFailOnUnrecognizedToken() {
        // Given

        // When
        final Uni<ExecutionResult> executionResult = antlrExecutor.execute(new Formula("\""), new StructuredData(), new NoOpExecutionWrapper());

        // Then
        assertOnFailure(executionResult, throwableToAssert ->
                assertThat(throwableToAssert)
                        .isInstanceOf(ExecutionException.class)
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
        final Uni<ExecutionResult> executionResult = antlrExecutor.execute(new Formula("\"Hello\"\n\"World\""), new StructuredData(), new NoOpExecutionWrapper());

        // Then
        assertOnFailure(executionResult, throwableToAssert ->
                assertThat(throwableToAssert)
                        .isInstanceOf(ExecutionException.class)
                        .cause()
                        .isInstanceOf(AntlrSyntaxErrorException.class)
                        .hasFieldOrPropertyWithValue("syntaxError",
                                new AntlrSyntaxError(2, 0, "extraneous input '\"World\"' expecting <EOF>"))
        );
    }

    @Test
    public void shouldLogWhenDebugFeatureIsActive() {
        // Given
        final ExecutionWrapper givenExecutionWrapper = new LoggingExecutionWrapper(executedAtProvider);
        doReturn(new ExecutedAt(ZonedDateTime.now())).when(executedAtProvider).now();

        // When
        final Uni<ExecutionResult> executionResult = antlrExecutor.execute(new Formula("\"true\""), new StructuredData(), givenExecutionWrapper);

        // Then
        assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                assertThat(executionResultToAssert.elementExecutions()).isNotEmpty()
        );
    }

    @Test
    public void shouldNotLogWhenDebugFeatureIsInactive() {
        // Given
        final ExecutionWrapper givenExecutionWrapper = new NoOpExecutionWrapper();
        doReturn(new ExecutedAt(ZonedDateTime.now())).when(executedAtProvider).now();

        // When
        final Uni<ExecutionResult> executionResult = antlrExecutor.execute(new Formula("\"true\""), new StructuredData(), givenExecutionWrapper);

        // Then
        assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                assertThat(executionResultToAssert.elementExecutions()).isEmpty()
        );
    }
}
