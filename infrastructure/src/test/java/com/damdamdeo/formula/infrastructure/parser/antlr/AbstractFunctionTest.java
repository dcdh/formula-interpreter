package com.damdamdeo.formula.infrastructure.parser.antlr;

import com.damdamdeo.formula.domain.provider.StubbedProcessedAtProviderParameterResolver;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.mock;

@ExtendWith(StubbedProcessedAtProviderParameterResolver.class)
public abstract class AbstractFunctionTest {
//    protected AntlrParser antlrExecutor;
//    protected ProcessedAtProvider processedAtProvider;
//
//    @BeforeEach
//    public void setup(final ProcessedAtProvider processedAtProvider) {
//        this.antlrExecutor = new AntlrParser(processedAtProvider, new DefaultAntlrParseTreeGenerator(processedAtProvider),
//                mock(ParserProcessing.class), mock(Cache.class));
//        this.processedAtProvider = processedAtProvider;
//    }
//
//    protected Formula formula4Test(final String formula) {
//        return new Formula(formula);
//    }
//
//    protected void assertOnExecutionResultReceived(final Uni<EvaluationResult> executionResult, final Consumer<EvaluationResult> assertionLogic) {
//        final UniAssertSubscriber<EvaluationResult> subscriber = executionResult
//                .subscribe()
//                .withSubscriber(UniAssertSubscriber.create());
//        final EvaluationResult evaluationResultToAssert = subscriber.awaitItem().getItem();
//        assertionLogic.accept(evaluationResultToAssert);
//    }
//
//    protected void assertOnFailure(final Uni<EvaluationResult> executionResult, final Consumer<Throwable> assertionLogic) {
//        final UniAssertSubscriber<EvaluationResult> subscriber = executionResult
//                .subscribe()
//                .withSubscriber(UniAssertSubscriber.create());
//        subscriber.awaitFailure(assertionLogic);
//    }
}
