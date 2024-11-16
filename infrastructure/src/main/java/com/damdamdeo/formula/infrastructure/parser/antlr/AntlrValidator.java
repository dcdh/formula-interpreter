package com.damdamdeo.formula.infrastructure.parser.antlr;

import com.damdamdeo.formula.domain.Formula;
import com.damdamdeo.formula.domain.ValidationException;
import com.damdamdeo.formula.domain.spi.Validator;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Objects;
import java.util.Optional;

@ApplicationScoped
public class AntlrValidator implements Validator<AntlrSyntaxError> {
    private final AntlrParseTreeGenerator antlrParseTreeGenerator;

    public AntlrValidator(final AntlrParseTreeGenerator antlrParseTreeGenerator) {
        this.antlrParseTreeGenerator = Objects.requireNonNull(antlrParseTreeGenerator);
    }

    @Override
    public Uni<Optional<AntlrSyntaxError>> validate(final Formula formula) {
        return Uni.createFrom().item(() -> this.antlrParseTreeGenerator.generate(formula))
                .map(toto -> Optional.<AntlrSyntaxError>empty())
                .onFailure(AntlrSyntaxErrorException.class)
                .recoverWithItem(antlrSyntaxErrorException -> Optional.of(((AntlrSyntaxErrorException) antlrSyntaxErrorException).antlrSyntaxError()))
                .onFailure(Exception.class)
                .transform(ValidationException::new);
    }
}
