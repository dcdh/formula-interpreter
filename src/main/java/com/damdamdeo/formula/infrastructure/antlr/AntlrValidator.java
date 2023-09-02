package com.damdamdeo.formula.infrastructure.antlr;

import com.damdamdeo.formula.domain.Formula;
import com.damdamdeo.formula.domain.ValidationException;
import com.damdamdeo.formula.domain.Validator;
import io.smallrye.mutiny.Uni;

import java.util.Objects;
import java.util.Optional;

public class AntlrValidator implements Validator<AntlrSyntaxError> {
    private final AntlrParseTreeGenerator antlrParseTreeGenerator;

    public AntlrValidator(final AntlrParseTreeGenerator antlrParseTreeGenerator) {
        this.antlrParseTreeGenerator = Objects.requireNonNull(antlrParseTreeGenerator);
    }

    @Override
    public Uni<Optional<AntlrSyntaxError>> validate(final Formula formula) throws ValidationException {
        return this.antlrParseTreeGenerator.generate(formula)
                .onItem().transform(parseTree -> Optional.<AntlrSyntaxError>empty())
                .onFailure(AntlrSyntaxErrorException.class)
                .recoverWithUni(syntaxErrorException -> Uni.createFrom().item(Optional.of(((AntlrSyntaxErrorException) syntaxErrorException).syntaxError())))
                .onFailure(Exception.class)
                .transform(ValidationException::new);
    }
}
