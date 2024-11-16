package com.damdamdeo.formula.infrastructure.parser.antlr;

import com.damdamdeo.formula.domain.Formula;
import com.damdamdeo.formula.domain.ParserEvaluationProcessedIn;
import io.smallrye.mutiny.Uni;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.Objects;

public interface AntlrParseTreeGenerator {
    Uni<GeneratorResult> generate(final Formula formula);

    record GeneratorResult(Formula formula,
                           ParseTree parseTree,
                           AntlrSyntaxErrorListener antlrSyntaxErrorListener,
                           ParserEvaluationProcessedIn parserEvaluationProcessedIn) {

        public GeneratorResult {
            Objects.requireNonNull(formula);
            Objects.requireNonNull(parseTree);
            Objects.requireNonNull(antlrSyntaxErrorListener);
        }

        public ParseTree parseTree() {
            if (antlrSyntaxErrorListener.hasSyntaxError()) {
                throw new AntlrSyntaxErrorException(formula, antlrSyntaxErrorListener.syntaxError());
            }
            return parseTree;
        }
    }

}