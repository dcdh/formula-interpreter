package com.damdamdeo.formula.infrastructure.evaluation.antlr;

import com.damdamdeo.formula.domain.*;
import com.damdamdeo.formula.domain.spi.ValueProvider;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public final class PartEvaluationCallback {
    private final PartEvaluationListener partEvaluationListener;
    private PartEvaluationId currentPartEvaluationId;
    private final NumericalContext numericalContext;
    private final List<StructuredReference> structuredReferences;
    private Evaluated currentEvaluated = new Evaluated();

    public PartEvaluationCallback(final PartEvaluationListener partEvaluationListener,
                                  final NumericalContext numericalContext,
                                  final List<StructuredReference> structuredReferences) {
        this.partEvaluationListener = Objects.requireNonNull(partEvaluationListener);
        this.currentPartEvaluationId = new PartEvaluationId(-1);
        this.numericalContext = Objects.requireNonNull(numericalContext);
        this.structuredReferences = Objects.requireNonNull(structuredReferences);
    }

    public void storeCurrentEvaluation(final Evaluated currentEvaluated) {
        this.currentEvaluated = Objects.requireNonNull(currentEvaluated);
    }

    public Evaluated getCurrentEvaluated() {
        return currentEvaluated;
    }

    public Evaluated evaluateArithmeticFunctions(final Supplier<ArithmeticFunction.Function> arithmeticFunctionSupplier,
                                                 final Supplier<Evaluated> leftSupplier,
                                                 final Supplier<Evaluated> rightSupplier,
                                                 final Supplier<PositionedAt> positionedAtSupplier) {
        Objects.requireNonNull(arithmeticFunctionSupplier);
        Objects.requireNonNull(leftSupplier);
        Objects.requireNonNull(rightSupplier);
        Objects.requireNonNull(positionedAtSupplier);
        return evaluate(() -> {
            final Evaluated left = leftSupplier.get();
            final Evaluated right = rightSupplier.get();
            final Value value = ArithmeticFunction.of(arithmeticFunctionSupplier.get(), left.value(), right.value()).evaluate(numericalContext);
            return new Evaluated(value, positionedAtSupplier.get(),
                    List.of(
                            new Input(
                                    InputName.ofLeft(),
                                    left.value(),
                                    left.positionedAt()),
                            new Input(
                                    InputName.ofRight(),
                                    right.value(),
                                    right.positionedAt())
                    )
            );
        });
    }

    public Evaluated evaluateComparisonFunctions(final Supplier<ComparisonFunction.Comparison> comparisonFunctionSupplier,
                                                 final Supplier<Evaluated> leftSupplier,
                                                 final Supplier<Evaluated> rightSupplier,
                                                 final Supplier<PositionedAt> positionedAtSupplier) {
        Objects.requireNonNull(comparisonFunctionSupplier);
        Objects.requireNonNull(leftSupplier);
        Objects.requireNonNull(rightSupplier);
        Objects.requireNonNull(positionedAtSupplier);
        return evaluate(() -> {
            final Evaluated left = leftSupplier.get();
            final Evaluated right = rightSupplier.get();
            final Value value = ComparisonFunction.of(comparisonFunctionSupplier.get(), left.value(), right.value())
                    .evaluate(numericalContext);
            return new Evaluated(value, positionedAtSupplier.get(),
                    List.of(
                            new Input(
                                    InputName.ofLeft(),
                                    left.value(),
                                    left.positionedAt()),
                            new Input(
                                    InputName.ofRight(),
                                    right.value(),
                                    right.positionedAt())
                    )
            );
        });
    }

    public Evaluated evaluateLogicalBooleanFunctions(final Supplier<LogicalBooleanFunction.Function> logicalBooleanFunctionSupplier,
                                                     final Supplier<Evaluated> leftSupplier,
                                                     final Supplier<Evaluated> rightSupplier,
                                                     final Supplier<PositionedAt> positionedAtSupplier) {
        Objects.requireNonNull(logicalBooleanFunctionSupplier);
        Objects.requireNonNull(leftSupplier);
        Objects.requireNonNull(rightSupplier);
        Objects.requireNonNull(positionedAtSupplier);
        return evaluate(() -> {
            final Evaluated left = leftSupplier.get();
            final Evaluated right = rightSupplier.get();
            final Value value = LogicalBooleanFunction.of(logicalBooleanFunctionSupplier.get(), left.value(), right.value()).evaluate(numericalContext);
            return new Evaluated(value, positionedAtSupplier.get(),
                    List.of(
                            new Input(
                                    InputName.ofLeft(),
                                    left.value(),
                                    left.positionedAt()),
                            new Input(
                                    InputName.ofRight(),
                                    right.value(),
                                    right.positionedAt())
                    )
            );
        });
    }

    public Evaluated evaluateLogicalComparisonFunctions(final Supplier<LogicalComparisonFunction.Function> logicalComparisonFunctionSupplier,
                                                        final Supplier<Evaluated> comparisonSupplier,
                                                        final Supplier<ValueProvider> onTrueSupplier,
                                                        final Supplier<ValueProvider> onFalseSupplier,
                                                        final Supplier<PositionedAt> positionedAtSupplier) {
        Objects.requireNonNull(logicalComparisonFunctionSupplier);
        Objects.requireNonNull(comparisonSupplier);
        Objects.requireNonNull(positionedAtSupplier);
        return evaluate(() -> {
            final Evaluated comparison = comparisonSupplier.get();
            final Value value = LogicalComparisonFunction.of(logicalComparisonFunctionSupplier.get(), comparison.value(),
                            onTrueSupplier.get(),
                            onFalseSupplier.get())
                    .evaluate(numericalContext);
            return new Evaluated(value, positionedAtSupplier.get(),
                    List.of(
                            new Input(
                                    InputName.ofComparisonValue(),
                                    comparison.value(),
                                    comparison.positionedAt())
                    )
            );
        });
    }

    public Evaluated evaluateStateFunction(final Supplier<StateFunction.Function> stateFunctionSupplier,
                                           final Supplier<Evaluated> stateSupplier,
                                           final Supplier<PositionedAt> positionedAtSupplier) {
        Objects.requireNonNull(stateFunctionSupplier);
        Objects.requireNonNull(stateSupplier);
        Objects.requireNonNull(positionedAtSupplier);
        return evaluate(() -> {
            final Evaluated state = stateSupplier.get();
            final Value value = StateFunction.of(stateFunctionSupplier.get(), state.value()).evaluate(numericalContext);
            return new Evaluated(value, positionedAtSupplier.get(),
                    List.of(
                            new Input(
                                    InputName.ofValue(),
                                    state.value(),
                                    state.positionedAt())
                    )
            );
        });
    }

    public Evaluated evaluateArgument(final Supplier<Argument> argumentSupplier,
                                      final Supplier<PositionedAt> positionedAtSupplier) {
        Objects.requireNonNull(argumentSupplier);
        Objects.requireNonNull(positionedAtSupplier);
        return evaluate(
                () -> {
                    final Argument argument = argumentSupplier.get();
                    final Value value = argument.resolveArgument(structuredReferences);
                    final PositionedAt positionedAt = positionedAtSupplier.get();
                    if (argument.reference() != null) {
                        return new Evaluated(value,
                                positionedAt,
                                List.of(
                                        new Input(
                                                InputName.ofStructuredReference(),
                                                argument.reference(),
                                                positionedAt.of(+3, -2))));
                    } else {
                        return new Evaluated(
                                value,
                                positionedAt);
                    }
                });
    }

    private Evaluated evaluate(final Supplier<Evaluated> evaluatedSupplier) {
        Objects.requireNonNull(evaluatedSupplier);
        currentPartEvaluationId = currentPartEvaluationId.increment();
        final PartEvaluationId partEvaluationId = currentPartEvaluationId;
        this.partEvaluationListener.onBeforePartEvaluation(partEvaluationId);
        final Evaluated evaluated = evaluatedSupplier.get();
        this.partEvaluationListener.onAfterPartEvaluation(partEvaluationId, evaluated);
        return evaluated;
    }
}
