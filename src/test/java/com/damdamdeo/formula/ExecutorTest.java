package com.damdamdeo.formula;

import com.damdamdeo.formula.syntax.SyntaxError;
import com.damdamdeo.formula.syntax.SyntaxErrorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ExecutorTest {

    private Executor executor;

    @BeforeEach
    public void setup() {
        executor = new Executor(
                new Validator());
    }

    @Test
    public void shouldFailOnUnrecognizedToken() {
        assertThatThrownBy(() -> executor.execute(new Formula("!!!!#boom")))
                .isInstanceOf(SyntaxErrorException.class)
                .hasFieldOrPropertyWithValue("syntaxError",
                        new SyntaxError(1, 4, "token recognition error at: '#'"));
    }

    @Test
    public void shouldFailWhenFormulaIsDefinedOnMultipleLines() {
        assertThatThrownBy(() -> executor.execute(new Formula("Hello\nWorld")))
                .isInstanceOf(SyntaxErrorException.class)
                .hasFieldOrPropertyWithValue("syntaxError",
                        new SyntaxError(2, 0, "extraneous input 'World' expecting <EOF>"));
    }

}
