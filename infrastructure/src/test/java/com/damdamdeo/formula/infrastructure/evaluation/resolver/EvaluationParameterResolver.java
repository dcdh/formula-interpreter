package com.damdamdeo.formula.infrastructure.evaluation.resolver;

import com.damdamdeo.formula.domain.Evaluated;
import com.damdamdeo.formula.domain.Formula;
import com.damdamdeo.formula.domain.IntermediateResult;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.util.List;
import java.util.Map;
import java.util.Objects;

// TODO enrichir un max possible ...

ce n'est pas un parameter resolver ... mais plutot un methode source ...
public final class EvaluationParameterResolver implements ParameterResolver {

    private final Map<>

    @Override
    public boolean supportsParameter(final ParameterContext parameterContext,
                                     final ExtensionContext extensionContext) throws ParameterResolutionException {
        return ;
    }

    @Override
    public Object resolveParameter(final ParameterContext parameterContext,
                                   final ExtensionContext extensionContext) throws ParameterResolutionException {
        return ;
    }

//    faire deux tags l'un pour tester l'evaluation et l'autre les intermediate results

    //    C'est ici que je vais m'amuser ...
}
