package com.damdamdeo.formula.domain.evaluation.provider;

import com.damdamdeo.formula.domain.Evaluated;
import com.damdamdeo.formula.domain.EvaluationProcessedIn;
import com.damdamdeo.formula.domain.Formula;
import com.damdamdeo.formula.domain.evaluation.Expression;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.stream.Stream;

@Deprecated
public final class EvaluationTestResolver implements ParameterResolver {

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("COMPOUND_ADD_MUL")
    @Test
    public @interface CompoundAddMulTest {
    }

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("ARGUMENT")
    @Test
    public @interface ArgumentTest {
    }

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("LOGICAL_AND")
    @Test
    public @interface LogicalAndTest {
    }

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("LOGICAL_OR")
    @Test
    public @interface LogicalORTest {
    }

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("COMPARISON_EQ")
    @Test
    public @interface ComparisonEQTest {
    }

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("COMPARISON_NEQ")
    @Test
    public @interface ComparisonNEQTest {
    }

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("COMPARISON_GTE")
    @Test
    public @interface ComparisonGTETest {
    }

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("COMPARISON_LTE")
    @Test
    public @interface ComparisonLTETest {
    }

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("COMPARISON_GT")
    @Test
    public @interface ComparisonGTTest {
    }

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("COMPARISON_LT")
    @Test
    public @interface ComparisonLTTest {
    }

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("COMPARISON_IF_LEFT_WIN")
    @Test
    public @interface ComparisonIfLeftWinTest {
    }

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("COMPARISON_IF_RIGHT_WIN")
    @Test
    public @interface ComparisonIfRightWinTest {
    }

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("COMPARISON_IF_ERROR_LEFT_WIN")
    @Test
    public @interface ComparisonIfErrorLeftWinTest {
    }

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("COMPARISON_IF_ERROR_RIGHT_WIN")
    @Test
    public @interface ComparisonIfErrorRightWinTest {
    }

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("COMPARISON_IF_NA_LEFT_WIN")
    @Test
    public @interface ComparisonIfNaLeftWinTest {
    }

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("COMPARISON_IF_NA_RIGHT_WIN")
    @Test
    public @interface ComparisonIfNaRightWinTest {
    }

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("STATE_IS_NA")
    @Test
    public @interface StateIsNaTest {
    }

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("STATE_IS_ERROR")
    @Test
    public @interface StateIsErrorTest {
    }

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("STATE_IS_NUMERIC")
    @Test
    public @interface StateIsNumericTest {
    }

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("STATE_IS_TEXT")
    @Test
    public @interface StateIsTextTest {
    }

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("STATE_IS_BLANK")
    @Test
    public @interface StateIsBlankTest {
    }

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("STATE_IS_LOGICAL")
    @Test
    public @interface StateIsLogicalTest {
    }

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("BIG_ONE_IS_JOE")
    @Test
    public @interface BigOneIsJoeTest {
    }

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("BIG_ONE_IS_JOE")
    @Test
    public @interface BigOneIsNotJoeTest {
    }

    @Override
    public boolean supportsParameter(final ParameterContext parameterContext,
                                     final ExtensionContext extensionContext) throws ParameterResolutionException {
        return Stream.of(
                StructuredReferences.class,
                Formula.class,
                Expression.class,
                Evaluated.class,
                IntermediateResults.class,
                EvaluationProcessedIn.class).anyMatch(clazz -> clazz.isAssignableFrom(parameterContext.getParameter().getType()));
    }

    @Override
    public Object resolveParameter(final ParameterContext parameterContext,
                                   final ExtensionContext extensionContext) throws ParameterResolutionException {
        final Evaluation.Kind kind = extensionContext.getTags()
                .stream()
                .filter(Evaluation.Kind::matchTag)
                .map(Evaluation.Kind::valueOf)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Should not be here: unknown tag"));
        if (StructuredReferences.class.isAssignableFrom(parameterContext.getParameter().getType())) {
            return Evaluation.EVALUATIONS.get(kind).structuredReferences();
        } else if (Formula.class.isAssignableFrom(parameterContext.getParameter().getType())) {
            return Evaluation.EVALUATIONS.get(kind).formula();
        } else if (Expression.class.isAssignableFrom(parameterContext.getParameter().getType())) {
            return Evaluation.EVALUATIONS.get(kind).expression();
        } else if (Evaluated.class.isAssignableFrom(parameterContext.getParameter().getType())) {
            return Evaluation.EVALUATIONS.get(kind).evaluated();
        } else if (IntermediateResults.class.isAssignableFrom(parameterContext.getParameter().getType())) {
            return Evaluation.EVALUATIONS.get(kind).intermediateResults();
        } else if (EvaluationProcessedIn.class.isAssignableFrom(parameterContext.getParameter().getType())) {
            return Evaluation.EVALUATIONS.get(kind).evaluationProcessedIn();
        } else {
            throw new IllegalStateException("Should not be here: unsupported parameter type");
        }
    }

}
