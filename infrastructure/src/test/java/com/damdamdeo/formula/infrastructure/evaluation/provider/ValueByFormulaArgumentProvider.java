package com.damdamdeo.formula.infrastructure.evaluation.provider;

import com.damdamdeo.formula.domain.Formula;
import com.damdamdeo.formula.domain.Value;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public final class ValueByFormulaArgumentProvider implements ArgumentsProvider {

    private static final Map<Formula, Value> VALUE_BY_FORMULA = new HashMap<>();

    static {
        je vais devoir reprendre depuis le domain en reconstituant la formule ...
    }

    @Override
    public Stream<? extends Arguments> provideArguments(final ExtensionContext context) throws Exception {
    }
}
