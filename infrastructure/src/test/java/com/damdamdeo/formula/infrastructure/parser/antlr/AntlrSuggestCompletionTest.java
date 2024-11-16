package com.damdamdeo.formula.infrastructure.parser.antlr;

import com.damdamdeo.formula.domain.SuggestedFormula;
import com.damdamdeo.formula.domain.SuggestionException;
import com.damdamdeo.formula.domain.SuggestionsCompletion;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class AntlrSuggestCompletionTest {

    private AntlrSuggestCompletion antlrSuggest;

    @BeforeEach
    public void setup() {
        antlrSuggest = new AntlrSuggestCompletion();
    }

    @ParameterizedTest
    @MethodSource("provideSuggestions")
    public void shouldReturnExpectedSuggestion(final SuggestedFormula givenFormula, final SuggestionsCompletion expectedSuggestionsCompletion) {
        // Given

        // When
        final Uni<SuggestionsCompletion> suggestionsCompletion = antlrSuggest.suggest(givenFormula);

        // Then
        assertOnSuggestionsCompletionResultReceived(suggestionsCompletion, suggestionsCompletionToAssert ->
                assertThat(suggestionsCompletionToAssert).isEqualTo(expectedSuggestionsCompletion)
        );
    }

    @Test
    public void shouldThrowExceptionWhenTheTimeProcessingIsTooLong() {
        // Given

        // When
        final Uni<SuggestionsCompletion> suggestionsCompletion = antlrSuggest.suggest(new SuggestedFormula("""
                IF(EQ([@[Sales Person]],"Joe"),MUL(MUL([@[Sales Amount]],[@[% Commission]]),2),MUL([@[Sales Amount]],"""));

        // Then
        assertOnFailure(suggestionsCompletion, throwableToAssert ->
                assertThat(throwableToAssert)
                        .isInstanceOf(SuggestionException.class)
                        .hasCauseInstanceOf(AntlrAutoSuggestionExecutionTimedOutException.class)
        );
    }

    private static Stream<Arguments> provideSuggestions() {
        return Stream.of(
                Arguments.of(new SuggestedFormula("IF"), new SuggestionsCompletion(List.of("("))),
                Arguments.of(new SuggestedFormula("IF(E"), new SuggestionsCompletion(List.of("Q"))),
                Arguments.of(new SuggestedFormula("IF(EQ([@[Sales Person]],\"Joe\"),MUL(MUL([@"), new SuggestionsCompletion(List.of("[]]")))
        );
    }

    protected void assertOnSuggestionsCompletionResultReceived(final Uni<SuggestionsCompletion> suggestionsCompletionResult,
                                                               final Consumer<SuggestionsCompletion> assertionLogic) {
        final UniAssertSubscriber<SuggestionsCompletion> subscriber = suggestionsCompletionResult
                .subscribe()
                .withSubscriber(UniAssertSubscriber.create());
        final SuggestionsCompletion suggestionsCompletionResultToAssert = subscriber.awaitItem().getItem();
        assertionLogic.accept(suggestionsCompletionResultToAssert);
    }

    protected void assertOnFailure(final Uni<SuggestionsCompletion> suggestionsCompletionResult,
                                   final Consumer<Throwable> assertionLogic) {
        final UniAssertSubscriber<SuggestionsCompletion> subscriber = suggestionsCompletionResult
                .subscribe()
                .withSubscriber(UniAssertSubscriber.create());
        subscriber.awaitFailure(assertionLogic);
    }
}
