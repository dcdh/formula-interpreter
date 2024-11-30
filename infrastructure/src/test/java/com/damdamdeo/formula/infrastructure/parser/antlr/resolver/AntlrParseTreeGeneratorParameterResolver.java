package com.damdamdeo.formula.infrastructure.parser.antlr.resolver;

import com.damdamdeo.formula.domain.Formula;
import com.damdamdeo.formula.domain.usecase.resolver.EvaluateUseCaseTestResolver;
import com.damdamdeo.formula.infrastructure.parser.antlr.AntlrSyntaxError;
import com.damdamdeo.formula.infrastructure.parser.antlr.AntlrSyntaxErrorException;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.util.List;
import java.util.Objects;

public final class AntlrParseTreeGeneratorParameterResolver implements ParameterResolver {

    public static final ValidFormula VALID_FORMULA = new ValidFormula(
            EvaluateUseCaseTestResolver.FORMULA
    );

    public static final InvalidFormula INVALID_FORMULA = new InvalidFormula(
            new Formula("IF("),
            new AntlrSyntaxErrorException(new Formula("IF("), new AntlrSyntaxError(
                    1, 3, "mismatched input '<EOF>' expecting {'GT', 'GTE', 'EQ', 'NEQ', 'LT', 'LTE', 'AND', 'OR', 'IF', 'IFERROR', 'ISNUM', 'ISLOGICAL', 'ISTEXT', 'ISBLANK', 'ISNA', 'ISERROR', 'IFNA', TRUE, FALSE, STRUCTURED_REFERENCE, VALUE, NUMERIC}"
            ))
    );

    @Override
    public boolean supportsParameter(final ParameterContext parameterContext,
                                     final ExtensionContext extensionContext) throws ParameterResolutionException {
        return List.of(ValidFormula.class, InvalidFormula.class).contains(parameterContext.getParameter().getType());
    }

    @Override
    public Object resolveParameter(final ParameterContext parameterContext,
                                   final ExtensionContext extensionContext) throws ParameterResolutionException {
        final Class<?> type = parameterContext.getParameter().getType();
        if (ValidFormula.class.isAssignableFrom(type)) {
            return VALID_FORMULA;
        } else if (InvalidFormula.class.isAssignableFrom(type)) {
            return INVALID_FORMULA;
        } else {
            throw new ParameterResolutionException(type + " is not supported");
        }
    }

    public record ValidFormula(Formula formula) {
        public ValidFormula {
            Objects.requireNonNull(formula);
        }
    }

    public record InvalidFormula(Formula formula, Throwable cause) {
        public InvalidFormula {
            Objects.requireNonNull(formula);
            Objects.requireNonNull(cause);
        }
    }
}
