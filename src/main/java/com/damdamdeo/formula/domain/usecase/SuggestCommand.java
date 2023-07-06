package com.damdamdeo.formula.domain.usecase;

import com.damdamdeo.formula.domain.Formula;
import com.damdamdeo.formula.domain.SuggestedFormula;
import com.damdamdeo.formula.domain.UseCaseCommand;

public record SuggestCommand(Formula formula, SuggestedFormula suggestedFormula) implements UseCaseCommand {
}
