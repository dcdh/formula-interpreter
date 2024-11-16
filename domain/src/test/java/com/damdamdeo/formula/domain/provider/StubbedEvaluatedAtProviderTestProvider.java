package com.damdamdeo.formula.domain.provider;

import com.damdamdeo.formula.domain.ProcessedAt;
import com.damdamdeo.formula.domain.spi.ProcessedAtProvider;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.util.Iterator;

public final class StubbedEvaluatedAtProviderTestProvider implements ParameterResolver {
    @Override
    public boolean supportsParameter(final ParameterContext parameterContext,
                                     final ExtensionContext extensionContext) throws ParameterResolutionException {
        return ProcessedAtProvider.class.equals(parameterContext.getParameter().getType());
    }

    @Override
    public Object resolveParameter(final ParameterContext parameterContext,
                                   final ExtensionContext extensionContext) throws ParameterResolutionException {
        return new StubbedProcessedAtProvider(ListOfEvaluatedAtParameterResolver.LIST_OF_EXECUTED_ATS);
    }

    private static final class StubbedProcessedAtProvider implements ProcessedAtProvider {
        private final Iterator<ProcessedAt> responseIterator;

        private StubbedProcessedAtProvider(final ListOfEvaluatedAtParameterResolver.ListOfEvaluatedAt listOfEvaluatedAts) {
            this.responseIterator = listOfEvaluatedAts.listOf().iterator();
        }

        @Override
        public ProcessedAt now() {
            final ProcessedAt response;
            if (responseIterator.hasNext()) {
                response = responseIterator.next();
            } else {
                throw new IllegalStateException("No more responses");
            }
            return response;
        }
    }
}
