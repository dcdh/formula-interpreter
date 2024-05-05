package com.damdamdeo.formula.domain.usecase;

import com.damdamdeo.formula.domain.SuggestedFormula;

public record SuggestCommand(SuggestedFormula suggestedFormula) implements UseCaseCommand {
}
