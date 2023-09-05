package com.damdamdeo.formula.domain.usecase;

import com.damdamdeo.formula.domain.spi.SuggestCompletion;
import com.damdamdeo.formula.domain.SuggestionsCompletion;
import io.smallrye.mutiny.Uni;

import java.util.Objects;

public final class SuggestUseCase implements UseCase<SuggestionsCompletion, SuggestCommand> {

    private final SuggestCompletion autoSuggestCompletion;

    public SuggestUseCase(final SuggestCompletion suggestCompletion) {
        this.autoSuggestCompletion = Objects.requireNonNull(suggestCompletion);
    }

    @Override
    public Uni<SuggestionsCompletion> execute(final SuggestCommand command) {
        return autoSuggestCompletion.suggest(command.suggestedFormula());
    }
}
