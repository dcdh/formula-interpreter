package com.damdamdeo.formula.domain;

public interface UseCase<R, C extends UseCaseCommand> {
    R execute(C command);
}
