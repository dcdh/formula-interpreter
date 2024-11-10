package com.damdamdeo.formula.domain.usecase.provider;

import com.damdamdeo.formula.domain.Formula;
import com.damdamdeo.formula.domain.SyntaxError;
import com.damdamdeo.formula.domain.ValidationException;
import io.smallrye.mutiny.Uni;
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
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public abstract class ValidatorTestResolver<R extends SyntaxError> implements ParameterResolver {

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("VALID")
    @Test
    public @interface ValidTest {
    }

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("INVALID")
    @Test
    public @interface InValidTest {
    }

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("DOMAIN_VALIDATION_EXCEPTION")
    @Test
    public @interface DomainValidationExceptionTest {
    }

    public enum State {
        VALID, INVALID, DOMAIN_VALIDATION_EXCEPTION;

        public static boolean matchTag(final String tag) {
            for (final State state : State.values()) {
                if (state.name().equals(tag)) {
                    return true;
                }
            }
            return false;
        }
    }

    private final Map<State, Context<R>> CONTEXT_BY_STATE = Map.of(
            State.VALID, new Context<>(new Formula("true"), new GivenResponse<>(Uni.createFrom().item(Optional.empty()))),
            State.INVALID, new Context<>(new Formula("IF"), new GivenResponse<>(Uni.createFrom().item(Optional.of(invalid())))),
            State.DOMAIN_VALIDATION_EXCEPTION, new Context<>(new Formula("BOOM"), new GivenResponse<>(Uni.createFrom().failure(validationException())))
    );

    @Override
    public boolean supportsParameter(final ParameterContext parameterContext,
                                     final ExtensionContext extensionContext) throws ParameterResolutionException {
        return Formula.class.equals(parameterContext.getParameter().getType())
                || GivenResponse.class.isAssignableFrom(parameterContext.getParameter().getType());
    }

    @Override
    public Object resolveParameter(final ParameterContext parameterContext,
                                   final ExtensionContext extensionContext) throws ParameterResolutionException {
        final State state = extensionContext.getTags()
                .stream()
                .filter(State::matchTag)
                .map(State::valueOf)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Should not be here: unknown tag"));

        if (Formula.class.equals(parameterContext.getParameter().getType())) {
            return CONTEXT_BY_STATE.get(state).givenFormula();
        } else if (GivenResponse.class.isAssignableFrom(parameterContext.getParameter().getType())) {
            return CONTEXT_BY_STATE.get(state).givenResponse();
        } else {
            throw new IllegalStateException("Should not be here: unsupported parameter type");
        }
    }

    protected abstract R invalid();

    protected abstract ValidationException validationException();

    private record Context<R>(Formula givenFormula, GivenResponse<R> givenResponse) {
        private Context {
            Objects.requireNonNull(givenFormula);
            Objects.requireNonNull(givenResponse);
        }
    }

    public record GivenResponse<R>(Uni<Optional<R>> response) {
        public GivenResponse {
            Objects.requireNonNull(response);
        }
    }
}
