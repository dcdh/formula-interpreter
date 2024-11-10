package com.damdamdeo.formula.infrastructure.antlr;

import com.damdamdeo.formula.domain.*;
import com.damdamdeo.formula.domain.evaluation.Expression;
import com.damdamdeo.formula.domain.evaluation.provider.EvaluationTestResolver;
import com.damdamdeo.formula.domain.evaluation.provider.IntermediateResults;
import com.damdamdeo.formula.domain.evaluation.provider.StructuredReferences;
import io.smallrye.mutiny.Uni;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class AntlrArgumentNumericTest extends AbstractFunctionTest {

    non !!
    @EvaluationTestResolver.ArgumentTest
    public void shouldProcess(final StructuredReferences structuredReferences,
                              final Formula formula,
                              final Expression expression,
                              final Evaluated evaluated,
                              final IntermediateResults intermediateResults,
                              final EvaluationProcessedIn evaluationProcessedIn) {
        // Given

        // When
        final Uni<EvaluationResult> executionResult = antlrExecutor.process(formula,
                new PartEvaluationCallback(new DebugPartEvaluationListener(evaluatedAtProvider), new NumericalContext(),
                        structuredReferences.structuredReferences()));

        // Then
        assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                assertAll(
                        () -> assertThat(executionResultToAssert.value()).isEqualTo(evaluated.value()),
                        // TODO better testing ... should be extracted elsewhere
                        () -> assertThat(executionResultToAssert.parserEvaluationProcessedIn()).isNotNull(),
                        () -> assertThat(executionResultToAssert.intermediateResults()).containsAll(intermediateResults.intermediateResult()),
                        () -> assertThat(executionResultToAssert.evaluationProcessedIn()).isEqualTo(evaluationProcessedIn)
                ));
    }
}
