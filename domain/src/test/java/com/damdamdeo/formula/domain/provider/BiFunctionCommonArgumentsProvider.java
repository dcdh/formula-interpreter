package com.damdamdeo.formula.domain.provider;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

public final class BiFunctionCommonArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(final ExtensionContext context) throws Exception {
        return BiFunctionCommonProviders.provide()
                .stream()
                .map(biFunctionCommon -> Arguments.of(
                        biFunctionCommon.givenLeft(), biFunctionCommon.givenRight(), biFunctionCommon.expected()));
    }
}
