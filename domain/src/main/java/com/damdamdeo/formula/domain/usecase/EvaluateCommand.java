package com.damdamdeo.formula.domain.usecase;

import com.damdamdeo.formula.domain.DebugFeature;
import com.damdamdeo.formula.domain.EvaluateOn;
import com.damdamdeo.formula.domain.Formula;
import com.damdamdeo.formula.domain.StructuredReference;

import java.util.List;

public record EvaluateCommand(Formula formula,
                              List<StructuredReference> structuredReferences,
                              DebugFeature debugFeature,
                              EvaluateOn evaluateOn) implements UseCaseCommand {
}
