package com.damdamdeo.formula.domain.usecase;

import com.damdamdeo.formula.domain.DebugFeature;
import com.damdamdeo.formula.domain.Formula;
import com.damdamdeo.formula.domain.StructuredReferences;

public record ExecuteCommand(Formula formula,
                             StructuredReferences structuredReferences,
                             DebugFeature debugFeature) implements UseCaseCommand {
}
