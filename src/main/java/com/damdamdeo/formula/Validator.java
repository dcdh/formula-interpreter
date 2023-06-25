package com.damdamdeo.formula;

import com.damdamdeo.formula.syntax.SyntaxErrorException;
import com.damdamdeo.formula.syntax.SyntaxErrorListener;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class Validator {

    public ParseTree validate(final Formula formula) throws SyntaxErrorException {
        final FormulaLexer lexer = new FormulaLexer(CharStreams.fromString(formula.formula()));
        lexer.removeErrorListeners();
        final SyntaxErrorListener syntaxErrorListener = new SyntaxErrorListener();
        lexer.addErrorListener(syntaxErrorListener);
        if (syntaxErrorListener.hasSyntaxError()) {
            throw new SyntaxErrorException(syntaxErrorListener.syntaxError());
        }
        final FormulaParser parser = new FormulaParser(new CommonTokenStream(lexer));
        parser.removeErrorListeners();
        parser.addErrorListener(syntaxErrorListener);
        ParseTree tree = parser.prog();
        if (syntaxErrorListener.hasSyntaxError()) {
            throw new SyntaxErrorException(syntaxErrorListener.syntaxError());
        }
        return tree;
    }

}
