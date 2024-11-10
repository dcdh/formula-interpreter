package com.damdamdeo.formula.domain.evaluation;

import com.damdamdeo.formula.domain.*;
import com.damdamdeo.formula.domain.evaluation.provider.EvaluationTestResolver;
import com.damdamdeo.formula.domain.provider.StubbedEvaluatedAtProviderTestProvider;
import com.damdamdeo.formula.domain.spi.EvaluatedAtProvider;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(StubbedEvaluatedAtProviderTestProvider.class)
@ExtendWith(EvaluationTestResolver.class)
class EvaluationTest {
non revoir le test !
    reprendre d'abord l'infra !
    @EvaluationTestResolver.CompoundAddMulTest
    void shouldProcessExpression(final Formula formula, final Expression expression, final Value value) {
        final Evaluator evaluator = new Evaluator(new NumericalContext(), List.of(), new NoOpPartEvaluationListener());
        final Evaluated accept = expression.accept(evaluator);

        assertThat(accept.value()).isEqualTo(value);
    }

    @ParameterizedTest
    @MethodSource({
            "com.damdamdeo.formula.domain.evaluation.provider.EvaluationTestProvider#provideExpressions"
    })
    void shouldDebugExpression(final Formula formula, final Expression expression, final Value value,
                               final List<IntermediateResult> intermediateResults,
                               final EvaluatedAtProvider evaluatedAtProvider) {
        final DebugPartEvaluationListener debugPartEvaluationListener = new DebugPartEvaluationListener(evaluatedAtProvider);
        final Evaluator evaluator = new Evaluator(new NumericalContext(), List.of(), debugPartEvaluationListener);
        expression.accept(evaluator);

        assertThat(debugPartEvaluationListener.intermediateResults()).containsExactlyElementsOf(
                intermediateResults);
    }
putain je vais devoir evaluer toutes les expressions !!!
}