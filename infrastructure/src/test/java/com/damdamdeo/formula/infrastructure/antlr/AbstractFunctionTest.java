package com.damdamdeo.formula.infrastructure.antlr;

import com.damdamdeo.formula.domain.EvaluationResult;
import com.damdamdeo.formula.domain.evaluation.provider.EvaluationTestResolver;
import com.damdamdeo.formula.domain.provider.StubbedEvaluatedAtProviderTestProvider;
import com.damdamdeo.formula.domain.spi.EvaluatedAtProvider;
import io.quarkus.cache.Cache;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.function.Consumer;

import static org.mockito.Mockito.mock;

@ExtendWith(EvaluationTestResolver.class)
@ExtendWith(StubbedEvaluatedAtProviderTestProvider.class)
public abstract class AbstractFunctionTest {
    protected AntlrParser antlrExecutor;
    protected EvaluatedAtProvider evaluatedAtProvider;

    @BeforeEach
    public void setup(final EvaluatedAtProvider evaluatedAtProvider) {
        this.antlrExecutor = new AntlrParser(evaluatedAtProvider, new DefaultAntlrParseTreeGenerator(evaluatedAtProvider),
                mock(ParserMapping.class), mock(Cache.class));
        this.evaluatedAtProvider = evaluatedAtProvider;
    }

    protected void assertOnExecutionResultReceived(final Uni<EvaluationResult> executionResult, final Consumer<EvaluationResult> assertionLogic) {
        final UniAssertSubscriber<EvaluationResult> subscriber = executionResult
                .subscribe()
                .withSubscriber(UniAssertSubscriber.create());
        final EvaluationResult evaluationResultToAssert = subscriber.awaitItem().getItem();
        assertionLogic.accept(evaluationResultToAssert);
    }

    protected void assertOnFailure(final Uni<EvaluationResult> executionResult, final Consumer<Throwable> assertionLogic) {
        final UniAssertSubscriber<EvaluationResult> subscriber = executionResult
                .subscribe()
                .withSubscriber(UniAssertSubscriber.create());
        subscriber.awaitFailure(assertionLogic);
    }
}
