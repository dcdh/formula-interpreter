package com.damdamdeo.formula.domain.usecase.provider;

import com.damdamdeo.formula.domain.EvaluationResult;
import com.damdamdeo.formula.domain.Formula;
import com.damdamdeo.formula.domain.StructuredReference;
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

// TODO just deux cas de tests ! et encore ici attention parce que je dois avoir qu'un seule parseur !!!
public abstract class EvaluateUseCaseTestResolver implements ParameterResolver {

//    @Target({ElementType.TYPE, ElementType.METHOD})
//    @Retention(RetentionPolicy.RUNTIME)
//    @Tag("VALID")
//    @Test
//    public @interface ValidTest {
//    }
//
//    @Target({ElementType.TYPE, ElementType.METHOD})
//    @Retention(RetentionPolicy.RUNTIME)
//    @Tag("DOMAIN_VALIDATION_EXCEPTION")
//    @Test
//    public @interface DomainValidationExceptionTest {
//    }
//
//    public enum State {
//        VALID, DOMAIN_EVALUATION_EXCEPTION;
//
//        public static boolean matchTag(final String tag) {
//            for (final ValidatorTestResolver.State state : ValidatorTestResolver.State.values()) {
//                if (state.name().equals(tag)) {
//                    return true;
//                }
//            }
//            return false;
//        }
//    }
//
//    /*
//    FCK
//        private final Map<State, Context> CONTEXT_BY_STATE = Map.of(
//            State.VALID, new Context(new SuggestedFormula("IF"), new GivenResponse(Uni.createFrom().item(new SuggestionsCompletion(List.of("("))))),
//            State.DOMAIN_VALIDATION_EXCEPTION, new Context(suggestedFormulaInError(), new GivenResponse(Uni.createFrom().failure(
//                    suggestionException()
//            )))
//    );
//     */
//    private final Map<State, Context> CONTEXT_BY_STATE = Map.of(
//            State.VALID, new Context(),
//            State.DOMAIN_EVALUATION_EXCEPTION, new Context()
//    );
//
//    @Override
//    public boolean supportsParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext) throws ParameterResolutionException {
//        return List.of(Formula.class, StructuredReferences.class, GivenResponse.class)
//                .contains(parameterContext.getParameter().getType());
//    }
//
//    @Override
//    public Object resolveParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext) throws ParameterResolutionException {
//        /*
//        if (SuggestedFormula.class.equals(parameterContext.getParameter().getType())) {
//            return CONTEXT_BY_STATE.get(state).suggestedFormula();
//        } else if (GivenResponse.class.equals(parameterContext.getParameter().getType())) {
//            return CONTEXT_BY_STATE.get(state).givenResponse();
//        } else {
//            throw new IllegalStateException("Should not be here: unsupported parameter type");
//        }
//         */
//        final State state = extensionContext.getTags()
//                .stream()
//                .filter(State::matchTag)
//                .map(State::valueOf)
//                .findFirst()
//                .orElseThrow(() -> new IllegalStateException("Should not be here: unknown tag"));
//        if (Formula.class.isAssignableFrom(parameterContext.getParameter().getType())) {
//            return CONTEXT_BY_STATE.get(state).givenFormula();
//        } else if (StructuredReferences.class.isAssignableFrom(parameterContext.getParameter().getType())) {
//            return CONTEXT_BY_STATE.get(state).structuredReferences();
//        } else if (GivenResponse.class.isAssignableFrom(parameterContext.getParameter().getType())) {
//            return CONTEXT_BY_STATE.get(state).givenResponse();
//        } else {
//            throw new IllegalStateException("Should not be here: unsupported parameter type");
//        }
//    }
//
//    private record Context(Formula givenFormula,
//                           StructuredReferences structuredReferences,
//                           GivenResponse givenResponse) {
//        private Context {
//            Objects.requireNonNull(givenFormula);
//            Objects.requireNonNull(structuredReferences);
//            Objects.requireNonNull(givenResponse);
//        }
//    }
//
//    public record GivenResponse(Uni<EvaluationResult> response) {
//        public GivenResponse {
//            Objects.requireNonNull(response);
//        }
//    }

}

