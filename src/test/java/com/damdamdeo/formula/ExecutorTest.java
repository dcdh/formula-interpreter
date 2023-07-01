package com.damdamdeo.formula;

import com.damdamdeo.formula.structuredreference.StructuredData;
import com.damdamdeo.formula.syntax.SyntaxError;
import com.damdamdeo.formula.syntax.SyntaxErrorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ExecutorTest {

    private Executor executor;

    @BeforeEach
    public void setup() {
        executor = new Executor(
                () -> new ExecutionId(new UUID(0, 0)),
                new InMemoryExecutionLogger(),
                new Validator(), new NumericalContext());
    }

    @Test
    public void shouldFailOnUnrecognizedToken() {
        assertThatThrownBy(() -> executor.execute(new Formula("\""), new StructuredData()))
                .isInstanceOf(SyntaxErrorException.class)
                .hasFieldOrPropertyWithValue("syntaxError",
                        new SyntaxError(1, 1, "mismatched input '<EOF>' expecting {'ADD', 'SUB', 'DIV', 'MUL', 'GT', 'GTE', 'EQ', 'NEQ', 'LT', 'LTE', 'AND', 'OR', 'IF', 'IFERROR', 'ISNUM', 'ISLOGICAL', 'ISTEXT', 'ISBLANK', 'ISNA', 'ISERROR', 'IFNA', TRUE, FALSE, STRUCTURED_REFERENCE, VALUE, NUMERIC}"));
    }

    @Test
    public void shouldFailWhenFormulaIsDefinedOnMultipleLines() {
        assertThatThrownBy(() -> executor.execute(new Formula("\"Hello\"\n\"World\""), new StructuredData()))
                .isInstanceOf(SyntaxErrorException.class)
                .hasFieldOrPropertyWithValue("syntaxError",
                        new SyntaxError(2, 0, "extraneous input '\"World\"' expecting <EOF>"));
    }

}
