package com.damdamdeo.formula.domain.usecase;

import com.damdamdeo.formula.domain.DebugFeature;
import com.damdamdeo.formula.domain.Formula;
import com.damdamdeo.formula.domain.StructuredData;

public record ExecuteCommand(Formula formula,
                             StructuredData structuredData,
                             DebugFeature debugFeature) implements UseCaseCommand {
}
