package com.damdamdeo.formula;

import org.junit.jupiter.api.BeforeEach;

public abstract class AbstractExpressionTest {

    protected Executor executor;

    @BeforeEach
    public void setup() {
        executor = new Executor(new Validator());
    }

    protected Formula formula4Test(final String formula) {
        return new Formula(formula);
    }
}
