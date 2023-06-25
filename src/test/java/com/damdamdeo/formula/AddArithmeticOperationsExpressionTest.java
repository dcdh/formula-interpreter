package com.damdamdeo.formula;

import com.damdamdeo.formula.result.*;
import com.damdamdeo.formula.structuredreference.Reference;
import com.damdamdeo.formula.structuredreference.StructuredData;
import com.damdamdeo.formula.structuredreference.StructuredDatum;
import com.damdamdeo.formula.structuredreference.Value;
import com.damdamdeo.formula.syntax.SyntaxErrorException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class AddArithmeticOperationsExpressionTest extends AbstractExpressionTest {

    @Test
    public void shouldAddForStructuredReferenceLeftAndStructuredReferenceRight() throws SyntaxErrorException {
        // Given
        final String givenFormula = "[@[North Sales Amount]]+[@[South Sales Amount]]";
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
                        new ValueResult("920"),
                        List.of(
                                new MatchedToken("[@[North Sales Amount]]+[@[South Sales Amount]]", 1, 0, 46),
                                new MatchedToken("[@[North Sales Amount]]", 1, 0, 22),
                                new MatchedToken("[@[South Sales Amount]]", 1, 24, 46)
                        )));
    }

    @Test
    public void shouldAddForStructuredReferenceLeftAndValueRight() throws SyntaxErrorException {
        // Given
        final String givenFormula = "[@[North Sales Amount]]+260";
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
                        new ValueResult("920"),
                        List.of(
                                new MatchedToken("[@[North Sales Amount]]+260", 1, 0, 26),
                                new MatchedToken("[@[North Sales Amount]]", 1, 0, 22),
                                new MatchedToken("260", 1, 24, 26)
                        )));
    }

    @Test
    public void shouldAddForValueLeftAndStructuredReferenceRight() throws SyntaxErrorException {
        // Given
        final String givenFormula = "660+[@[South Sales Amount]]";
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
                        new ValueResult("920"),
                        List.of(
                                new MatchedToken("660+[@[South Sales Amount]]", 1, 0, 26),
                                new MatchedToken("660", 1, 0, 2),
                                new MatchedToken("[@[South Sales Amount]]", 1, 4, 26)
                        )));
    }

    @Test
    public void shouldAddForValueLeftAndValueRight() throws SyntaxErrorException {
        // Given
        final String givenFormula = "660+260";
        final StructuredData givenStructuredData = new StructuredData(List.of());

        // When
        final ExecutionResult executionResult = executor.execute(formula4Test(givenFormula), givenStructuredData);

        // Then
        assertThat(executionResult).isEqualTo(
                new ExecutionResult(
                        new ValueResult("920"),
                        List.of(
                                new MatchedToken("660+260", 1, 0, 6),
                                new MatchedToken("660", 1, 0, 2),
                                new MatchedToken("260", 1, 4, 6)
                        )));
    }

    @Test
    public void shouldBeUnknownWhenOneStructuredReferenceIsUnknown() throws SyntaxErrorException {
        // Given
        final String givenFormula = "[@[North Sales Amount]]+[@[South Sales Amount]]";
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
                                new MatchedToken("[@[North Sales Amount]]+[@[South Sales Amount]]", 1, 0, 46),
                                new MatchedToken("[@[North Sales Amount]]", 1, 0, 22),
                                new MatchedToken("[@[South Sales Amount]]", 1, 24, 46)
                        )));
    }

    @Test
    public void shouldBeInErrorWhenOneStructuredReferenceIsNotANumerical() throws SyntaxErrorException {
        // Given
        final String givenFormula = "[@[North Sales Amount]]+[@[South Sales Amount]]";
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
                                new MatchedToken("[@[North Sales Amount]]+[@[South Sales Amount]]", 1, 0, 46),
                                new MatchedToken("[@[North Sales Amount]]", 1, 0, 22),
                                new MatchedToken("[@[South Sales Amount]]", 1, 24, 46)
                        )));
    }

}
