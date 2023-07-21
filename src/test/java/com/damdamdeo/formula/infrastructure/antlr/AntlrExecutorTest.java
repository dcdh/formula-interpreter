package com.damdamdeo.formula.infrastructure.antlr;

import com.damdamdeo.formula.domain.*;
import com.damdamdeo.formula.infrastructure.logger.InMemoryExecutionLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

public class AntlrExecutorTest {

    private AntlrExecutor antlrExecutor;

    @BeforeEach
    public void setup() {
        final ExecutedAtProvider executedAtProvider = mock(ExecutedAtProvider.class);
        antlrExecutor = new AntlrExecutor(
                () -> new ExecutionId(new UUID(0, 0)),
                new InMemoryExecutionLogger(),
                executedAtProvider,
                new NumericalContext());
    }

    @Test
    public void shouldFailOnUnrecognizedToken() {
        assertThatThrownBy(() -> antlrExecutor.execute(new Formula("\""), new StructuredData()))
                .isInstanceOf(ExecutionException.class)
                .cause()
                .isInstanceOf(AntlrSyntaxErrorException.class)
                .hasFieldOrPropertyWithValue("syntaxError",
                        new AntlrSyntaxError(1, 1, "mismatched input '<EOF>' expecting {'ADD', 'SUB', 'DIV', 'MUL', 'GT', 'GTE', 'EQ', 'NEQ', 'LT', 'LTE', 'AND', 'OR', 'IF', 'IFERROR', 'ISNUM', 'ISLOGICAL', 'ISTEXT', 'ISBLANK', 'ISNA', 'ISERROR', 'IFNA', TRUE, FALSE, STRUCTURED_REFERENCE, VALUE, NUMERIC}"));
    }

    @Test
    public void shouldFailWhenFormulaIsDefinedOnMultipleLines() {
        assertThatThrownBy(() -> antlrExecutor.execute(new Formula("\"Hello\"\n\"World\""), new StructuredData()))
                .isInstanceOf(ExecutionException.class)
                .cause()
                .isInstanceOf(AntlrSyntaxErrorException.class)
                .hasFieldOrPropertyWithValue("syntaxError",
                        new AntlrSyntaxError(2, 0, "extraneous input '\"World\"' expecting <EOF>"));
    }

}
