package com.damdamdeo.formula.infrastructure.evaluation.provider;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

public final class EvaluationArgumentArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(final ExtensionContext context) throws Exception {
//        TODO reprendre depuis le domain :) le Hello World, true, false, 0, 1 et numeric ...
////        idem
//// TODO pour l'evaluation je devrais reprendre le meme ... ou bien Joe ... à voir ...
        throw new RuntimeException();
    }
//    ValueProviders merger dans le stream comme pour le evaluationen dessous ...
}
