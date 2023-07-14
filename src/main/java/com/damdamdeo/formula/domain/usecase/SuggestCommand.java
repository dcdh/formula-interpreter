package com.damdamdeo.formula.domain.usecase;

import com.damdamdeo.formula.domain.SuggestedFormula;
import com.damdamdeo.formula.domain.UseCaseCommand;

public record SuggestCommand(SuggestedFormula suggestedFormula) implements UseCaseCommand {
}
