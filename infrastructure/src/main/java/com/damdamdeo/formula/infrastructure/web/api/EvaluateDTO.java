package com.damdamdeo.formula.infrastructure.web.api;

import com.damdamdeo.formula.domain.*;
import com.damdamdeo.formula.domain.usecase.EvaluateCommand;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.Map;
import java.util.stream.Collectors;

@Schema(name = "Evaluate", required = true, requiredProperties = {"formula", "structuredReferences", "debugFeature", "evaluateOn"})
public record EvaluateDTO(@Schema(required = true) String formula,
                          @Schema(required = true) Map<String, String> structuredReferences,
                          @Schema(required = true) DebugFeature debugFeature,
                          @Schema(required = true) EvaluateOn evaluateOn) {

    public EvaluateCommand toEvaluateCommand() {
        return new EvaluateCommand(
                new Formula(formula),
                structuredReferences.entrySet().stream()
                        .map(structuredDatum -> new StructuredReference(new Reference(structuredDatum.getKey()), new Value(structuredDatum.getValue())))
                        .collect(Collectors.toList()),
                debugFeature,
                evaluateOn
        );
    }
}
