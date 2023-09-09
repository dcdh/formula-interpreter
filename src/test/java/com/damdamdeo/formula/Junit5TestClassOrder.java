package com.damdamdeo.formula;

import com.damdamdeo.formula.infrastructure.ApplicationTest;
import com.damdamdeo.formula.infrastructure.ApplicationTestIT;
import org.junit.jupiter.api.ClassDescriptor;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.ClassOrdererContext;

import java.util.Comparator;

/**
 * Run test following this priority:
 * 1. domain
 * 2. infra
 * 3. ApplicationTest
 * 4. ApplicationTestIT
 */
public final class Junit5TestClassOrder implements ClassOrderer {
    @Override
    public void orderClasses(final ClassOrdererContext classOrdererContext) {
        classOrdererContext.getClassDescriptors().sort(Comparator.comparingInt(Junit5TestClassOrder::getOrder));
    }

    private static int getOrder(final ClassDescriptor classDescriptor) {
        final String packageName = classDescriptor.getTestClass().getPackageName();
        if (classDescriptor.getTestClass().equals(ApplicationTest.class)) {
            return 3;
        } else if (classDescriptor.getTestClass().equals(ApplicationTestIT.class)) {
            return 4;
        } else if (packageName.endsWith(".domain")
                   || packageName.contains(".domain.")) {
            return 1;
        } else if (packageName.endsWith(".infrastructure")
                   || packageName.contains(".infrastructure.")) {
            return 2;
        } else {
            return 5;
        }
    }
}
