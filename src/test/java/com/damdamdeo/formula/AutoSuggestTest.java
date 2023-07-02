package com.damdamdeo.formula;

import com.damdamdeo.formula.autosuggest.AutoSuggestionProcessingTimedOutException;
import org.antlr.runtime.RecognitionException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AutoSuggestTest extends AbstractExpressionTest {

    @ParameterizedTest
    @MethodSource("provideSuggestions")
    public void shouldReturnExpectedSuggestion(final String givenFormula, final List<String> expectedSuggestions) {
        final AutoSuggest autoSuggest = new AutoSuggest();
        final List<String> suggestions = autoSuggest.execute(new Formula(givenFormula));
        assertThat(suggestions).containsAll(expectedSuggestions);
    }

    @Test
    public void shouldThrowExceptionWhenTheTimeProcessingIsTooLong() {
        final AutoSuggest autoSuggest = new AutoSuggest();
        assertThatThrownBy(() -> autoSuggest.execute(new Formula("""
                IF(EQ([@[Sales Person]],"Joe"),MUL(MUL([@[Sales Amount]],[@[% Commission]]),2),MUL([@[Sales Amount]],""")))
                .isInstanceOf(AutoSuggestionProcessingTimedOutException.class);
    }

    private static Stream<Arguments> provideSuggestions() {
        return Stream.of(
                Arguments.of("IF", List.of("(")),
                Arguments.of("IF(E", List.of("Q")),
                Arguments.of("IF(EQ([@[Sales Person]],\"Joe\"),MUL(MUL([@", List.of("[]]"))
        );
    }

}
