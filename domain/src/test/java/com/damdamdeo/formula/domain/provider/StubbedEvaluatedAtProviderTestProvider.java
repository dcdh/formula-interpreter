package com.damdamdeo.formula.domain.provider;

import com.damdamdeo.formula.domain.EvaluatedAt;
import com.damdamdeo.formula.domain.spi.EvaluatedAtProvider;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.util.Iterator;

public final class StubbedEvaluatedAtProviderTestProvider implements ParameterResolver {
    @Override
    public boolean supportsParameter(final ParameterContext parameterContext,
                                     final ExtensionContext extensionContext) throws ParameterResolutionException {
        return EvaluatedAtProvider.class.equals(parameterContext.getParameter().getType());
    }

    @Override
    public Object resolveParameter(final ParameterContext parameterContext,
                                   final ExtensionContext extensionContext) throws ParameterResolutionException {
        return new StubbedEvaluatedAtProvider(ListOfEvaluatedAtParameterResolver.LIST_OF_EXECUTED_ATS);
    }

    private static final class StubbedEvaluatedAtProvider implements EvaluatedAtProvider {
        private final Iterator<EvaluatedAt> responseIterator;

        private StubbedEvaluatedAtProvider(final ListOfEvaluatedAtParameterResolver.ListOfEvaluatedAt listOfEvaluatedAts) {
            this.responseIterator = listOfEvaluatedAts.listOf().iterator();
        }

        @Override
        public EvaluatedAt now() {
            final EvaluatedAt response;
            if (responseIterator.hasNext()) {
                response = responseIterator.next();
            } else {
                throw new IllegalStateException("No more responses");
            }
            return response;
        }
    }
}
