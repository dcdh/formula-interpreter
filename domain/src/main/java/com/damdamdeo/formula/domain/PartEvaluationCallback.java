package com.damdamdeo.formula.domain;

import com.damdamdeo.formula.domain.spi.ValueProvider;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public final class PartEvaluationCallback {
    private final PartEvaluationListener partEvaluationListener;
    private PartEvaluationId currentPartEvaluationId;
    private final NumericalContext numericalContext;
    private final List<StructuredReference> structuredData;
    private Evaluated currentEvaluated = new Evaluated();

    public PartEvaluationCallback(final PartEvaluationListener partEvaluationListener,
                                  final NumericalContext numericalContext,
                                  final List<StructuredReference> structuredData) {
        this.partEvaluationListener = Objects.requireNonNull(partEvaluationListener);
        this.currentPartEvaluationId = new PartEvaluationId(-1);
        this.numericalContext = Objects.requireNonNull(numericalContext);
        this.structuredData = Objects.requireNonNull(structuredData);
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
                    () -> List.of(
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
                    () -> List.of(
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
                    () -> List.of(
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
                    () -> List.of(
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
                    () -> List.of(
                            new Input(
                                    InputName.ofValue(),
                                    state.value(),
                                    state.positionedAt())
                    )
            );
        });
    }

    public Evaluated evaluateArgumentStructuredReference(final Supplier<Reference> referenceSupplier,
                                                         final Supplier<PositionedAt> positionedAtSupplier) {
        Objects.requireNonNull(referenceSupplier);
        Objects.requireNonNull(positionedAtSupplier);
        return evaluate(() -> {
            final StructuredReferencesFunction structuredReferencesFunction = new StructuredReferencesFunction(structuredData, referenceSupplier.get());
            final Value value = structuredReferencesFunction.evaluate(numericalContext);
            return new Evaluated(value, positionedAtSupplier.get(),
                    () -> List.of(
                            new Input(
                                    InputName.ofStructuredReference(),
                                    structuredReferencesFunction.reference(),
                                    positionedAtSupplier.get().of(+3, -2)
                            )));
        });
    }

    public Evaluated evaluateArgument(final Supplier<Value> valueSupplier,
                                      final Supplier<PositionedAt> positionedAtSupplier) {
        Objects.requireNonNull(valueSupplier);
        Objects.requireNonNull(positionedAtSupplier);
        return evaluate(
                () -> new Evaluated(valueSupplier.get(), positionedAtSupplier.get()));
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

    public List<IntermediateResult> intermediateResults() {
        return this.partEvaluationListener.intermediateResults();
    }
}
