package com.damdamdeo.formula.domain.usecase;

import com.damdamdeo.formula.domain.*;

import java.util.Objects;

public final class SuggestUseCase implements UseCase<SuggestionsCompletion, SuggestCommand> {

    private final SuggestCompletion autoSuggestCompletion;

    public SuggestUseCase(final SuggestCompletion suggestCompletion) {
        this.autoSuggestCompletion = Objects.requireNonNull(suggestCompletion);
    }

    @Override
    public SuggestionsCompletion execute(final SuggestCommand command) throws SuggestionException {
        try {
            return autoSuggestCompletion.suggest(command.suggestedFormula());
        } catch (final AutoSuggestUnavailableException exception) {
            throw new SuggestionException(exception);
        } catch (final AutoSuggestionExecutionException exception) {
            throw new SuggestionException(exception);
        } catch (final AutoSuggestionExecutionTimedOutException exception) {
            throw new SuggestionException(exception);
        } catch (final Exception exception) {
            throw new SuggestionException(exception);
        }
    }
}
