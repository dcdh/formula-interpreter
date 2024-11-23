package com.damdamdeo.formula.domain.usecase;

import com.damdamdeo.formula.domain.SuggestedFormula;
import com.damdamdeo.formula.domain.SuggestionException;
import com.damdamdeo.formula.domain.SuggestionsCompletion;
import com.damdamdeo.formula.domain.spi.SuggestCompletion;
import com.damdamdeo.formula.domain.usecase.resolver.SuggestCompletionParameterResolver;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SuggestUseCaseTest.DomainSuggestCompletionParameterResolver.class)
class SuggestUseCaseTest {

    @Mock
    private SuggestCompletion suggestCompletion;

    private SuggestUseCase suggestUseCase;

    @BeforeEach
    void setup() {
        suggestUseCase = new SuggestUseCase(suggestCompletion);
    }

    @Test
    void shouldSuggest(final SuggestCompletionParameterResolver.GivenHappyPath givenHappyPath) {
        // Given
        final SuggestedFormula givenSuggestedFormula = givenHappyPath.suggestedFormula();
        doReturn(givenHappyPath.toUni()).when(suggestCompletion).suggest(givenSuggestedFormula);

        // When
        final Uni<SuggestionsCompletion> executed = suggestUseCase.execute(new SuggestCommand(givenSuggestedFormula));

        // Then
        final UniAssertSubscriber<SuggestionsCompletion> subscriber = executed
                .subscribe().withSubscriber(UniAssertSubscriber.create());

        subscriber.assertCompleted().assertItem(new SuggestionsCompletion(List.of("(")));
    }

    @Test
    @Tag("IllegalStateException")
    void shouldFailWhenAnExceptionIsThrown(final SuggestCompletionParameterResolver.GivenFailing givenFailing) {
        // Given
        final SuggestedFormula givenSuggestedFormula = givenFailing.suggestedFormula();
        doReturn(givenFailing.toUni()).when(suggestCompletion).suggest(givenSuggestedFormula);

        // When
        final Uni<SuggestionsCompletion> executed = suggestUseCase.execute(new SuggestCommand(givenSuggestedFormula));

        // Then
        final UniAssertSubscriber<SuggestionsCompletion> subscriber = executed.subscribe()
                .withSubscriber(UniAssertSubscriber.create());

        assertAll(
                subscriber::assertFailed,
                () -> assertThat(subscriber.getFailure()).isInstanceOf(SuggestionException.class)
                        .hasRootCauseInstanceOf(IllegalStateException.class)
                        .hasRootCauseMessage("Something went wrong")
        );
    }

    static class DomainSuggestCompletionParameterResolver extends SuggestCompletionParameterResolver {
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