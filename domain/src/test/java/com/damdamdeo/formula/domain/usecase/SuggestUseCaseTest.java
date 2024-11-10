package com.damdamdeo.formula.domain.usecase;

import com.damdamdeo.formula.domain.SuggestedFormula;
import com.damdamdeo.formula.domain.SuggestionException;
import com.damdamdeo.formula.domain.SuggestionsCompletion;
import com.damdamdeo.formula.domain.spi.SuggestCompletion;
import com.damdamdeo.formula.domain.usecase.provider.SuggestCompletionTestResolver;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

@ExtendWith(SuggestUseCaseTest.DomainSuggestCompletionTestResolver.class)
class SuggestUseCaseTest {

    private SuggestCompletion suggestCompletion;

    private SuggestUseCase suggestUseCase;

    @BeforeEach
    void setup() {
        this.suggestCompletion = mock(SuggestCompletion.class);
        this.suggestUseCase = new SuggestUseCase(suggestCompletion);
    }

    @SuggestCompletionTestResolver.ValidTest
    void shouldSuggest(final SuggestedFormula suggestedFormula,
                       final SuggestCompletionTestResolver.GivenResponse givenResponse) {
        // Given
        doReturn(givenResponse.response()).when(suggestCompletion).suggest(suggestedFormula);

        // When
        final Uni<SuggestionsCompletion> executed = suggestUseCase.execute(new SuggestCommand(suggestedFormula));

        // Then
        final UniAssertSubscriber<SuggestionsCompletion> subscriber = executed
                .subscribe().withSubscriber(UniAssertSubscriber.create());

        assertAll(
                () -> subscriber.assertCompleted().assertItem(new SuggestionsCompletion(List.of("("))),
                () -> verify(suggestCompletion, times(1)).suggest(any())
        );
    }

    @SuggestCompletionTestResolver.DomainValidationExceptionTest
    void shouldFailWhenAnExceptionIsThrown(final SuggestedFormula suggestedFormula,
                                           final SuggestCompletionTestResolver.GivenResponse givenResponse) {
        // Given
        doReturn(givenResponse.response()).when(suggestCompletion).suggest(suggestedFormula);

        // When
        final Uni<SuggestionsCompletion> executed = suggestUseCase.execute(new SuggestCommand(suggestedFormula));

        // Then
        final UniAssertSubscriber<SuggestionsCompletion> subscriber = executed
                .subscribe().withSubscriber(UniAssertSubscriber.create());

        assertAll(
                subscriber::assertFailed,
                () -> assertThat(subscriber.getFailure()).isInstanceOf(SuggestionException.class)
                        .hasRootCauseInstanceOf(IllegalStateException.class)
                        .hasRootCauseMessage("Something went wrong"),
                () -> verify(suggestCompletion, times(1)).suggest(any())
        );
    }

    static class DomainSuggestCompletionTestResolver extends SuggestCompletionTestResolver {

        @Override
        protected SuggestedFormula suggestedFormulaInError() {
            return new SuggestedFormula("IF");
        }

        @Override
        protected SuggestionException suggestionException() {
            return new SuggestionException(new IllegalStateException("Something went wrong"));
        }
    }
}
