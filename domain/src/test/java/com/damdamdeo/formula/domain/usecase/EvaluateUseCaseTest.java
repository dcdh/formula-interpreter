package com.damdamdeo.formula.domain.usecase;

import com.damdamdeo.formula.domain.Formula;
import com.damdamdeo.formula.domain.NumericalContext;
import com.damdamdeo.formula.domain.provider.StubbedEvaluatedAtProviderTestProvider;
import com.damdamdeo.formula.domain.spi.EvaluatedAtProvider;
import com.damdamdeo.formula.domain.spi.Parser;
import com.damdamdeo.formula.domain.usecase.provider.EvaluateUseCaseTestResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.mock;

//@ExtendWith(
//@ExtendWith(StubbedEvaluatedAtProviderTestProvider.class)
class EvaluateUseCaseTest {
    // I do not mock because I am inside the domain ???
//    private EvaluateUseCase evaluateUseCase;
//    private Parser parser;
//
//    @BeforeEach
//    void setUp(final EvaluatedAtProvider evaluatedAtProvider) {
//        this.parser = mock(Parser.class);
//        this.evaluateUseCase = new EvaluateUseCase(parser, evaluatedAtProvider, new NumericalContext());
//    }
//
//    en entrée je prends un uni avec le expected et
//    void shouldDebugEvaluation(final Formula formula,
//                               final EvaluateUseCaseTestResolver.StructuredReferences structuredReferences,
//                               ) {
//        // Given
//
//        // When
//
//        // Then
//
//    }
//
//    void shouldNotDebugEvaluation() {
//        // Given
//
//        // When
//
//        // Then
//
//    }
//
//    @Test
//    void shouldEvaluateUsingExpression() {
//        // Given
//
//        // When
//
//        // Then
//
//        FCK il va y avoir des choses à faire ...
//        throw new RuntimeException("TODO");
//    }
//
//    @Test
//    void shouldEvaluateUsingAntlr() {
//        // Given
//
//        // When
//
//        // Then
//
//        FCK il va y avoir des choses à faire ...
//        throw new RuntimeException("TODO");
//    }
//
//    /*
//        public Uni<EvaluationResult> execute(final EvaluateCommand command) {
//        final PartEvaluationListener partEvaluationListener = switch (command.debugFeature()) {
//            case ACTIVE -> new DebugPartEvaluationListener(evaluatedAtProvider);
//            case INACTIVE -> new NoOpPartEvaluationListener();
//        };
//        return switch (command.evaluateOn()) {
//            case ANTLR ->
//                    parser.process(command.formula(), new PartEvaluationCallback(partEvaluationListener, numericalContext, command.structuredReferences()));
//            case ANTLR_MAPPING_DOMAIN_EVAL -> parser.process(command.formula())
//                    .map(processingResult -> {
//                        final EvaluatedAtStart evaluatedAtStart = evaluatedAtProvider.now();
//                        final Evaluator evaluator = new Evaluator(new NumericalContext(), command.structuredReferences(), partEvaluationListener);
//                        final Evaluated evaluated = processingResult.expression().accept(evaluator);
//                        final List<IntermediateResult> intermediateResults = evaluator.intermediateResults();
//                        final EvaluatedAtEnd evaluatedAtEnd = evaluatedAtProvider.now();
//                        return new EvaluationResult(evaluated.value(),
//                                processingResult.parserEvaluationProcessedIn(),
//                                intermediateResults,
//                                new EvaluationProcessedIn(evaluatedAtStart, evaluatedAtEnd));
//                    });
//        };
//    }
//     */
}
