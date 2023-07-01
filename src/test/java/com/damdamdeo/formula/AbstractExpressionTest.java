package com.damdamdeo.formula;

import org.junit.jupiter.api.BeforeEach;

import java.util.UUID;

public abstract class AbstractExpressionTest {

    protected Executor executor;

    @BeforeEach
    public void setup() {
        executor = new Executor(
                () -> new ExecutionId(new UUID(0, 0)),
                new InMemoryExecutionLogger(),
                new Validator(), new NumericalContext());
    }

    protected Formula formula4Test(final String formula) {
        return new Formula(formula);
    }
}
