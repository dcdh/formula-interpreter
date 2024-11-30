package com.damdamdeo.formula.domain.usecase.resolver;

import com.damdamdeo.formula.domain.*;
import com.damdamdeo.formula.domain.spi.ProcessedAtProvider;
import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.ZonedDateTime;
import java.util.*;

public abstract class EvaluateUseCaseTestResolver implements ParameterResolver {

    public static Formula FORMULA = new Formula("AND(0,0)");

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("DebugFeatureACTIVE")
    @Test
    public @interface DebugFeatureActiveTest {
    }

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("DebugFeatureINACTIVE")
    @Test
    public @interface DebugFeatureInactiveTest {
    }

    private final ProcessedAtProvider processedAtProvider;
    private final DebugPartEvaluationListener debugPartEvaluationListener;
    private final NoOpPartEvaluationListener noOpPartEvaluationListener;

    public EvaluateUseCaseTestResolver() {
        this.processedAtProvider = new ProcessedAtProvider() {
            final Queue<ProcessedAt> myQueue = new LinkedList<>(ListOfProcessedAtParameterResolver.LIST_OF_PROCESSED_AT.listOf());

            @Override
            public ProcessedAt now() {
                return myQueue.poll();
            }
        };
        this.debugPartEvaluationListener = new DebugPartEvaluationListener(processedAtProvider);
        this.noOpPartEvaluationListener = new NoOpPartEvaluationListener();
    }

    @Override
    public boolean supportsParameter(final ParameterContext parameterContext,
                                     final ExtensionContext extensionContext) throws ParameterResolutionException {
        return List.of(GivenHappyPath.class,
                        GivenFailing.class,
                        PartEvaluationListener.class,
                        DebugPartEvaluationListener.class,
                        NoOpPartEvaluationListener.class,
                        ProcessedAtProvider.class, EvaluationResult.class)
                .contains(parameterContext.getParameter().getType());
    }

    @Override
    public Object resolveParameter(final ParameterContext parameterContext,
                                   final ExtensionContext extensionContext) throws ParameterResolutionException {
        final Class<?> type = parameterContext.getParameter().getType();
        final DebugFeature debugFeature = extensionContext.getTags().stream().filter(tag -> tag.startsWith("DebugFeature"))
                .findFirst()
                .map(debugFeatureTag -> debugFeatureTag.substring("DebugFeature".length()))
                .map(DebugFeature::valueOf)
                .orElseThrow(() -> new IllegalStateException("Missing DebugFeature"));
        if (GivenHappyPath.class.equals(type)) {
            final List<IntermediateResult> intermediateResults = switch (debugFeature) {
                case ACTIVE -> List.of(
                        new IntermediateResult(
                                Value.of("false"),
                                new PositionedAt(0, 7),
                                List.of(
                                        new Input(
                                                new InputName("left"),
                                                Value.of("0"),
                                                new PositionedAt(4, 4)
                                        ),
                                        new Input(
                                                new InputName("right"),
                                                Value.of("0"),
                                                new PositionedAt(6, 6)
                                        )
                                ),
                                new EvaluationProcessedIn(
                                        new ProcessedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")),
                                        new ProcessedAt(ZonedDateTime.parse("2023-12-25T10:15:08+01:00[Europe/Paris]")))),
                        new IntermediateResult(
                                Value.of("0"),
                                new PositionedAt(4, 4),
                                List.of(),
                                new EvaluationProcessedIn(
                                        new ProcessedAt(ZonedDateTime.parse("2023-12-25T10:15:04+01:00[Europe/Paris]")),
                                        new ProcessedAt(ZonedDateTime.parse("2023-12-25T10:15:05+01:00[Europe/Paris]")))),
                        new IntermediateResult(
                                Value.of("0"),
                                new PositionedAt(6, 6),
                                List.of(),
                                new EvaluationProcessedIn(
                                        new ProcessedAt(ZonedDateTime.parse("2023-12-25T10:15:06+01:00[Europe/Paris]")),
                                        new ProcessedAt(ZonedDateTime.parse("2023-12-25T10:15:07+01:00[Europe/Paris]"))))
                );
                case INACTIVE -> List.of();
            };
            final ProcessingMetrics processingMetrics = switch (debugFeature) {
                case ACTIVE -> new ProcessingMetrics(
                        FormulaCacheRetrieval.MISSED,
                        new EvaluationLoadingProcessedIn(
                                new ProcessedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")),
                                new ProcessedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]"))),
                        new EvaluationProcessedIn(
                                new ProcessedAt(ZonedDateTime.parse("2023-12-25T10:15:02+01:00[Europe/Paris]")),
                                new ProcessedAt(ZonedDateTime.parse("2023-12-25T10:15:09+01:00[Europe/Paris]"))
                        )
                );
                case INACTIVE -> new ProcessingMetrics(
                        FormulaCacheRetrieval.MISSED,
                        new EvaluationLoadingProcessedIn(
                                new ProcessedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")),
                                new ProcessedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]"))),
                        new EvaluationProcessedIn(
                                new ProcessedAt(ZonedDateTime.parse("2023-12-25T10:15:02+01:00[Europe/Paris]")),
                                new ProcessedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]"))
                        )
                );
            };
            return new GivenHappyPath(
                    FORMULA,
                    debugFeature,
                    new Evaluated(
                            new Value("false"),
                            new PositionedAt(0, 7),
                            List.of(
                                    // FIXME TODO ...
                            )
                    ),
                    new EvaluationResult(new Value("false"), intermediateResults, processingMetrics)
            );
        } else if (GivenFailing.class.equals(type)) {
            return givenFailings()
                    .entrySet()
                    .stream().filter(formulaThrowableEntry ->
                            extensionContext.getTags().contains(
                                    formulaThrowableEntry.getValue().getClass().getSimpleName()))
                    .findFirst()
                    .map(formulaThrowableEntry -> new GivenFailing(
                            formulaThrowableEntry.getKey(), debugFeature, formulaThrowableEntry.getValue()))
                    .orElseThrow(() -> new IllegalStateException("No tags match one of the givenFailingResponse"));
        } else if (ProcessedAtProvider.class.equals(type)) {
            return processedAtProvider;
        } else if (PartEvaluationListener.class.equals(type)) {
            return switch (debugFeature) {
                case ACTIVE -> debugPartEvaluationListener;
                case INACTIVE -> noOpPartEvaluationListener;
            };
        } else if (DebugPartEvaluationListener.class.equals(type)) {
            return debugPartEvaluationListener;
        } else if (NoOpPartEvaluationListener.class.equals(type)) {
            return noOpPartEvaluationListener;
        } else {
            throw new IllegalStateException("Should not be here: unsupported parameter type");
        }
    }

    protected abstract Map<Formula, Throwable> givenFailings();

    public record GivenHappyPath(Formula formula, DebugFeature debugFeature,
                                 Evaluated evaluated,
                                 EvaluationResult evaluationResult) {
        public GivenHappyPath {
            Objects.requireNonNull(formula);
            Objects.requireNonNull(debugFeature);
            Objects.requireNonNull(evaluated);
            Objects.requireNonNull(evaluationResult);
        }

        public Uni<EvaluationResult> toUni() {
            return Uni.createFrom().item(evaluationResult);
        }
    }

    public record GivenFailing(Formula formula, DebugFeature debugFeature, Throwable cause) {
        public GivenFailing {
            Objects.requireNonNull(formula);
            Objects.requireNonNull(debugFeature);
            Objects.requireNonNull(cause);
        }

        public Uni<Exception> toUni() {
            return Uni.createFrom().failure(cause);
        }
    }
}
