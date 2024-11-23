package com.damdamdeo.formula.domain.usecase.resolver;

import com.damdamdeo.formula.domain.SuggestedFormula;
import com.damdamdeo.formula.domain.SuggestionsCompletion;
import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public abstract class SuggestCompletionParameterResolver implements ParameterResolver {

    @Override
    public boolean supportsParameter(final ParameterContext parameterContext,
                                     final ExtensionContext extensionContext) throws ParameterResolutionException {
        return List.of(GivenHappyPath.class, GivenFailing.class)
                .contains(parameterContext.getParameter().getType());
    }

    @Override
    public Object resolveParameter(final ParameterContext parameterContext,
                                   final ExtensionContext extensionContext) throws ParameterResolutionException {
        final Class<?> type = parameterContext.getParameter().getType();
        if (GivenHappyPath.class.isAssignableFrom(type)) {
            return new GivenHappyPath(new SuggestedFormula("IF"), new SuggestionsCompletion(List.of("(")));
        } else if (GivenFailing.class.isAssignableFrom(type)) {
            return givenFailings().stream()
                    .filter(failingResponse -> failingResponse.matchTag(extensionContext.getTags()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("No tags match one of the givenFailingResponse"));
        } else {
            throw new ParameterResolutionException(parameterContext.getParameter().getType() + " is not supported");
        }
    }

    protected abstract List<GivenFailing> givenFailings();

    public record GivenHappyPath(SuggestedFormula suggestedFormula, SuggestionsCompletion suggestionsCompletion) {
        public GivenHappyPath {
            Objects.requireNonNull(suggestedFormula);
            Objects.requireNonNull(suggestionsCompletion);
        }

        public Uni<SuggestionsCompletion> toUni() {
            return Uni.createFrom().item(suggestionsCompletion);
        }
    }

    public record GivenFailing(SuggestedFormula suggestedFormula, Throwable cause) {
        public GivenFailing {
            Objects.requireNonNull(suggestedFormula);
            Objects.requireNonNull(cause);
        }

        public Uni<Exception> toUni() {
            return Uni.createFrom().failure(cause);
        }

        public boolean matchTag(final Set<String> tags) {
            Objects.requireNonNull(tags);
            return tags.contains(cause.getClass().getSimpleName());
        }
    }
}