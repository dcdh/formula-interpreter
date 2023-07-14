package com.damdamdeo.formula.infrastructure.antlr;

import com.damdamdeo.formula.domain.SuggestedFormula;
import com.damdamdeo.formula.domain.SuggestionsCompletion;
import com.damdamdeo.formula.infrastructure.antlr.autosuggest.AutoSuggestionExecutionTimedOutException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AntlrSuggestCompletionTest extends AbstractExpressionTest {

    @ParameterizedTest
    @MethodSource("provideSuggestions")
    public void shouldReturnExpectedSuggestion(final SuggestedFormula givenFormula, final SuggestionsCompletion expectedSuggestionsCompletion) {
        final AntlrSuggestCompletion antlrSuggest = new AntlrSuggestCompletion();
        final SuggestionsCompletion suggestionsCompletion = antlrSuggest.suggest(givenFormula);
        assertThat(suggestionsCompletion).isEqualTo(expectedSuggestionsCompletion);
    }

    @Test
    public void shouldThrowExceptionWhenTheTimeProcessingIsTooLong() {
        final AntlrSuggestCompletion antlrSuggest = new AntlrSuggestCompletion();
        assertThatThrownBy(() -> antlrSuggest.suggest(new SuggestedFormula("""
                IF(EQ([@[Sales Person]],"Joe"),MUL(MUL([@[Sales Amount]],[@[% Commission]]),2),MUL([@[Sales Amount]],""")))
                .isInstanceOf(AutoSuggestionExecutionTimedOutException.class);
    }

    private static Stream<Arguments> provideSuggestions() {
        return Stream.of(
                Arguments.of(new SuggestedFormula("IF"), new SuggestionsCompletion(List.of("("))),
                Arguments.of(new SuggestedFormula("IF(E"), new SuggestionsCompletion(List.of("Q"))),
                Arguments.of(new SuggestedFormula("IF(EQ([@[Sales Person]],\"Joe\"),MUL(MUL([@"), new SuggestionsCompletion(List.of("[]]")))
        );
    }

}
