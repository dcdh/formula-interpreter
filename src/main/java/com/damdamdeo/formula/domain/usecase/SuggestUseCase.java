package com.damdamdeo.formula.domain.usecase;

import com.damdamdeo.formula.domain.SuggestCompletion;
import com.damdamdeo.formula.domain.SuggestionException;
import com.damdamdeo.formula.domain.SuggestionsCompletion;
import com.damdamdeo.formula.domain.UseCase;

import java.util.Objects;

public final class SuggestUseCase implements UseCase<SuggestionsCompletion, SuggestCommand> {

    private final SuggestCompletion autoSuggestCompletion;

    public SuggestUseCase(final SuggestCompletion suggestCompletion) {
        this.autoSuggestCompletion = Objects.requireNonNull(suggestCompletion);
    }

    @Override
    public SuggestionsCompletion execute(final SuggestCommand command) throws SuggestionException {
        return autoSuggestCompletion.suggest(command.suggestedFormula());
    }
}
