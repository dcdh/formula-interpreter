package com.damdamdeo.formula.infrastructure.parser.antlr;

import com.damdamdeo.formula.FormulaLexer;
import com.damdamdeo.formula.FormulaParser;
import com.damdamdeo.formula.domain.Formula;
import jakarta.enterprise.context.ApplicationScoped;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

@ApplicationScoped
public final class AntlrParseTreeGenerator {

    public ParseTree generate(final Formula formula) throws AntlrSyntaxErrorException {
        final FormulaLexer lexer = new FormulaLexer(CharStreams.fromString(formula.formula()));
        final AntlrSyntaxErrorListener antlrSyntaxErrorListener = new AntlrSyntaxErrorListener();
        final FormulaParser parser = new FormulaParser(new CommonTokenStream(lexer));
        parser.removeErrorListeners();
        parser.addErrorListener(antlrSyntaxErrorListener);
        final ParseTree parseTree = parser.program();
        if (antlrSyntaxErrorListener.hasSyntaxError()) {
            throw new AntlrSyntaxErrorException(formula, antlrSyntaxErrorListener.syntaxError());
        }
        return parseTree;
    }
}
