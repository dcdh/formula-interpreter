package com.damdamdeo.formula;

import com.damdamdeo.formula.result.ExecutionResult;
import com.damdamdeo.formula.result.MatchedToken;
import com.damdamdeo.formula.result.ValueResult;
import com.damdamdeo.formula.structuredreference.StructuredData;
import com.damdamdeo.formula.syntax.SyntaxErrorException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ValueExpressionTest extends AbstractExpressionTest {

    @ParameterizedTest
    @CsvSource({
            "Hello World,Hello World",
            "AZERTY,AZERTY",
            "10,10"
    })
    public void shouldReturnInputValue(final String givenFormula,
                                       final String expectedResult) throws SyntaxErrorException {
        // Given

        // When
        final ExecutionResult executionResult = executor.execute(formula4Test(givenFormula), new StructuredData());

        // Then
        assertThat(executionResult).isEqualTo(
                new ExecutionResult(
                        new ValueResult(expectedResult),
                        List.of(new MatchedToken(expectedResult, 1, 0, expectedResult.length() - 1))
                ));
    }

}
