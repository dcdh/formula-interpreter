package com.damdamdeo.formula.domain.provider;

import com.damdamdeo.formula.domain.Value;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class StateFunctionProviders {

    public enum Type {
        IS_BLANK {
            @Override
            Expected notAvailable() {
                return new Expected(Value.ofFalse());
            }

            @Override
            Expected unknownRef() {
                return new Expected(Value.ofFalse());
            }

            @Override
            Expected notANumericalValue() {
                return new Expected(Value.ofFalse());
            }

            @Override
            Expected divByZero() {
                return new Expected(Value.ofFalse());
            }

            @Override
            Expected notALogicalValue() {
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
            Expected one() {
                return new Expected(Value.ofFalse());
            }

            @Override
            Expected zero() {
                return new Expected(Value.ofFalse());
            }

            @Override
            Expected sixSixZero() {
                return new Expected(Value.ofFalse());
            }

            @Override
            Expected azerty() {
                return new Expected(Value.ofFalse());
            }

            @Override
            Expected empty() {
                return new Expected(Value.ofTrue());
            }
        },
        IS_ERROR {
            @Override
            Expected notAvailable() {
                return new Expected(Value.ofTrue());
            }

            @Override
            Expected unknownRef() {
                return new Expected(Value.ofTrue());
            }

            @Override
            Expected notANumericalValue() {
                return new Expected(Value.ofTrue());
            }

            @Override
            Expected divByZero() {
                return new Expected(Value.ofTrue());
            }

            @Override
            Expected notALogicalValue() {
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
            Expected one() {
                return new Expected(Value.ofFalse());
            }

            @Override
            Expected zero() {
                return new Expected(Value.ofFalse());
            }

            @Override
            Expected sixSixZero() {
                return new Expected(Value.ofFalse());
            }

            @Override
            Expected azerty() {
                return new Expected(Value.ofFalse());
            }

            @Override
            Expected empty() {
                return new Expected(Value.ofFalse());
            }
        },
        IS_LOGICAL {
            @Override
            Expected notAvailable() {
                return new Expected(Value.ofFalse());
            }

            @Override
            Expected unknownRef() {
                return new Expected(Value.ofFalse());
            }

            @Override
            Expected notANumericalValue() {
                return new Expected(Value.ofFalse());
            }

            @Override
            Expected divByZero() {
                return new Expected(Value.ofFalse());
            }

            @Override
            Expected notALogicalValue() {
                return new Expected(Value.ofFalse());
            }

            @Override
            Expected trueBoolean() {
                return new Expected(Value.ofTrue());
            }

            @Override
            Expected falseBoolean() {
                return new Expected(Value.ofTrue());
            }

            @Override
            Expected one() {
                return new Expected(Value.ofTrue());
            }

            @Override
            Expected zero() {
                return new Expected(Value.ofTrue());
            }

            @Override
            Expected sixSixZero() {
                return new Expected(Value.ofFalse());
            }

            @Override
            Expected azerty() {
                return new Expected(Value.ofFalse());
            }

            @Override
            Expected empty() {
                return new Expected(Value.ofFalse());
            }
        },
        IS_NOT_AVAILABLE {
            @Override
            Expected notAvailable() {
                return new Expected(Value.ofTrue());
            }

            @Override
            Expected unknownRef() {
                return new Expected(Value.ofFalse());
            }

            @Override
            Expected notANumericalValue() {
                return new Expected(Value.ofFalse());
            }

            @Override
            Expected divByZero() {
                return new Expected(Value.ofFalse());
            }

            @Override
            Expected notALogicalValue() {
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
            Expected one() {
                return new Expected(Value.ofFalse());
            }

            @Override
            Expected zero() {
                return new Expected(Value.ofFalse());
            }

            @Override
            Expected sixSixZero() {
                return new Expected(Value.ofFalse());
            }

            @Override
            Expected azerty() {
                return new Expected(Value.ofFalse());
            }

            @Override
            Expected empty() {
                return new Expected(Value.ofFalse());
            }
        },
        IS_NUMERIC {
            @Override
            Expected notAvailable() {
                return new Expected(Value.ofFalse());
            }

            @Override
            Expected unknownRef() {
                return new Expected(Value.ofFalse());
            }

            @Override
            Expected notANumericalValue() {
                return new Expected(Value.ofFalse());
            }

            @Override
            Expected divByZero() {
                return new Expected(Value.ofFalse());
            }

            @Override
            Expected notALogicalValue() {
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
            Expected one() {
                return new Expected(Value.ofTrue());
            }

            @Override
            Expected zero() {
                return new Expected(Value.ofTrue());
            }

            @Override
            Expected sixSixZero() {
                return new Expected(Value.ofTrue());
            }

            @Override
            Expected azerty() {
                return new Expected(Value.ofFalse());
            }

            @Override
            Expected empty() {
                return new Expected(Value.ofFalse());
            }
        },
        IS_TEXT {
            @Override
            Expected notAvailable() {
                return new Expected(Value.ofFalse());
            }

            @Override
            Expected unknownRef() {
                return new Expected(Value.ofFalse());
            }

            @Override
            Expected notANumericalValue() {
                return new Expected(Value.ofFalse());
            }

            @Override
            Expected divByZero() {
                return new Expected(Value.ofFalse());
            }

            @Override
            Expected notALogicalValue() {
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
            Expected one() {
                return new Expected(Value.ofFalse());
            }

            @Override
            Expected zero() {
                return new Expected(Value.ofFalse());
            }

            @Override
            Expected sixSixZero() {
                return new Expected(Value.ofFalse());
            }

            @Override
            Expected azerty() {
                return new Expected(Value.ofTrue());
            }

            @Override
            Expected empty() {
                return new Expected(Value.ofTrue());
            }
        };

        abstract Expected notAvailable();

        abstract Expected unknownRef();

        abstract Expected notANumericalValue();

        abstract Expected divByZero();

        abstract Expected notALogicalValue();

        abstract Expected trueBoolean();

        abstract Expected falseBoolean();

        abstract Expected one();

        abstract Expected zero();

        abstract Expected sixSixZero();

        abstract Expected azerty();

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

    private final Map<Type, List<StateFunction>> FUNCTIONS_BY_TYPE;

    public static List<StateFunction> byType(final Type type) {
        return new StateFunctionProviders().FUNCTIONS_BY_TYPE.get(type);
    }

    public StateFunctionProviders() {
        FUNCTIONS_BY_TYPE = Stream.of(Type.values()).collect(Collectors.toMap(Function.identity(), type ->
                List.of(
                        new StateFunction(new GivenArgument(Value.ofNotAvailable()), type.notAvailable()),
                        new StateFunction(new GivenArgument(Value.ofUnknownRef()), type.unknownRef()),
                        new StateFunction(new GivenArgument(Value.ofNotANumericalValue()), type.notANumericalValue()),
                        new StateFunction(new GivenArgument(Value.ofDividedByZero()), type.divByZero()),
                        new StateFunction(new GivenArgument(Value.ofNotALogicalValue()), type.notALogicalValue()),
                        new StateFunction(new GivenArgument(Value.ofTrue()), type.trueBoolean()),
                        new StateFunction(new GivenArgument(Value.ofFalse()), type.falseBoolean()),
                        new StateFunction(new GivenArgument(Value.ofZero()), type.zero()),
                        new StateFunction(new GivenArgument(Value.ofOne()), type.one()),
                        new StateFunction(new GivenArgument(Value.ofNumeric("660")), type.sixSixZero()),
                        new StateFunction(new GivenArgument(Value.ofText("azerty")), type.azerty()),
                        new StateFunction(new GivenArgument(Value.ofEmpty()), type.empty())
                )));
    }

    public record StateFunction(GivenArgument givenArgument, Expected expected) {
        public StateFunction {
            Objects.requireNonNull(givenArgument);
            Objects.requireNonNull(expected);
        }
    }

}
