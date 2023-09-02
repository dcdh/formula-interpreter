package com.damdamdeo.formula.infrastructure.antlr;

import com.damdamdeo.formula.domain.Formula;
import io.smallrye.mutiny.Uni;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.Objects;

public interface AntlrParseTreeGenerator {
    Uni<GeneratorResult> generate(final Formula formula);

    final class GeneratorResult {
        private final Formula formula;
        private final ParseTree parseTree;
        private final SyntaxErrorListener syntaxErrorListener;

        public GeneratorResult(final Formula formula,
                               final ParseTree parseTree,
                               final SyntaxErrorListener syntaxErrorListener) {
            this.formula = Objects.requireNonNull(formula);
            this.parseTree = Objects.requireNonNull(parseTree);
            this.syntaxErrorListener = Objects.requireNonNull(syntaxErrorListener);
        }

        public ParseTree parseTree() throws AntlrSyntaxErrorException {
            if (syntaxErrorListener.hasSyntaxError()) {
                throw new AntlrSyntaxErrorException(formula, syntaxErrorListener.syntaxError());
            }
            return parseTree;
        }
    }

}