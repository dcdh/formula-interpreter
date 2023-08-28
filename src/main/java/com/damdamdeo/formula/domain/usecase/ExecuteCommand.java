package com.damdamdeo.formula.domain.usecase;

import com.damdamdeo.formula.domain.DebugFeature;
import com.damdamdeo.formula.domain.Formula;
import com.damdamdeo.formula.domain.StructuredData;
import com.damdamdeo.formula.domain.UseCaseCommand;

public record ExecuteCommand(Formula formula,
                             StructuredData structuredData,
                             DebugFeature debugFeature) implements UseCaseCommand {
}
