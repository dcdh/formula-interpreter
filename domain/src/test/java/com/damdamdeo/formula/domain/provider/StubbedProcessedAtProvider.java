package com.damdamdeo.formula.domain.provider;

import com.damdamdeo.formula.domain.ProcessedAt;
import com.damdamdeo.formula.domain.spi.ProcessedAtProvider;

import java.util.Iterator;

public final class StubbedProcessedAtProvider implements ProcessedAtProvider {
    private final Iterator<ProcessedAt> responseIterator;

    public StubbedProcessedAtProvider() {
        this.responseIterator = ListOfProcessedAtParameterResolver.LIST_OF_EXECUTED_ATS.listOf().iterator();
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