package com.damdamdeo.formula.infrastructure.antlr;

import com.damdamdeo.formula.domain.ExecutedAt;
import com.damdamdeo.formula.domain.ExecutedAtProvider;
import com.damdamdeo.formula.domain.ExecutionId;
import com.damdamdeo.formula.domain.Formula;
import com.damdamdeo.formula.infrastructure.logger.InMemoryExecutionLogger;
import org.junit.jupiter.api.BeforeEach;

import java.time.ZonedDateTime;
import java.util.UUID;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public abstract class AbstractExpressionTest {
    protected AntlrExecutor antlrExecutor;
    protected ExecutedAtProvider executedAtProvider;

    @BeforeEach
    public void setup() {
        executedAtProvider = mock(ExecutedAtProvider.class);
        doReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:30+01:00[Europe/Paris]"))).when(executedAtProvider).now();
        antlrExecutor = new AntlrExecutor(
                () -> new ExecutionId(new UUID(0, 0)),
                new InMemoryExecutionLogger(), executedAtProvider, new NumericalContext());
    }

    protected Formula formula4Test(final String formula) {
        return new Formula(formula);
    }
}
