package com.damdamdeo.formula.domain.provider;

import com.damdamdeo.formula.domain.spi.ProcessedAtProvider;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public final class StubbedProcessedAtProviderParameterResolver implements ParameterResolver {
    @Override
    public boolean supportsParameter(final ParameterContext parameterContext,
                                     final ExtensionContext extensionContext) throws ParameterResolutionException {
        return ProcessedAtProvider.class.equals(parameterContext.getParameter().getType());
    }

    @Override
    public Object resolveParameter(final ParameterContext parameterContext,
                                   final ExtensionContext extensionContext) throws ParameterResolutionException {
        if (ProcessedAtProvider.class.equals(parameterContext.getParameter().getType())) {
            return new StubbedProcessedAtProvider();
        } else {
            throw new ParameterResolutionException("Parameter " + parameterContext.getParameter().getType() + " not supported");
        }
    }

}
