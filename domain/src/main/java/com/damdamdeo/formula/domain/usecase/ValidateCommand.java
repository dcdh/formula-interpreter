package com.damdamdeo.formula.domain.usecase;

import com.damdamdeo.formula.domain.Formula;

public record ValidateCommand(Formula formula) implements UseCaseCommand {
}
