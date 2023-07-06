package com.damdamdeo.formula.infrastructure.antlr;

import com.damdamdeo.formula.FormulaLexer;
import com.damdamdeo.formula.FormulaParser;
import com.damdamdeo.formula.domain.Formula;
import com.damdamdeo.formula.domain.Validator;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class AntlrValidator implements Validator<SyntaxError> {

    public ParseTree doValidate(final Formula formula) throws SyntaxErrorException {
        final FormulaLexer lexer = new FormulaLexer(CharStreams.fromString(formula.formula()));
        lexer.removeErrorListeners();
        final SyntaxErrorListener syntaxErrorListener = new SyntaxErrorListener();
        lexer.addErrorListener(syntaxErrorListener);
        if (syntaxErrorListener.hasSyntaxError()) {
            throw new SyntaxErrorException(formula, syntaxErrorListener.syntaxError());
        }
        final FormulaParser parser = new FormulaParser(new CommonTokenStream(lexer));
        parser.removeErrorListeners();
        parser.addErrorListener(syntaxErrorListener);
        ParseTree tree = parser.program();
        if (syntaxErrorListener.hasSyntaxError()) {
            throw new SyntaxErrorException(formula, syntaxErrorListener.syntaxError());
        }
        return tree;
    }

    @Override
    public SyntaxError validate(final Formula formula) {
        try {
            doValidate(formula);
            return null;
        } catch (final SyntaxErrorException syntaxErrorException) {
            return syntaxErrorException.syntaxError();
        }
    }
}
