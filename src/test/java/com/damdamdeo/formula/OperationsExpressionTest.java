package com.damdamdeo.formula;

import com.damdamdeo.formula.result.ErrorResult;
import com.damdamdeo.formula.result.ExecutionResult;
import com.damdamdeo.formula.result.UnknownReferenceResult;
import com.damdamdeo.formula.result.ValueResult;
import com.damdamdeo.formula.structuredreference.Reference;
import com.damdamdeo.formula.structuredreference.StructuredData;
import com.damdamdeo.formula.structuredreference.StructuredDatum;
import com.damdamdeo.formula.syntax.SyntaxErrorException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class OperationsExpressionTest extends AbstractExpressionTest {
    @ParameterizedTest
    @CsvSource({
            "ADD,920",
            "SUB,400",
            "DIV,2.538462",
            "MUL,171600"
    })
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
        assertThat(executionResult).isEqualTo(
                new ExecutionResult(
                        new ValueResult(expectedValue)));
    }

    @ParameterizedTest
    @CsvSource({
            "ADD,920",
            "SUB,400",
            "DIV,2.538462",
            "MUL,171600"
    })
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
        assertThat(executionResult).isEqualTo(
                new ExecutionResult(
                        new ValueResult(expectedValue)));
    }

    @ParameterizedTest
    @CsvSource({
            "ADD,920",
            "SUB,400",
            "DIV,2.538462",
            "MUL,171600"
    })
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
        assertThat(executionResult).isEqualTo(
                new ExecutionResult(
                        new ValueResult(expectedValue)));
    }

    @ParameterizedTest
    @CsvSource({
            "ADD,920",
            "SUB,400",
            "DIV,2.538462",
            "MUL,171600"
    })
    public void shouldExecuteOperationForValueLeftAndValueRight(final String givenOperation,
                                                                final String expectedValue) throws SyntaxErrorException {
        // Given
        final String givenFormula = String.format("%s(660,260)", givenOperation);
        final StructuredData givenStructuredData = new StructuredData(List.of());

        // When
        final ExecutionResult executionResult = executor.execute(formula4Test(givenFormula), givenStructuredData);

        // Then
        assertThat(executionResult).isEqualTo(
                new ExecutionResult(
                        new ValueResult(expectedValue)));
    }

    @ParameterizedTest
    @CsvSource({
            "ADD",
            "SUB",
            "DIV",
            "MUL"
    })
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
        assertThat(executionResult).isEqualTo(
                new ExecutionResult(
                        new UnknownReferenceResult()));
    }

    @ParameterizedTest
    @CsvSource({
            "ADD",
            "SUB",
            "DIV",
            "MUL"
    })
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
        assertThat(executionResult).isEqualTo(
                new ExecutionResult(
                        new ErrorResult()));
    }

}
