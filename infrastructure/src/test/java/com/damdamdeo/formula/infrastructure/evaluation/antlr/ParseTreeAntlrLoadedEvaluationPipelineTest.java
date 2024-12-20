package com.damdamdeo.formula.infrastructure.evaluation.antlr;

import com.damdamdeo.formula.domain.*;
import com.damdamdeo.formula.domain.provider.Expected;
import com.damdamdeo.formula.infrastructure.evaluation.provider.DebugFormulaArgumentsProvider;
import com.damdamdeo.formula.infrastructure.evaluation.provider.EvaluationArgumentArgumentsProvider;
import com.damdamdeo.formula.infrastructure.evaluation.provider.EvaluationFunctionsArgumentsProvider;
import com.damdamdeo.formula.infrastructure.evaluation.provider.GivenFormula;
import com.damdamdeo.formula.infrastructure.parser.antlr.AntlrParseTreeGenerator;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectSpy;
import jakarta.inject.Inject;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.Mockito;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@QuarkusTest
class ParseTreeAntlrLoadedEvaluationPipelineTest {
    @Inject
    ParseTreeAntlrLoadedEvaluationPipeline parseTreeAntlrLoadedEvaluationPipeline;

    @InjectSpy
    AntlrParseTreeGenerator antlrParseTreeGenerator;

    @Test
    void shouldLoadAntlrCallAntlrParseTreeGenerator() {
        // Given
        final Formula givenFormula = new Formula("true");
        final ParseTree givenParseTree = Mockito.mock(ParseTree.class);
        doReturn(givenParseTree).when(antlrParseTreeGenerator).generate(givenFormula);

        // When
        final ParseTreeAntlrLoaded parseTreeAntlrLoaded = parseTreeAntlrLoadedEvaluationPipeline.load(givenFormula);

        // Then
        assertThat(parseTreeAntlrLoaded).isEqualTo(new ParseTreeAntlrLoaded(givenParseTree));
    }

    @ParameterizedTest
    @ArgumentsSource(EvaluationFunctionsArgumentsProvider.class)
    @ArgumentsSource(EvaluationArgumentArgumentsProvider.class)
    void shouldEvaluate(final GivenFormula givenFormula, final Expected expected) {
        // Given
        final ParseTreeAntlrLoaded loaded = parseTreeAntlrLoadedEvaluationPipeline.load(givenFormula.formula());

        // When
        final Evaluated evaluated = parseTreeAntlrLoadedEvaluationPipeline.evaluate(loaded, new NoOpPartEvaluationListener(), List.of(), new NumericalContext());

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
//        je dois tester le full evaluation
        throw new RuntimeException("TODO");
    }
}