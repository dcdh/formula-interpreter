package com.damdamdeo.formula.domain;

import java.util.Optional;

public interface Validator<R extends SyntaxError> {
    Optional<R> validate(Formula formula);
}
