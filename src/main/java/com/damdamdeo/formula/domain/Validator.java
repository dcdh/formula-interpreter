package com.damdamdeo.formula.domain;

public interface Validator<R> {
    R validate(Formula formula);
}
