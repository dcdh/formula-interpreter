package com.damdamdeo.formula.domain;

import io.smallrye.mutiny.Uni;

import java.util.Optional;

public interface Validator<R extends SyntaxError> {
    Uni<Optional<R>> validate(Formula formula) throws ValidationException;
}
