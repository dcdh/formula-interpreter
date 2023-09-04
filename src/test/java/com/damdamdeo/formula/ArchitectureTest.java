package com.damdamdeo.formula;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.Architectures;

@AnalyzeClasses(importOptions = {
        ImportOption.DoNotIncludeTests.class
})
public class ArchitectureTest {

    @ArchTest
    public void shouldRespectHexagonalArchitecture(final JavaClasses classes) {
        final ArchRule hexagonalArchitectureRule = Architectures.layeredArchitecture()
                .consideringOnlyDependenciesInLayers()
                .layer("domain").definedBy("..domain..")
                .layer("infrastructure").definedBy("..infrastructure..")
                .whereLayer("domain").mayOnlyBeAccessedByLayers("infrastructure")
                .whereLayer("infrastructure").mayNotBeAccessedByAnyLayer();
        hexagonalArchitectureRule.check(classes);
    }

}
