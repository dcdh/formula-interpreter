package com.damdamdeo.formula.infrastructure.evaluation.expression;

import com.damdamdeo.formula.domain.Evaluated;
import com.damdamdeo.formula.domain.NoOpPartEvaluationListener;
import com.damdamdeo.formula.domain.NumericalContext;
import com.damdamdeo.formula.domain.provider.Expected;
import com.damdamdeo.formula.infrastructure.evaluation.provider.DebugFormulaArgumentsProvider;
import com.damdamdeo.formula.infrastructure.evaluation.provider.EvaluationArgumentArgumentsProvider;
import com.damdamdeo.formula.infrastructure.evaluation.provider.EvaluationFunctionsArgumentsProvider;
import com.damdamdeo.formula.infrastructure.evaluation.provider.GivenFormula;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
class DefaultAntlrMappingExpressionEvaluationPipelineTest {

    @Inject
    DefaultAntlrMappingExpressionEvaluationPipeline defaultAntlrMappingExpressionEvaluationPipeline;

    @ParameterizedTest
    @ArgumentsSource(EvaluationFunctionsArgumentsProvider.class)
    @ArgumentsSource(EvaluationArgumentArgumentsProvider.class)
    void shouldEvaluate(final GivenFormula givenFormula, final Expected expected) {
        // Given
        final DefaultAntlrMappingExpressionLoaded loaded = defaultAntlrMappingExpressionEvaluationPipeline.load(givenFormula.formula());

        // When
        final Evaluated evaluated = defaultAntlrMappingExpressionEvaluationPipeline.evaluate(loaded, new NoOpPartEvaluationListener(), List.of(), new NumericalContext());

        // Then
        assertThat(evaluated.value()).isEqualTo(expected.value());
    }

    @ParameterizedTest
    @ArgumentsSource(DebugFormulaArgumentsProvider.class)
    void shouldDebug() {
        // Given
//        final PartEvaluationListener partEvaluationListener = new DebugPartEvaluationListener();
        // TODO tester le result
        // TODO tester les intermediate results
        // TODO tester l'expression
//        je dois tester le full evaluation
        throw new RuntimeException("TODO");
    }
}