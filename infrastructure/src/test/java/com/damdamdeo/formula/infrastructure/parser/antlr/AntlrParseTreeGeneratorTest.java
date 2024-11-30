package com.damdamdeo.formula.infrastructure.parser.antlr;

import com.damdamdeo.formula.infrastructure.parser.antlr.resolver.AntlrParseTreeGeneratorParameterResolver;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@QuarkusTest
@ExtendWith(AntlrParseTreeGeneratorParameterResolver.class)
class AntlrParseTreeGeneratorTest {

    @Inject
    AntlrParseTreeGenerator antlrParseTreeGenerator;

    @Test
    void shouldGenerateAntlrParseTreeWhenFormulaIsValid(final AntlrParseTreeGeneratorParameterResolver.ValidFormula validFormula) {
        // Given

        // When && Then
        final ParseTree parseTree = assertDoesNotThrow(() -> antlrParseTreeGenerator.generate(validFormula.formula()));
        assertThat(parseTree).isNotNull();
    }

    @Test
    void shouldThrowAntlrSyntaxErrorExceptionWhenFormulaIsInvalid(final AntlrParseTreeGeneratorParameterResolver.InvalidFormula invalidFormula) {
        // Given

        // When && Then
        assertThatThrownBy(() -> antlrParseTreeGenerator.generate(invalidFormula.formula()))
                .isEqualTo(invalidFormula.cause());
    }
}
