package com.damdamdeo.formula;

import com.damdamdeo.formula.structuredreference.Reference;
import com.damdamdeo.formula.structuredreference.StructuredData;
import com.damdamdeo.formula.structuredreference.StructuredDatum;
import com.damdamdeo.formula.syntax.SyntaxErrorException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class StructuredReferenceExpressionTest extends AbstractExpressionTest {

    @Test
    public void shouldReturnStructuredReferenceValue() throws SyntaxErrorException {
        // Given
        final String givenFormula = "[@[% Commission]]";
        final StructuredData givenStructuredData = new StructuredData(List.of(
                new StructuredDatum(new Reference("% Commission"), "10%")
        ));

        // When
        final ExecutionResult executionResult = executor.execute(formula4Test(givenFormula), givenStructuredData);

        // Then
        assertThat(executionResult.result()).isEqualTo(
                new Value("10%"));
    }

    @Test
    public void shouldReturnUnknownReferenceWhenStructureReferenceDoesNotExists() throws SyntaxErrorException {
        // Given
        final String givenFormula = "[@[% Commission]]";
        final StructuredData givenStructuredData = new StructuredData();

        // When
        final ExecutionResult executionResult = executor.execute(formula4Test(givenFormula), givenStructuredData);

        // Then
        assertThat(executionResult.result()).isEqualTo(
                new Value("#REF!"));
    }

    @Test
    public void shouldReturnNotAvailableWhenStructureReferenceValueIsNull() throws SyntaxErrorException {
        // Given
        final String givenFormula = "[@[% Commission]]";
        final StructuredData givenStructuredData = new StructuredData(List.of(
                new StructuredDatum(new Reference("% Commission"), null)
        ));

        // When
        final ExecutionResult executionResult = executor.execute(formula4Test(givenFormula), givenStructuredData);

        // Then
        assertThat(executionResult.result()).isEqualTo(
                new Value("#NA!"));
    }

    @Test
    public void shouldLogExecution() throws SyntaxErrorException {
        // Given
        final String givenFormula = "[@[% Commission]]";
        final StructuredData givenStructuredData = new StructuredData(List.of(
                new StructuredDatum(new Reference("% Commission"), "10%")
        ));

        // When
        final ExecutionResult executionResult = executor.execute(formula4Test(givenFormula), givenStructuredData);

        // Then
        assertThat(executionResult.executions()).containsExactly(
                new AntlrExecution(new ExecutionId(new UUID(0, 0)), 0, 16, Map.of(
                        new InputName("structuredReference"), new Reference("% Commission")
                ), Value.of("10%"))
        );
    }

}
