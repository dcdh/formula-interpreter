package com.damdamdeo.formula.domain.evaluation;

import com.damdamdeo.formula.domain.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class EvaluationTest {

    @ParameterizedTest
    @MethodSource({
            "com.damdamdeo.formula.domain.evaluation.provider.EvaluationTestProvider#provideExpressions"
    })
    void shouldProcessExpression(final Formula formula, final Expression expression, final Value value) {
        final Evaluator evaluator = new Evaluator(new NumericalContext(), List.of(), new NoOpPartEvaluationListener());
        final Evaluated accept = expression.accept(evaluator);

        assertThat(accept.value()).isEqualTo(value);
    }
}