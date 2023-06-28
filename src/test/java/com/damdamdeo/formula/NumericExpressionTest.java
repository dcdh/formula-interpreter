package com.damdamdeo.formula;

import com.damdamdeo.formula.result.ExecutionResult;
import com.damdamdeo.formula.result.ValueResult;
import com.damdamdeo.formula.structuredreference.StructuredData;
import com.damdamdeo.formula.syntax.SyntaxErrorException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class NumericExpressionTest extends AbstractExpressionTest {

    @ParameterizedTest
    @CsvSource({
            "0",
            "0.00",
            "123",
            "-123",
            "1.23E3",
            "1.23E+3",
            "12.3E+7",
            "12.0",
            "12.3",
            "0.00123",
            "-1.23E-12",
            "1234.5E-4",
            "0E+7",
            "-0"
    })
    public void shouldBeANumeric(final String givenFormula) throws SyntaxErrorException {
        // Given

        // When
        final ExecutionResult executionResult = executor.execute(formula4Test(givenFormula), new StructuredData());

        // Then
        assertThat(executionResult.result().isNumeric()).isTrue();
    }

    // TODO faire les tests par operations ...

    @ParameterizedTest
    @CsvSource({
            "ADD,123000000",
            "SUB,123000000",
            "DIV,-100000000000000000000",
            "MUL,-0.000151"
    })
    public void shouldExecuteOperationForValueLeftAndValueRight(final String givenOperation,
                                                                final String expectedValue) throws SyntaxErrorException {
        // Given
        final String givenFormula = String.format("%s(12.3E+7,-1.23E-12)", givenOperation);
        final StructuredData givenStructuredData = new StructuredData(List.of());

        // When
        final ExecutionResult executionResult = executor.execute(formula4Test(givenFormula), givenStructuredData);

        // Then
        assertThat(executionResult).isEqualTo(
                new ExecutionResult(
                        new ValueResult(expectedValue)));
    }
}
