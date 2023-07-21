package com.damdamdeo.formula.infrastructure.antlr;

import com.damdamdeo.formula.FormulaLexer;
import com.damdamdeo.formula.FormulaParser;
import com.damdamdeo.formula.domain.Formula;
import com.damdamdeo.formula.domain.ValidationException;
import com.damdamdeo.formula.domain.Validator;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.Optional;

public class AntlrValidator implements Validator<AntlrSyntaxError> {

    public ParseTree doValidate(final Formula formula) throws AntlrSyntaxErrorException {
        final FormulaLexer lexer = new FormulaLexer(CharStreams.fromString(formula.formula()));
        lexer.removeErrorListeners();
        final SyntaxErrorListener syntaxErrorListener = new SyntaxErrorListener();
        lexer.addErrorListener(syntaxErrorListener);
        if (syntaxErrorListener.hasSyntaxError()) {
            throw new AntlrSyntaxErrorException(formula, syntaxErrorListener.syntaxError());
        }
        final FormulaParser parser = new FormulaParser(new CommonTokenStream(lexer));
        parser.removeErrorListeners();
        parser.addErrorListener(syntaxErrorListener);
        ParseTree tree = parser.program();
        if (syntaxErrorListener.hasSyntaxError()) {
            throw new AntlrSyntaxErrorException(formula, syntaxErrorListener.syntaxError());
        }
        return tree;
    }

    @Override
    public Optional<AntlrSyntaxError> validate(final Formula formula) throws ValidationException {
        try {
            doValidate(formula);
            return Optional.empty();
        } catch (final AntlrSyntaxErrorException syntaxErrorException) {
            return Optional.of(syntaxErrorException.syntaxError());
        } catch (final Exception exception) {
            throw new ValidationException(exception);
        }
    }
}
