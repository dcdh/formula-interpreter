package com.damdamdeo.formula.infrastructure.usecase;

import com.damdamdeo.formula.domain.SuggestedFormula;
import com.damdamdeo.formula.domain.usecase.SuggestUseCase;
import com.damdamdeo.formula.domain.usecase.resolver.SuggestCompletionParameterResolver;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.List;

@QuarkusTest
class InfraSuggestUseCaseTest {

    @Inject
    SuggestUseCase suggestUseCase;

    @Test
    void suggest() {
        throw new RuntimeException("TODO");
    }


    public static class InfraSuggestCompletionParameterResolver extends SuggestCompletionParameterResolver {
        @Override
        protected List<GivenFailing> givenFailings() {
            return List.of(
                    new GivenFailing(
                            new SuggestedFormula("IF"),
                            new IllegalStateException("Something went wrong"))
            );
        }
    }

}
