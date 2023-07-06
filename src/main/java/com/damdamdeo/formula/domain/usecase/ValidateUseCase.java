package com.damdamdeo.formula.domain.usecase;

import com.damdamdeo.formula.domain.UseCase;
import com.damdamdeo.formula.domain.Validator;

import java.util.Objects;

public final class ValidateUseCase<R> implements UseCase<R, ValidateCommand> {
    private final Validator<R> validator;

    public ValidateUseCase(final Validator<R> validator) {
        this.validator = Objects.requireNonNull(validator);
    }

    @Override
    public R execute(final ValidateCommand command) {
        return validator.validate(command.formula());
    }
}
