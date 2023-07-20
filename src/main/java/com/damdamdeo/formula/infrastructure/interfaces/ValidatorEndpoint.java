package com.damdamdeo.formula.infrastructure.interfaces;

import com.damdamdeo.formula.domain.Formula;
import com.damdamdeo.formula.domain.ValidationException;
import com.damdamdeo.formula.domain.usecase.ValidateCommand;
import com.damdamdeo.formula.domain.usecase.ValidateUseCase;
import com.damdamdeo.formula.infrastructure.antlr.AntlrSyntaxError;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

import java.util.Objects;

@Path("/validate")
public final class ValidatorEndpoint {
    private final ValidateUseCase<AntlrSyntaxError> validateUseCase;

    public ValidatorEndpoint(final ValidateUseCase<AntlrSyntaxError> validateUseCase) {
        this.validateUseCase = Objects.requireNonNull(validateUseCase);
    }

    @POST
    @Produces("application/vnd.formula-validator-v1+json")
    public SyntaxErrorDTO validate(@FormParam("formula") final String formula) throws ValidationException {
        return validateUseCase.execute(new ValidateCommand(new Formula(formula)))
                .map(SyntaxErrorDTO::new)
                .orElse(null);
    }

}