package com.damdamdeo.formula.infrastructure.antlr;

import com.damdamdeo.formula.domain.ExecutedAt;
import com.damdamdeo.formula.domain.ExecutedAtProvider;
import com.damdamdeo.formula.domain.Formula;
import org.junit.jupiter.api.BeforeEach;

import java.time.ZonedDateTime;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class AbstractExpressionTest {
    protected AntlrExecutor antlrExecutor;
    protected ExecutedAtProvider executedAtProvider;

    @BeforeEach
    public void setup() {
        executedAtProvider = mock(ExecutedAtProvider.class);
        when(executedAtProvider.now())
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")));
        antlrExecutor = new AntlrExecutor(executedAtProvider, new NumericalContext());
    }

    protected Formula formula4Test(final String formula) {
        return new Formula(formula);
    }
}
