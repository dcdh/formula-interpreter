package com.damdamdeo.formula.infrastructure;

import com.damdamdeo.formula.infrastructure.ApplicationTest;
import com.damdamdeo.formula.infrastructure.ApplicationTestIT;
import org.junit.jupiter.api.ClassDescriptor;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.ClassOrdererContext;

import java.util.Comparator;

/**
 * Run test following this priority:
 * 1. infra
 * 2. ApplicationTest
 * 3. ApplicationTestIT
 */
public final class Junit5TestClassOrder implements ClassOrderer {
    @Override
    public void orderClasses(final ClassOrdererContext classOrdererContext) {
        classOrdererContext.getClassDescriptors().sort(Comparator.comparingInt(Junit5TestClassOrder::getOrder));
    }

    private static int getOrder(final ClassDescriptor classDescriptor) {
        if (classDescriptor.getTestClass().equals(ApplicationTestIT.class)) {
            return 3;
        } else if (classDescriptor.getTestClass().equals(ApplicationTest.class)) {
            return 2;
        } else {
            return 1;
        }
    }
}
