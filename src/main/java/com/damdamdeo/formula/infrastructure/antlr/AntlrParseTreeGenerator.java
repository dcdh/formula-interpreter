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
        private final AntlrSyntaxErrorListener antlrSyntaxErrorListener;

        public GeneratorResult(final Formula formula,
                               final ParseTree parseTree,
                               final AntlrSyntaxErrorListener antlrSyntaxErrorListener) {
            this.formula = Objects.requireNonNull(formula);
            this.parseTree = Objects.requireNonNull(parseTree);
            this.antlrSyntaxErrorListener = Objects.requireNonNull(antlrSyntaxErrorListener);
        }

        public ParseTree parseTree() throws AntlrSyntaxErrorException {
            if (antlrSyntaxErrorListener.hasSyntaxError()) {
                throw new AntlrSyntaxErrorException(formula, antlrSyntaxErrorListener.syntaxError());
            }
            return parseTree;
        }
    }

}