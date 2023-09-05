package com.damdamdeo.formula.domain.usecase;

import io.smallrye.mutiny.Uni;

public interface UseCase<R, C extends UseCaseCommand> {
    Uni<R> execute(C command);
}
