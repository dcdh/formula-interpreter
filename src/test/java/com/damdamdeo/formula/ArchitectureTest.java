package com.damdamdeo.formula;

import com.tngtech.archunit.core.domain.JavaAnnotation;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaType;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import com.tngtech.archunit.library.Architectures;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ArchitectureTest {
    @Test
    public void shouldRespectHexagonalArchitecture() {
        final JavaClasses classes = new ClassFileImporter()
                .importPackages("com.damdamdeo.formula..");
        final ArchRule hexagonalArchitectureRule = Architectures.layeredArchitecture()
                .consideringOnlyDependenciesInLayers()
                .layer("domain").definedBy("..domain..")
                .layer("infrastructure").definedBy("..infrastructure..")
                .whereLayer("domain").mayOnlyBeAccessedByLayers("infrastructure")
                .whereLayer("infrastructure").mayNotBeAccessedByAnyLayer();
        hexagonalArchitectureRule.check(classes);
    }

    private static final List<String> ALLOWED_EXTERNAL_ANNOTATIONS = List.of("java.lang.FunctionalInterface");

    @Test
    public void shouldDomainDependsOnSpecificPackages() {
        final JavaClasses classes = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("com.damdamdeo.formula..");
        final ArchRule domainClassesRule = ArchRuleDefinition.classes()
                .that().resideInAPackage("com.damdamdeo.formula.domain..")
                .should().onlyAccessClassesThat().resideInAnyPackage(
                        "com.damdamdeo.formula.domain..",
                        "java.util..",
                        "java.lang..",
                        "java.math..",
                        "java.time.."
                )
                .andShould(new ArchCondition<>("Depend mostly on domain annotations") {
                    @Override
                    public void check(final JavaClass javaClass, final ConditionEvents conditionEvents) {
                        final List<JavaType> illegalAnnotationsDependency = Stream.of(
                                        javaClass.getAnnotations(),
                                        javaClass.getAllFields().stream().flatMap(javaField -> javaField.getAnnotations().stream())
                                                .collect(Collectors.toList()),
                                        javaClass.getConstructors().stream().flatMap(javaConstructor -> javaConstructor.getAnnotations().stream())
                                                .collect(Collectors.toList()),
                                        javaClass.getConstructors().stream().flatMap(javaConstructor -> javaConstructor.getParameters().stream())
                                                .flatMap(parameter -> parameter.getAnnotations().stream()).collect(Collectors.toList()),
                                        javaClass.getMethods().stream().flatMap(javaMethod -> javaMethod.getAnnotations().stream())
                                                .collect(Collectors.toList()),
                                        javaClass.getMethods().stream().flatMap(javaMethod -> javaMethod.getParameters().stream())
                                                .flatMap(parameter -> parameter.getAnnotations().stream()).collect(Collectors.toList())
                                )
                                .flatMap(Collection::stream)
                                .map(JavaAnnotation::getType)
                                .filter(type -> !ALLOWED_EXTERNAL_ANNOTATIONS.contains(type.getName()))
                                .filter(type -> !type.getName().startsWith("com.damdamdeo.formula.domain."))
                                .toList();
                        if (illegalAnnotationsDependency.size() > 0) {
                            conditionEvents.add(SimpleConditionEvent.violated(javaClass, String.format("Annotations '%s' not belonging to allowed external annotations or domain are forbidden",
                                    illegalAnnotationsDependency.stream()
                                            .map(JavaType::getName)
                                            .collect(Collectors.toList()))));
                        }
                    }
                });
        domainClassesRule.check(classes);
    }
}
