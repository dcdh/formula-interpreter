package com.damdamdeo.formula.domain.evaluation;

import com.damdamdeo.formula.domain.DebugPartEvaluationListener;
import com.damdamdeo.formula.domain.Evaluated;
import com.damdamdeo.formula.domain.NoOpPartEvaluationListener;
import com.damdamdeo.formula.domain.NumericalContext;
import com.damdamdeo.formula.domain.evaluation.provider.ExpectedEvaluation;
import com.damdamdeo.formula.domain.evaluation.provider.Given;
import com.damdamdeo.formula.domain.provider.StubbedEvaluatedAtProviderTestProvider;
import com.damdamdeo.formula.domain.spi.EvaluatedAtProvider;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(StubbedEvaluatedAtProviderTestProvider.class)
class ExpressionTest {

    @ParameterizedTest
    @MethodSource({
//            "com.damdamdeo.formula.domain.evaluation.provider.ArgumentExpressionTestProvider#provide"
//            "com.damdamdeo.formula.domain.evaluation.provider.ArithmeticExpressionTestProvider#provideOperationForStructuredReferenceLeftAndStructuredReferenceRight",
//            "com.damdamdeo.formula.domain.evaluation.provider.ArithmeticExpressionTestProvider#provideOperationForStructuredReferenceLeftAndValueRight",
//            "com.damdamdeo.formula.domain.evaluation.provider.ArithmeticExpressionTestProvider#provideOperationForValueLeftAndStructuredReferenceRight",
//            "com.damdamdeo.formula.domain.evaluation.provider.ArithmeticExpressionTestProvider#provideOperationForValueLeftAndValueRight"
//            "com.damdamdeo.formula.domain.evaluation.provider.LogicalComparisonExpressionTestProvider#provideLogicalComparisonFunctions"
//            "com.damdamdeo.formula.domain.evaluation.provider.LogicalBooleanExpressionTestProvider#provideOperationForStructuredReferenceLeftAndStructuredReferenceRight",
//            "com.damdamdeo.formula.domain.evaluation.provider.LogicalBooleanExpressionTestProvider#provideOperationForStructuredReferenceLeftAndValueRight",
//            "com.damdamdeo.formula.domain.evaluation.provider.LogicalBooleanExpressionTestProvider#provideOperationForValueLeftAndStructuredReferenceRight",
//            "com.damdamdeo.formula.domain.evaluation.provider.LogicalBooleanExpressionTestProvider#provideOperationForValueLeftAndValueRight"
//            "com.damdamdeo.formula.domain.evaluation.provider.CompoundExpressionTestProvider#provide"
    })
    void shouldEvaluate(final Given given, final ExpectedEvaluation expectedEvaluation) {
        // Given
        final Evaluator evaluator = new Evaluator(new NumericalContext(), given.structuredReferences(), new NoOpPartEvaluationListener());

        // When
        final Evaluated evaluated = given.expression().accept(evaluator);

        // Then
        assertThat(evaluated).isEqualTo(expectedEvaluation.evaluated());
    }

    @ParameterizedTest
    @MethodSource({
//            "com.damdamdeo.formula.domain.evaluation.provider.ArgumentExpressionTestProvider#provide"
//            "com.damdamdeo.formula.domain.evaluation.provider.ArithmeticExpressionTestProvider#provideOperationForStructuredReferenceLeftAndStructuredReferenceRight",
//            "com.damdamdeo.formula.domain.evaluation.provider.ArithmeticExpressionTestProvider#provideOperationForStructuredReferenceLeftAndValueRight",
//            "com.damdamdeo.formula.domain.evaluation.provider.ArithmeticExpressionTestProvider#provideOperationForValueLeftAndStructuredReferenceRight",
//            "com.damdamdeo.formula.domain.evaluation.provider.ArithmeticExpressionTestProvider#provideOperationForValueLeftAndValueRight"
//            "com.damdamdeo.formula.domain.evaluation.provider.LogicalComparisonExpressionTestProvider#provideLogicalComparisonFunctions"
//            "com.damdamdeo.formula.domain.evaluation.provider.LogicalBooleanExpressionTestProvider#provideOperationForStructuredReferenceLeftAndStructuredReferenceRight",
//            "com.damdamdeo.formula.domain.evaluation.provider.LogicalBooleanExpressionTestProvider#provideOperationForStructuredReferenceLeftAndValueRight",
//            "com.damdamdeo.formula.domain.evaluation.provider.LogicalBooleanExpressionTestProvider#provideOperationForValueLeftAndStructuredReferenceRight",
//            "com.damdamdeo.formula.domain.evaluation.provider.LogicalBooleanExpressionTestProvider#provideOperationForValueLeftAndValueRight"
//            "com.damdamdeo.formula.domain.evaluation.provider.CompoundExpressionTestProvider#provide"
    })
    void shouldDebugOperation(final Given given, final ExpectedEvaluation expectedEvaluation,
                              final EvaluatedAtProvider evaluatedAtProvider) {
        // Given
        final DebugPartEvaluationListener debugPartEvaluationListener = new DebugPartEvaluationListener(evaluatedAtProvider);
        final Evaluator evaluator = new Evaluator(new NumericalContext(), given.structuredReferences(), debugPartEvaluationListener);

        // When
        given.expression().accept(evaluator);

        // Then
        assertThat(debugPartEvaluationListener.intermediateResults()).isEqualTo(expectedEvaluation.intermediateResults());
    }


}
