package com.damdamdeo.formula.domain;

public interface ComparisonFunction {
    Value evaluate(Value left, Value right, NumericalContext numericalContext);
}
