package com.damdamdeo.formula.infrastructure.antlr;

import com.damdamdeo.formula.domain.*;
import com.damdamdeo.formula.domain.spi.EvaluatedAtProvider;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import org.junit.jupiter.api.BeforeEach;

import java.time.ZonedDateTime;
import java.util.function.Consumer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class AbstractFunctionTest {
    protected AntlrParser antlrExecutor;
    protected EvaluatedAtProvider evaluatedAtProvider;

    @BeforeEach
    public void setup() {
        evaluatedAtProvider = mock(EvaluatedAtProvider.class);
        when(evaluatedAtProvider.now())
                .thenReturn(new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")))
                .thenReturn(new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")));
        antlrExecutor = new AntlrParser(evaluatedAtProvider, new DefaultAntlrParseTreeGenerator(evaluatedAtProvider));
    }

    protected Formula formula4Test(final String formula) {
        return new Formula(formula);
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
