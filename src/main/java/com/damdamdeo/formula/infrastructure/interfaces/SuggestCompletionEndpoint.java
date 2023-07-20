package com.damdamdeo.formula.infrastructure.interfaces;

import com.damdamdeo.formula.domain.SuggestedFormula;
import com.damdamdeo.formula.domain.SuggestionException;
import com.damdamdeo.formula.domain.usecase.SuggestCommand;
import com.damdamdeo.formula.domain.usecase.SuggestUseCase;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

import java.util.List;
import java.util.Objects;

@Path("/suggestCompletion")
public final class SuggestCompletionEndpoint {
    private final SuggestUseCase suggestUseCase;

    public SuggestCompletionEndpoint(final SuggestUseCase suggestUseCase) {
        this.suggestUseCase = Objects.requireNonNull(suggestUseCase);
    }

    @POST
    @Produces("application/vnd.suggest-completion-v1+json")
    public List<String> suggestCompletion(@FormParam("suggestedFormula") final String suggestedFormula) throws SuggestionException {
        return suggestUseCase.execute(new SuggestCommand(new SuggestedFormula(suggestedFormula))).suggestions();
    }
}
