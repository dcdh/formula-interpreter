package com.damdamdeo.formula.infrastructure.evaluation.provider;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

public final class IntermediateResultsByFormulaProvider implements ArgumentsProvider {

    FCK juste le faire par formule une fois pour chaque type ...
    @Override
    public Stream<? extends Arguments> provideArguments(final ExtensionContext context) throws Exception {
//        return Stream.of(Arguments.of(
//
//        ));
    }
}
