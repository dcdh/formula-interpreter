package com.damdamdeo.formula.infrastructure.antlr;

import com.damdamdeo.formula.FormulaLexer;
import com.damdamdeo.formula.FormulaParser;
import com.damdamdeo.formula.domain.Formula;
import com.damdamdeo.formula.domain.ValidationException;
import com.damdamdeo.formula.domain.Validator;
import io.smallrye.mutiny.Uni;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.Optional;

public class AntlrValidator implements Validator<AntlrSyntaxError> {

    public Uni<ParseTree> doValidate(final Formula formula) throws AntlrSyntaxErrorException {
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

    @Override
    public Uni<Optional<AntlrSyntaxError>> validate(final Formula formula) throws ValidationException {
        return doValidate(formula)
                .onItem().transform(tree -> Optional.<AntlrSyntaxError>empty())
                .onFailure(AntlrSyntaxErrorException.class)
                .recoverWithUni(syntaxErrorException -> Uni.createFrom().item(Optional.of(((AntlrSyntaxErrorException) syntaxErrorException).syntaxError())))
                .onFailure(Exception.class)
                .transform(ValidationException::new);
    }
}
