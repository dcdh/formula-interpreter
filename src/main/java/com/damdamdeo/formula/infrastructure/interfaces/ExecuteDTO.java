package com.damdamdeo.formula.infrastructure.interfaces;

import com.damdamdeo.formula.domain.*;
import com.damdamdeo.formula.domain.usecase.ExecuteCommand;

import java.util.Map;
import java.util.stream.Collectors;

public record ExecuteDTO(String formula, Map<String, String> structuredData) {

    public ExecuteCommand toExecuteCommand() {
        return new ExecuteCommand(
                new Formula(formula),
                new StructuredData(structuredData.entrySet().stream()
                        .map(structuredDatum -> new StructuredDatum(new Reference(structuredDatum.getKey()), new Value(structuredDatum.getValue())))
                        .collect(Collectors.toList()))
        );
    }
}
