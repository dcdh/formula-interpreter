package com.damdamdeo.formula.domain.usecase.provider;


import com.damdamdeo.formula.domain.SuggestedFormula;
import com.damdamdeo.formula.domain.SuggestionException;
import com.damdamdeo.formula.domain.SuggestionsCompletion;
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
import java.util.List;
import java.util.Map;
import java.util.Objects;

public abstract class SuggestCompletionTestResolver implements ParameterResolver {

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("VALID")
    @Test
    public @interface ValidTest {
    }

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("DOMAIN_VALIDATION_EXCEPTION")
    @Test
    public @interface DomainValidationExceptionTest {
    }

    public enum State {
        VALID, DOMAIN_VALIDATION_EXCEPTION;

        public static boolean matchTag(final String tag) {
            for (final ValidatorTestResolver.State state : ValidatorTestResolver.State.values()) {
                if (state.name().equals(tag)) {
                    return true;
                }
            }
            return false;
        }
    }

    private final Map<State, Context> CONTEXT_BY_STATE = Map.of(
            State.VALID, new Context(new SuggestedFormula("IF"), new GivenResponse(Uni.createFrom().item(new SuggestionsCompletion(List.of("("))))),
            State.DOMAIN_VALIDATION_EXCEPTION, new Context(suggestedFormulaInError(), new GivenResponse(Uni.createFrom().failure(
                    suggestionException()
            )))
    );

    @Override
    public boolean supportsParameter(final ParameterContext parameterContext,
                                     final ExtensionContext extensionContext) throws ParameterResolutionException {
        return List.of(SuggestedFormula.class, GivenResponse.class)
                .contains(parameterContext.getParameter().getType());
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

        if (SuggestedFormula.class.equals(parameterContext.getParameter().getType())) {
            return CONTEXT_BY_STATE.get(state).suggestedFormula();
        } else if (GivenResponse.class.equals(parameterContext.getParameter().getType())) {
            return CONTEXT_BY_STATE.get(state).givenResponse();
        } else {
            throw new IllegalStateException("Should not be here: unsupported parameter type");
        }
    }

    protected abstract SuggestedFormula suggestedFormulaInError();

    protected abstract SuggestionException suggestionException();

    private record Context(SuggestedFormula suggestedFormula, GivenResponse givenResponse) {
        private Context {
            Objects.requireNonNull(suggestedFormula);
            Objects.requireNonNull(givenResponse);
        }
    }

    public record GivenResponse(Uni<SuggestionsCompletion> response) {
        public GivenResponse {
            Objects.requireNonNull(response);
        }
    }
}
