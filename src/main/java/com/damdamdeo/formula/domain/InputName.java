package com.damdamdeo.formula.domain;

import java.util.Objects;

public record InputName(String name) {
    public InputName {
        Objects.requireNonNull(name);
    }
}
