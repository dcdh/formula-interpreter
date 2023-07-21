package com.damdamdeo.formula.domain;

public interface SuggestCompletion {
    SuggestionsCompletion suggest(SuggestedFormula suggestedFormula) throws SuggestionException;

}
