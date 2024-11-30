package com.damdamdeo.formula.domain.provider;

import com.damdamdeo.formula.domain.Value;
import com.damdamdeo.formula.domain.ValueTest;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class LogicalComparisonFunctionProviders {

    public enum Type {
        IF {
            @Override
            Expected notAvailable() {
                return new Expected(Value.ofNotAvailable());
            }

            @Override
            Expected unknownRef() {
                return new Expected(Value.ofUnknownRef());
            }

            @Override
            Expected numericalValueExpected() {
                return new Expected(Value.ofNotANumericalValue());
            }

            @Override
            Expected dividedByZero() {
                return new Expected(Value.ofDividedByZero());
            }

            @Override
            Expected trueBoolean() {
                return new Expected(Value.ofTrue());
            }

            @Override
            Expected falseBoolean() {
                return new Expected(Value.ofFalse());
            }

            @Override
            Expected zero() {
                return new Expected(Value.ofFalse());
            }

            @Override
            Expected one() {
                return new Expected(Value.ofTrue());
            }

            @Override
            Expected sixSixZero() {
                return new Expected(Value.ofNotALogicalValue());
            }

            @Override
            Expected empty() {
                return new Expected(Value.ofNotALogicalValue());
            }
        },
        IF_ERROR {
            @Override
            Expected notAvailable() {
                return new Expected(Value.ofTrue());
            }

            @Override
            Expected unknownRef() {
                return new Expected(Value.ofTrue());
            }

            @Override
            Expected numericalValueExpected() {
                return new Expected(Value.ofTrue());
            }

            @Override
            Expected dividedByZero() {
                return new Expected(Value.ofTrue());
            }

            @Override
            Expected trueBoolean() {
                return new Expected(Value.ofFalse());
            }

            @Override
            Expected falseBoolean() {
                return new Expected(Value.ofFalse());
            }

            @Override
            Expected zero() {
                return new Expected(Value.ofFalse());
            }

            @Override
            Expected one() {
                return new Expected(Value.ofFalse());
            }

            @Override
            Expected sixSixZero() {
                return new Expected(Value.ofFalse());
            }

            @Override
            Expected empty() {
                return new Expected(Value.ofFalse());
            }
        },
        IF_NOT_AVAILABLE {
            @Override
            Expected notAvailable() {
                return new Expected(Value.ofTrue());
            }

            @Override
            Expected unknownRef() {
                return new Expected(Value.ofFalse());
            }

            @Override
            Expected numericalValueExpected() {
                return new Expected(Value.ofFalse());
            }

            @Override
            Expected dividedByZero() {
                return new Expected(Value.ofFalse());
            }

            @Override
            Expected trueBoolean() {
                return new Expected(Value.ofFalse());
            }

            @Override
            Expected falseBoolean() {
                return new Expected(Value.ofFalse());
            }

            @Override
            Expected zero() {
                return new Expected(Value.ofFalse());
            }

            @Override
            Expected one() {
                return new Expected(Value.ofFalse());
            }

            @Override
            Expected sixSixZero() {
                return new Expected(Value.ofFalse());
            }

            @Override
            Expected empty() {
                return new Expected(Value.ofFalse());
            }
        };

        abstract Expected notAvailable();

        abstract Expected unknownRef();

        abstract Expected numericalValueExpected();

        abstract Expected dividedByZero();

        abstract Expected trueBoolean();

        abstract Expected falseBoolean();

        abstract Expected zero();

        abstract Expected one();

        abstract Expected sixSixZero();

        abstract Expected empty();

        public static boolean matchTag(final String tag) {
            for (final Type type : Type.values()) {
                if (type.name().equals(tag)) {
                    return true;
                }
            }
            return false;
        }
    }

    private final Map<Type, List<LogicalComparisonFunction>> FUNCTIONS_BY_TYPE;

    public static List<LogicalComparisonFunction> byType(final Type type) {
        return new LogicalComparisonFunctionProviders().FUNCTIONS_BY_TYPE.get(type);
    }

    public LogicalComparisonFunctionProviders() {
        FUNCTIONS_BY_TYPE = Stream.of(Type.values()).collect(Collectors.toMap(Function.identity(), type ->
                List.of(
                        new LogicalComparisonFunction(new GivenComparison(Value.ofNotAvailable()), type.notAvailable()),
                        new LogicalComparisonFunction(new GivenComparison(Value.ofUnknownRef()), type.unknownRef()),
                        new LogicalComparisonFunction(new GivenComparison(Value.ofNotANumericalValue()), type.numericalValueExpected()),
                        new LogicalComparisonFunction(new GivenComparison(Value.ofDividedByZero()), type.dividedByZero()),
                        new LogicalComparisonFunction(new GivenComparison(Value.ofTrue()), type.trueBoolean()),
                        new LogicalComparisonFunction(new GivenComparison(Value.ofFalse()), type.falseBoolean()),
                        new LogicalComparisonFunction(new GivenComparison(Value.ofZero()), type.zero()),
                        new LogicalComparisonFunction(new GivenComparison(Value.ofOne()), type.one()),
                        new LogicalComparisonFunction(new GivenComparison(ValueTest.SIX_SIX_ZERO), type.sixSixZero()),
                        new LogicalComparisonFunction(new GivenComparison(Value.ofEmpty()), type.empty())
                )));
    }

    public record LogicalComparisonFunction(GivenComparison givenComparison, Expected expected) {
        public LogicalComparisonFunction {
            Objects.requireNonNull(givenComparison);
            Objects.requireNonNull(expected);
        }
    }
}
