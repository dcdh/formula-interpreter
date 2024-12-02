package com.damdamdeo.formula.domain.provider;

import com.damdamdeo.formula.domain.Value;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ValueProviders {

    public enum Type {
        TEXT, NUMERIC, BOOLEAN_TRUE, BOOLEAN_FALSE, NOT_AVAILABLE, UNKNOWN_REF, NOT_A_NUMERICAL, DIVIDED_BY_ZERO, NOT_A_LOGICAL_VALUE;

        public static boolean matchTag(final String tag) {
            for (final Type type : Type.values()) {
                if (type.name().equals(tag)) {
                    return true;
                }
            }
            return false;
        }
    }

    private final Map<Type, List<Value>> VALUES_BY_TYPE = new HashMap<>();

    public static List<Value> byType(final Type type) {
        return new ValueProviders().VALUES_BY_TYPE.get(type);
    }

    public ValueProviders() {
        VALUES_BY_TYPE.put(Type.TEXT, List.of(
                Value.ofText("\"Hello World\"")
        ));
        VALUES_BY_TYPE.put(Type.NUMERIC, List.of(
                Value.ofNumeric("0"),
                Value.ofNumeric("0.00"),
                Value.ofNumeric("123"),
                Value.ofNumeric("-123"),
                Value.ofNumeric("1.23E3"),
                Value.ofNumeric("1.23E+3"),
                Value.ofNumeric("12.3E+7"),
                Value.ofNumeric("12.0"),
                Value.ofNumeric("12.3"),
                Value.ofNumeric("0.00123"),
                Value.ofNumeric("-1.23E-12"),
                Value.ofNumeric("1234.5E-4"),
                Value.ofNumeric("0E+7"),
                Value.ofNumeric("-0")
        ));
        VALUES_BY_TYPE.put(Type.BOOLEAN_TRUE, List.of(
                Value.ofBoolean("true"),
                Value.ofBoolean("1"),
                Value.ofTrue(),
                Value.ofOne()
        ));
        VALUES_BY_TYPE.put(Type.BOOLEAN_FALSE, List.of(
                Value.ofBoolean("false"),
                Value.ofBoolean("0"),
                Value.ofFalse(),
                Value.ofZero()
        ));
        VALUES_BY_TYPE.put(Type.NOT_AVAILABLE, List.of(
                Value.ofNotAvailable()
        ));
        VALUES_BY_TYPE.put(Type.UNKNOWN_REF, List.of(
                Value.ofUnknownRef()
        ));
        VALUES_BY_TYPE.put(Type.NOT_A_NUMERICAL, List.of(
                Value.ofNotANumericalValue()
        ));
        VALUES_BY_TYPE.put(Type.DIVIDED_BY_ZERO, List.of(
                Value.ofDividedByZero()
        ));
        VALUES_BY_TYPE.put(Type.NOT_A_LOGICAL_VALUE, List.of(
                Value.ofNotALogicalValue()
        ));
    }
}
