package com.damdamdeo.formula.result;

import java.util.Objects;

public final class UnknownReferenceResult implements Result {
    private final String result = "#REF!";
    @Override
    public String result() {
        return result;
    }
    @Override
    public boolean isUnknown() {
        return true;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnknownReferenceResult that = (UnknownReferenceResult) o;
        return Objects.equals(result, that.result);
    }

    @Override
    public int hashCode() {
        return Objects.hash(result);
    }
}
