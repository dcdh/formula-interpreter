package com.damdamdeo.formula.infrastructure.antlr;

import com.damdamdeo.formula.domain.*;
import com.damdamdeo.formula.domain.spi.ExecutedAtProvider;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import org.junit.jupiter.api.BeforeEach;

import java.time.ZonedDateTime;
import java.util.function.Consumer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class AbstractFunctionTest {
    protected AntlrExecutor antlrExecutor;
    protected ExecutedAtProvider executedAtProvider;

    @BeforeEach
    public void setup() {
        executedAtProvider = mock(ExecutedAtProvider.class);
        when(executedAtProvider.now())
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")));
        antlrExecutor = new AntlrExecutor(executedAtProvider, new NumericalContext(), new DefaultAntlrParseTreeGenerator(executedAtProvider));
    }

    protected Formula formula4Test(final String formula) {
        return new Formula(formula);
    }

    protected void assertOnExecutionResultReceived(final Uni<ExecutionResult> executionResult, final Consumer<ExecutionResult> assertionLogic) {
        final UniAssertSubscriber<ExecutionResult> subscriber = executionResult
                .subscribe()
                .withSubscriber(UniAssertSubscriber.create());
        final ExecutionResult executionResultToAssert = subscriber.awaitItem().getItem();
        assertionLogic.accept(executionResultToAssert);
    }

    protected void assertOnFailure(final Uni<ExecutionResult> executionResult, final Consumer<Throwable> assertionLogic) {
        final UniAssertSubscriber<ExecutionResult> subscriber = executionResult
                .subscribe()
                .withSubscriber(UniAssertSubscriber.create());
        subscriber.awaitFailure(assertionLogic);
    }
}
