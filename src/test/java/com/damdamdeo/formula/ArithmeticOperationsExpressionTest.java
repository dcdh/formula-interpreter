package com.damdamdeo.formula;

import com.damdamdeo.formula.result.*;
import com.damdamdeo.formula.structuredreference.Reference;
import com.damdamdeo.formula.structuredreference.StructuredData;
import com.damdamdeo.formula.structuredreference.StructuredDatum;
import com.damdamdeo.formula.structuredreference.Value;
import com.damdamdeo.formula.syntax.SyntaxErrorException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ArithmeticOperationsExpressionTest extends AbstractExpressionTest {
    @ParameterizedTest
    @CsvSource({
            "+,920",
            "-,400",
            "/,2.538462"
    })
    public void shouldExecuteOperationForStructuredReferenceLeftAndStructuredReferenceRight(final String givenOperation,
                                                                                            final String expectedValue) throws SyntaxErrorException {
        // Given
        final String givenFormula = String.format("[@[North Sales Amount]]%s[@[South Sales Amount]]", givenOperation);
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
                        new ValueResult(expectedValue),
                        List.of(
                                new MatchedToken(String.format("[@[North Sales Amount]]%s[@[South Sales Amount]]", givenOperation), 1, 0, 46),
                                new MatchedToken("[@[North Sales Amount]]", 1, 0, 22),
                                new MatchedToken("[@[South Sales Amount]]", 1, 24, 46)
                        )));
    }

    @ParameterizedTest
    @CsvSource({
            "+,920",
            "-,400",
            "/,2.538462"
    })
    public void shouldExecuteOperationForStructuredReferenceLeftAndValueRight(final String givenOperation,
                                                                              final String expectedValue) throws SyntaxErrorException {
        // Given
        final String givenFormula = String.format("[@[North Sales Amount]]%s260", givenOperation);
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
                        new ValueResult(expectedValue),
                        List.of(
                                new MatchedToken(String.format("[@[North Sales Amount]]%s260", givenOperation), 1, 0, 26),
                                new MatchedToken("[@[North Sales Amount]]", 1, 0, 22),
                                new MatchedToken("260", 1, 24, 26)
                        )));
    }

    @ParameterizedTest
    @CsvSource({
            "+,920",
            "-,400",
            "/,2.538462"
    })
    public void shouldExecuteOperationForValueLeftAndStructuredReferenceRight(final String givenOperation,
                                                                              final String expectedValue) throws SyntaxErrorException {
        // Given
        final String givenFormula = String.format("660%s[@[South Sales Amount]]", givenOperation);
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
                        new ValueResult(expectedValue),
                        List.of(
                                new MatchedToken(String.format("660%s[@[South Sales Amount]]", givenOperation), 1, 0, 26),
                                new MatchedToken("660", 1, 0, 2),
                                new MatchedToken("[@[South Sales Amount]]", 1, 4, 26)
                        )));
    }

    @ParameterizedTest
    @CsvSource({
            "+,920",
            "-,400",
            "/,2.538462"
    })
    public void shouldExecuteOperationForValueLeftAndValueRight(final String givenOperation,
                                                                final String expectedValue) throws SyntaxErrorException {
        // Given
        final String givenFormula = String.format("660%s260", givenOperation);
        final StructuredData givenStructuredData = new StructuredData(List.of());

        // When
        final ExecutionResult executionResult = executor.execute(formula4Test(givenFormula), givenStructuredData);

        // Then
        assertThat(executionResult).isEqualTo(
                new ExecutionResult(
                        new ValueResult(expectedValue),
                        List.of(
                                new MatchedToken(String.format("660%s260", givenOperation), 1, 0, 6),
                                new MatchedToken("660", 1, 0, 2),
                                new MatchedToken("260", 1, 4, 6)
                        )));
    }

    @ParameterizedTest
    @CsvSource({
            "+",
            "-",
            "/"
    })
    public void shouldBeUnknownWhenOneStructuredReferenceIsUnknown(final String givenOperation) throws SyntaxErrorException {
        // Given
        final String givenFormula = String.format("[@[North Sales Amount]]%s[@[South Sales Amount]]", givenOperation);
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
                        new UnknownReferenceResult(),
                        List.of(
                                new MatchedToken(String.format("[@[North Sales Amount]]%s[@[South Sales Amount]]", givenOperation), 1, 0, 46),
                                new MatchedToken("[@[North Sales Amount]]", 1, 0, 22),
                                new MatchedToken("[@[South Sales Amount]]", 1, 24, 46)
                        )));
    }

    @ParameterizedTest
    @CsvSource({
            "+",
            "-",
            "/"
    })
    public void shouldBeInErrorWhenOneStructuredReferenceIsNotANumerical(final String givenOperation) throws SyntaxErrorException {
        // Given
        final String givenFormula = String.format("[@[North Sales Amount]]%s[@[South Sales Amount]]", givenOperation);
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
                        new ErrorResult(),
                        List.of(
                                new MatchedToken(String.format("[@[North Sales Amount]]%s[@[South Sales Amount]]", givenOperation), 1, 0, 46),
                                new MatchedToken("[@[North Sales Amount]]", 1, 0, 22),
                                new MatchedToken("[@[South Sales Amount]]", 1, 24, 46)
                        )));
    }

}
