package com.damdamdeo.formula.infrastructure.antlr;

import com.damdamdeo.formula.FormulaLexer;
import com.damdamdeo.formula.FormulaParser;
import com.damdamdeo.formula.domain.Formula;
import io.smallrye.mutiny.Uni;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public final class AntlrParseTreeGenerator {
    public Uni<ParseTree> generate(final Formula formula) {
        return Uni.createFrom().item(() -> {
            final FormulaLexer lexer = new FormulaLexer(CharStreams.fromString(formula.formula()));
            final SyntaxErrorListener syntaxErrorListener = new SyntaxErrorListener();
            final FormulaParser parser = new FormulaParser(new CommonTokenStream(lexer));
            parser.removeErrorListeners();
            parser.addErrorListener(syntaxErrorListener);
            final ParseTree tree = parser.program();
            if (syntaxErrorListener.hasSyntaxError()) {
                throw new AntlrSyntaxErrorException(formula, syntaxErrorListener.syntaxError());
            }
            return tree;
        });
    }
}
