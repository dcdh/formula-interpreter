package com.damdamdeo.formula.domain;

import io.smallrye.mutiny.Uni;

public interface SuggestCompletion {
    Uni<SuggestionsCompletion> suggest(SuggestedFormula suggestedFormula) throws SuggestionException;

}
