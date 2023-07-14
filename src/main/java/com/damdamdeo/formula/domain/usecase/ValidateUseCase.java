package com.damdamdeo.formula.domain.usecase;

import com.damdamdeo.formula.domain.SyntaxError;
import com.damdamdeo.formula.domain.UseCase;
import com.damdamdeo.formula.domain.Validator;

import java.util.Objects;
import java.util.Optional;

public final class ValidateUseCase<R extends SyntaxError> implements UseCase<Optional<R>, ValidateCommand> {
    private final Validator<R> validator;

    public ValidateUseCase(final Validator<R> validator) {
        this.validator = Objects.requireNonNull(validator);
    }

    @Override
    public Optional<R> execute(final ValidateCommand command) {
        return validator.validate(command.formula());
    }
}
