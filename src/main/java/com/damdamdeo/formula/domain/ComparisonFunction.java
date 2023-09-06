package com.damdamdeo.formula.domain;

public interface ComparisonFunction {
    Value execute(Value left, Value right, NumericalContext numericalContext);
}
