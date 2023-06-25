package com.damdamdeo.formula;

import com.damdamdeo.formula.result.ExecutionResult;
import com.damdamdeo.formula.result.MatchedToken;
import com.damdamdeo.formula.result.UnknownReferenceResult;
import com.damdamdeo.formula.result.ValueResult;
import com.damdamdeo.formula.structuredreference.Reference;
import com.damdamdeo.formula.structuredreference.StructuredData;
import com.damdamdeo.formula.structuredreference.StructuredDatum;
import com.damdamdeo.formula.structuredreference.Value;
import com.damdamdeo.formula.syntax.SyntaxErrorException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class StructuredReferenceExpressionTest extends AbstractExpressionTest {

    @Test
    public void shouldReturnStructuredReferenceValue() throws SyntaxErrorException {
        // Given
        final String givenFormula = "[@[% Commission]]";
        final StructuredData givenStructuredData = new StructuredData(List.of(
                new StructuredDatum(new Reference("% Commission"), new Value("10%"))
        ));

        // When
        final ExecutionResult executionResult = executor.execute(formula4Test(givenFormula), givenStructuredData);

        // Then
        assertThat(executionResult).isEqualTo(new ExecutionResult(
                new ValueResult("10%"),
                List.of(new MatchedToken("[@[% Commission]]", 1, 0, 16))));
    }

    @Test
    public void shouldReturnUnknownReferenceWhenStructureReferenceDoesNotExists() throws SyntaxErrorException {
        // Given
        final String givenFormula = "[@[% Commission]]";
        final StructuredData givenStructuredData = new StructuredData();

        // When
        final ExecutionResult executionResult = executor.execute(formula4Test(givenFormula), givenStructuredData);

        // Then
        assertThat(executionResult).isEqualTo(new ExecutionResult(
                new UnknownReferenceResult(),
                List.of(new MatchedToken("[@[% Commission]]", 1, 0, 16))));
    }

}
