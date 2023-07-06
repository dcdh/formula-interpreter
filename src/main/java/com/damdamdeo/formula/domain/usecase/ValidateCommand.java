package com.damdamdeo.formula.domain.usecase;

import com.damdamdeo.formula.domain.Formula;
import com.damdamdeo.formula.domain.UseCaseCommand;

public record ValidateCommand(Formula formula) implements UseCaseCommand {
}
