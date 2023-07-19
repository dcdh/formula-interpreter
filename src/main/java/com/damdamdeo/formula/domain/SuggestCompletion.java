package com.damdamdeo.formula.domain;

import com.damdamdeo.formula.infrastructure.antlr.autosuggest.AutoSuggestUnavailableException;
import com.damdamdeo.formula.infrastructure.antlr.autosuggest.AutoSuggestionExecutionException;
import com.damdamdeo.formula.infrastructure.antlr.autosuggest.AutoSuggestionExecutionTimedOutException;

public interface SuggestCompletion {

    SuggestionsCompletion suggest(SuggestedFormula suggestedFormula)
            throws AutoSuggestUnavailableException, AutoSuggestionExecutionException, AutoSuggestionExecutionTimedOutException;

}
