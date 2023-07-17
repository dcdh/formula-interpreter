package com.damdamdeo.formula.infrastructure.antlr;

import com.damdamdeo.formula.domain.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class ArithmeticFunctionsExpressionTest extends AbstractExpressionTest {
    @ParameterizedTest
    @MethodSource("provideOperationsWithExpectedValues")
    public void shouldExecuteOperationForStructuredReferenceLeftAndStructuredReferenceRight(final String givenOperation,
                                                                                            final String expectedValue) throws SyntaxErrorException {
        // Given
        final String givenFormula = String.format("%s([@[North Sales Amount]],[@[South Sales Amount]])", givenOperation);
        final StructuredData givenStructuredData = new StructuredData(
                List.of(
                        new StructuredDatum(new Reference("North Sales Amount"), "660"),
                        new StructuredDatum(new Reference("South Sales Amount"), "260")
                )
        );

        // When
        final ExecutionResult executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredData);

        // Then
        assertThat(executionResult.result()).isEqualTo(
                new Value(expectedValue));
    }

    @ParameterizedTest
    @MethodSource("provideOperationsWithExpectedValues")
    public void shouldExecuteOperationForStructuredReferenceLeftAndValueRight(final String givenOperation,
                                                                              final String expectedValue) throws SyntaxErrorException {
        // Given
        final String givenFormula = String.format("%s([@[North Sales Amount]],260)", givenOperation);
        final StructuredData givenStructuredData = new StructuredData(
                List.of(
                        new StructuredDatum(new Reference("North Sales Amount"), "660")
                )
        );

        // When
        final ExecutionResult executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredData);

        // Then
        assertThat(executionResult.result()).isEqualTo(
                new Value(expectedValue));
    }

    @ParameterizedTest
    @MethodSource("provideOperationsWithExpectedValues")
    public void shouldExecuteOperationForValueLeftAndStructuredReferenceRight(final String givenOperation,
                                                                              final String expectedValue) throws SyntaxErrorException {
        // Given
        final String givenFormula = String.format("%s(660,[@[South Sales Amount]])", givenOperation);
        final StructuredData givenStructuredData = new StructuredData(
                List.of(
                        new StructuredDatum(new Reference("South Sales Amount"), "260")
                )
        );

        // When
        final ExecutionResult executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredData);

        // Then
        assertThat(executionResult.result()).isEqualTo(
                new Value(expectedValue));
    }

    @ParameterizedTest
    @MethodSource("provideOperationsWithExpectedValues")
    public void shouldExecuteOperationForValueLeftAndValueRight(final String givenOperation,
                                                                final String expectedValue) throws SyntaxErrorException {
        // Given
        final String givenFormula = String.format("%s(660,260)", givenOperation);
        final StructuredData givenStructuredData = new StructuredData(List.of());

        // When
        final ExecutionResult executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredData);

        // Then
        assertThat(executionResult.result()).isEqualTo(
                new Value(expectedValue));
    }

    private static Stream<Arguments> provideOperationsWithExpectedValues() {
        return Stream.of(
                Arguments.of("ADD", "920"),
                Arguments.of("SUB", "400"),
                Arguments.of("DIV", "2.538462"),
                Arguments.of("MUL", "171600")
        );
    }

    @Test
    public void shouldCompoundArithmeticFunctions() throws SyntaxErrorException {
        // Given
        final String givenFormula = "DIV(ADD(2,MUL(2,4)),2)";
        final StructuredData givenStructuredData = new StructuredData(List.of());

        // When
        final ExecutionResult executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredData);

        // Then
        assertThat(executionResult.result()).isEqualTo(
                new Value("5"));
    }

    @ParameterizedTest
    @MethodSource("provideOperations")
    public void shouldBeUnknownWhenOneStructuredReferenceIsUnknown(final String givenOperation) throws SyntaxErrorException {
        // Given
        final String givenFormula = String.format("%s([@[North Sales Amount]],[@[South Sales Amount]])", givenOperation);
        final StructuredData givenStructuredData = new StructuredData(
                List.of(
                        new StructuredDatum(new Reference("North Sales Amount"), "660")
                )
        );

        // When
        final ExecutionResult executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredData);

        // Then
        assertThat(executionResult.result()).isEqualTo(
                new Value("#REF!"));
    }

    @ParameterizedTest
    @MethodSource("provideOperations")
    public void shouldBeNotAvailableWhenLeftStructuredReferenceIsNull(final String givenOperation) throws SyntaxErrorException {
        // Given
        final String givenFormula = String.format("%s([@[North Sales Amount]],[@[South Sales Amount]])", givenOperation);
        final StructuredData givenStructuredData = new StructuredData(
                List.of(
                        new StructuredDatum(new Reference("North Sales Amount"), (String) null),
                        new StructuredDatum(new Reference("South Sales Amount"), "260")
                )
        );

        // When
        final ExecutionResult executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredData);

        // Then
        assertThat(executionResult.result()).isEqualTo(
                new Value("#NA!"));
    }

    @ParameterizedTest
    @MethodSource("provideOperations")
    public void shouldBeNotAvailableWhenRightStructuredReferenceIsNull(final String givenOperation) throws SyntaxErrorException {
        // Given
        final String givenFormula = String.format("%s([@[North Sales Amount]],[@[South Sales Amount]])", givenOperation);
        final StructuredData givenStructuredData = new StructuredData(
                List.of(
                        new StructuredDatum(new Reference("North Sales Amount"), "660"),
                        new StructuredDatum(new Reference("South Sales Amount"), (String) null)
                )
        );

        // When
        final ExecutionResult executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredData);

        // Then
        assertThat(executionResult.result()).isEqualTo(
                new Value("#NA!"));
    }

    @ParameterizedTest
    @MethodSource("provideOperations")
    public void shouldBeInErrorWhenOneStructuredReferenceIsNotANumerical(final String givenOperation) throws SyntaxErrorException {
        // Given
        final String givenFormula = String.format("%s([@[North Sales Amount]],[@[South Sales Amount]])", givenOperation);
        final StructuredData givenStructuredData = new StructuredData(
                List.of(
                        new StructuredDatum(new Reference("North Sales Amount"), "660"),
                        new StructuredDatum(new Reference("South Sales Amount"), "boom")
                )
        );

        // When
        final ExecutionResult executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredData);

        // Then
        assertThat(executionResult.result()).isEqualTo(
                new Value("#NUM!"));
    }

    private static Stream<Arguments> provideOperations() {
        return Stream.of(
                Arguments.of("ADD"),
                Arguments.of("SUB"),
                Arguments.of("DIV"),
                Arguments.of("MUL")
        );
    }

    @Test
    public void shouldDivideByZeroProduceAnError() throws SyntaxErrorException {
        // Given
        final String givenFormula = "DIV(10,0)";
        final StructuredData givenStructuredData = new StructuredData(List.of());

        // When
        final ExecutionResult executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredData);

        // Then
        assertThat(executionResult.result()).isEqualTo(
                new Value("#DIV/0!"));
    }

    @Test
    public void shouldLogExecution() throws SyntaxErrorException {
        // Given
        final String givenFormula = "DIV(10,0)";
        final StructuredData givenStructuredData = new StructuredData(List.of());
        when(executedAtProvider.now())
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:30+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:31+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:32+01:00[Europe/Paris]")))
        ;

        // When
        final ExecutionResult executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredData);

        // Then
        assertThat(executionResult.executions()).containsExactly(
                new AntlrExecution(new ExecutionId(new UUID(0, 0)), new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:30+01:00[Europe/Paris]")), 4, 5, Map.of(), Value.of("10")),
                new AntlrExecution(new ExecutionId(new UUID(0, 0)), new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:31+01:00[Europe/Paris]")), 7, 7, Map.of(), Value.of("0")),
                new AntlrExecution(new ExecutionId(new UUID(0, 0)), new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:32+01:00[Europe/Paris]")), 0, 8, Map.of(
                        new InputName("left"), Value.of("10"),
                        new InputName("right"), Value.of("0")
                ), Value.of("#DIV/0!"))
        );
    }
}