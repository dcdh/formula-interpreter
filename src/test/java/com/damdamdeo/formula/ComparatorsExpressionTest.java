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

public class ComparatorsExpressionTest extends AbstractExpressionTest {
    @ParameterizedTest
    @CsvSource({
            "660,>,260,true",
            "260,>,660,false",
            "260,>,260,false",
            "660,>=,260,true",
            "260,>=,660,false",
            "260,>=,260,true",
            "660,=,260,false",
            "260,=,660,false",
            "260,=,260,true",
            "660,<,260,false",
            "260,<,660,true",
            "260,<,260,false",
            "660,<=,260,false",
            "260,<=,660,true",
            "260,<=,260,true"
    })
    public void shouldExecuteComparisonForStructuredReferenceLeftAndStructuredReferenceRight(final String leftValue,
                                                                                             final String givenComparison,
                                                                                             final String rightValue,
                                                                                             final String expectedValue) throws SyntaxErrorException {
        // Given
        final String givenFormula = String.format("[@[North Sales Amount]]%s[@[South Sales Amount]]", givenComparison);
        final StructuredData givenStructuredData = new StructuredData(
                List.of(
                        new StructuredDatum(new Reference("North Sales Amount"), new Value(leftValue)),
                        new StructuredDatum(new Reference("South Sales Amount"), new Value(rightValue))
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
            "660,>,260,true",
            "260,>,660,false",
            "260,>,260,false",
            "660,>=,260,true",
            "260,>=,660,false",
            "260,>=,260,true",
            "660,=,260,false",
            "260,=,660,false",
            "260,=,260,true",
            "660,<,260,false",
            "260,<,660,true",
            "260,<,260,false",
            "660,<=,260,false",
            "260,<=,660,true",
            "260,<=,260,true"
    })
    public void shouldExecuteComparisonForStructuredReferenceLeftAndValueRight(final String leftValue,
                                                                               final String givenComparison,
                                                                               final String rightValue,
                                                                               final String expectedValue) throws SyntaxErrorException {
        // Given
        final String givenFormula = String.format("[@[North Sales Amount]]%s%s", givenComparison, rightValue);
        final StructuredData givenStructuredData = new StructuredData(
                List.of(
                        new StructuredDatum(new Reference("North Sales Amount"), new Value(leftValue))
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
            "660,>,260,true",
            "260,>,660,false",
            "260,>,260,false",
            "660,>=,260,true",
            "260,>=,660,false",
            "260,>=,260,true",
            "660,=,260,false",
            "260,=,660,false",
            "260,=,260,true",
            "660,<,260,false",
            "260,<,660,true",
            "260,<,260,false",
            "660,<=,260,false",
            "260,<=,660,true",
            "260,<=,260,true"
    })
    public void shouldExecuteComparisonForValueLeftAndStructuredReferenceRight(final String leftValue,
                                                                               final String givenComparison,
                                                                               final String rightValue,
                                                                               final String expectedValue) throws SyntaxErrorException {
        // Given
        final String givenFormula = String.format("%s%s[@[South Sales Amount]]", leftValue, givenComparison);
        final StructuredData givenStructuredData = new StructuredData(
                List.of(
                        new StructuredDatum(new Reference("South Sales Amount"), new Value(rightValue))
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
            "660,>,260,true",
            "260,>,660,false",
            "260,>,260,false",
            "660,>=,260,true",
            "260,>=,660,false",
            "260,>=,260,true",
            "660,=,260,false",
            "260,=,660,false",
            "260,=,260,true",
            "660,<,260,false",
            "260,<,660,true",
            "260,<,260,false",
            "660,<=,260,false",
            "260,<=,660,true",
            "260,<=,260,true"
    })
    public void shouldExecuteComparisonForValueLeftAndValueRight(final String leftValue,
                                                                 final String givenComparison,
                                                                 final String rightValue,
                                                                 final String expectedValue) throws SyntaxErrorException {
        // Given
        final String givenFormula = String.format("%s%s%s", leftValue, givenComparison, rightValue);
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
            ">",
            ">=",
            "=",
            "<",
            "<="
    })
    public void shouldBeUnknownWhenOneStructuredReferenceIsUnknown(final String givenComparison) throws SyntaxErrorException {
        // Given
        final String givenFormula = String.format("[@[North Sales Amount]]%s[@[South Sales Amount]]", givenComparison);
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
            ">",
            ">=",
            "=",
            "<",
            "<="
    })
    public void shouldBeInErrorWhenOneStructuredReferenceIsNotANumerical(final String givenComparison) throws SyntaxErrorException {
        // Given
        final String givenFormula = String.format("[@[North Sales Amount]]%s[@[South Sales Amount]]", givenComparison);
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
