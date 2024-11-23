package com.damdamdeo.formula.domain.usecase;

import com.damdamdeo.formula.domain.SuggestionException;
import com.damdamdeo.formula.domain.SuggestionsCompletion;
import com.damdamdeo.formula.domain.spi.SuggestCompletion;
import io.smallrye.mutiny.Uni;

import java.util.Objects;

public final class SuggestUseCase implements UseCase<SuggestionsCompletion, SuggestCommand> {

    private final SuggestCompletion autoSuggestCompletion;

    public SuggestUseCase(final SuggestCompletion suggestCompletion) {
        this.autoSuggestCompletion = Objects.requireNonNull(suggestCompletion);
    }

    @Override
    public Uni<SuggestionsCompletion> execute(final SuggestCommand command) {
        return autoSuggestCompletion.suggest(command.suggestedFormula())
                .onFailure()
                .transform(SuggestionException::new);
    }
}
