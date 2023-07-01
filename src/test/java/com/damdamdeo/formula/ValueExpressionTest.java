package com.damdamdeo.formula;

import com.damdamdeo.formula.structuredreference.StructuredData;
import com.damdamdeo.formula.syntax.SyntaxErrorException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class ValueExpressionTest extends AbstractExpressionTest {

    @ParameterizedTest
    @CsvSource({
            "\"Hello World\",Hello World",
            "\"AZERTY\",AZERTY",
            "10,10",
            "\"-+E.09()%\",-+E.09()%"
    })
    public void shouldReturnInputValue(final String givenFormula,
                                       final String expectedResult) throws SyntaxErrorException {
        // Given

        // When
        final ExecutionResult executionResult = executor.execute(formula4Test(givenFormula), new StructuredData());

        // Then
        assertThat(executionResult.result()).isEqualTo(
                new Value(expectedResult));
    }

    @Test
    public void shouldLogExecution() throws SyntaxErrorException {
        // Given
        final String givenFormula = "\"Hello World\"";
        final StructuredData givenStructuredData = new StructuredData(List.of());

        // When
        final ExecutionResult executionResult = executor.execute(formula4Test(givenFormula), givenStructuredData);

        // Then
        assertThat(executionResult.executions()).containsExactly(
                new AntlrExecution(new ExecutionId(new UUID(0, 0)), 0, 12, Map.of(), Value.of("Hello World"))
        );
    }
}
