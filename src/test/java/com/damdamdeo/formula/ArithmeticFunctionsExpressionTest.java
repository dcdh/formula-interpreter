package com.damdamdeo.formula;

import com.damdamdeo.formula.structuredreference.Reference;
import com.damdamdeo.formula.structuredreference.StructuredData;
import com.damdamdeo.formula.structuredreference.StructuredDatum;
import com.damdamdeo.formula.syntax.SyntaxErrorException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class ArithmeticFunctionsExpressionTest extends AbstractExpressionTest {
    @ParameterizedTest
    @MethodSource("provideOperationsWithExpectedValues")
    public void shouldExecuteOperationForStructuredReferenceLeftAndStructuredReferenceRight(final String givenOperation,
                                                                                            final String expectedValue) throws SyntaxErrorException {
        // Given
        final String givenFormula = String.format("%s([@[North Sales Amount]],[@[South Sales Amount]])", givenOperation);
        final StructuredData givenStructuredData = new StructuredData(
                List.of(
                        new StructuredDatum(new Reference("North Sales Amount"), new Value("660")),
                        new StructuredDatum(new Reference("South Sales Amount"), new Value("260"))
                )
        );

        // When
        final ExecutionResult executionResult = executor.execute(formula4Test(givenFormula), givenStructuredData);

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
                        new StructuredDatum(new Reference("North Sales Amount"), new Value("660"))
                )
        );

        // When
        final ExecutionResult executionResult = executor.execute(formula4Test(givenFormula), givenStructuredData);

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
                        new StructuredDatum(new Reference("South Sales Amount"), new Value("260"))
                )
        );

        // When
        final ExecutionResult executionResult = executor.execute(formula4Test(givenFormula), givenStructuredData);

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
        final ExecutionResult executionResult = executor.execute(formula4Test(givenFormula), givenStructuredData);

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
        final ExecutionResult executionResult = executor.execute(formula4Test(givenFormula), givenStructuredData);

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
                        new StructuredDatum(new Reference("North Sales Amount"), new Value("660"))
                )
        );

        // When
        final ExecutionResult executionResult = executor.execute(formula4Test(givenFormula), givenStructuredData);

        // Then
        assertThat(executionResult.result()).isEqualTo(
                new Value("#REF!"));
    }

    @ParameterizedTest
    @MethodSource("provideOperations")
    public void shouldBeInErrorWhenOneStructuredReferenceIsNotANumerical(final String givenOperation) throws SyntaxErrorException {
        // Given
        final String givenFormula = String.format("%s([@[North Sales Amount]],[@[South Sales Amount]])", givenOperation);
        final StructuredData givenStructuredData = new StructuredData(
                List.of(
                        new StructuredDatum(new Reference("North Sales Amount"), new Value("660")),
                        new StructuredDatum(new Reference("South Sales Amount"), new Value("boom"))
                )
        );

        // When
        final ExecutionResult executionResult = executor.execute(formula4Test(givenFormula), givenStructuredData);

        // Then
        assertThat(executionResult.result()).isEqualTo(
                new Value("#VALUE!"));
    }

    private static Stream<Arguments> provideOperations() {
        return Stream.of(
                Arguments.of("ADD"),
                Arguments.of("SUB"),
                Arguments.of("DIV"),
                Arguments.of("MUL")
        );
    }
}
