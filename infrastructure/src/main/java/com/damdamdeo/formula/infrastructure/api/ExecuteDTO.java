package com.damdamdeo.formula.infrastructure.api;

import com.damdamdeo.formula.domain.*;
import com.damdamdeo.formula.domain.usecase.ExecuteCommand;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.Map;
import java.util.stream.Collectors;

@Schema(name = "Execute", required = true, requiredProperties = {"formula", "structuredReferences", "debugFeature"})
public record ExecuteDTO(@Schema(required = true) String formula,
                         @Schema(required = true) Map<String, String> structuredReferences,
                         @Schema(required = true) DebugFeature debugFeature) {

    public ExecuteCommand toExecuteCommand() {
        return new ExecuteCommand(
                new Formula(formula),
                new StructuredReferences(structuredReferences.entrySet().stream()
                        .map(structuredDatum -> new StructuredReference(new Reference(structuredDatum.getKey()), new Value(structuredDatum.getValue())))
                        .collect(Collectors.toList())),
                debugFeature
        );
    }
}
