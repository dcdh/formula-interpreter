package com.damdamdeo.formula.domain.spi;

import com.damdamdeo.formula.domain.Formula;
import com.damdamdeo.formula.domain.SyntaxError;
import io.smallrye.mutiny.Uni;

import java.util.Optional;

public interface Validator<R extends SyntaxError> {
    Uni<Optional<R>> validate(Formula formula);
}
