package com.damdamdeo.formula.domain.spi;

import com.damdamdeo.formula.domain.SuggestedFormula;
import com.damdamdeo.formula.domain.SuggestionsCompletion;
import io.smallrye.mutiny.Uni;

public interface SuggestCompletion {
    Uni<SuggestionsCompletion> suggest(SuggestedFormula suggestedFormula);
}
