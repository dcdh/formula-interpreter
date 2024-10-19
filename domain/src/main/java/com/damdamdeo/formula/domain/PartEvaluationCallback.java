package com.damdamdeo.formula.domain;

import com.damdamdeo.formula.domain.spi.ValueProvider;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public final class PartEvaluationCallback {
    private final PartEvaluationCallbackListener partEvaluationCallbackListener;
    private PartEvaluationId currentPartEvaluationId;
    private final NumericalContext numericalContext;
    private final List<StructuredReference> structuredData;
    private Evaluated currentEvaluated = new Evaluated();

    public PartEvaluationCallback(final PartEvaluationCallbackListener partEvaluationCallbackListener,
                                  final NumericalContext numericalContext,
                                  final List<StructuredReference> structuredData) {
        this.partEvaluationCallbackListener = Objects.requireNonNull(partEvaluationCallbackListener);
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

    public Evaluated evaluateArithmeticFunctions(final Supplier<ArithmeticFunction.Function> arithmeticFunction,
                                                 final Supplier<Evaluated> left,
                                                 final Supplier<Evaluated> right,
                                                 final Supplier<Range> range) {
        Objects.requireNonNull(arithmeticFunction);
        Objects.requireNonNull(left);
        Objects.requireNonNull(right);
        Objects.requireNonNull(range);
        return evaluate(() -> {
            final Evaluated leftEvaluated = left.get();
            final Evaluated rightEvaluated = right.get();
            final Value value = ArithmeticFunction.of(arithmeticFunction.get(), leftEvaluated.value(), rightEvaluated.value()).evaluate(numericalContext);
            return new Evaluated(value, range.get(),
                    () -> List.of(
                            new Input(
                                    InputName.ofLeft(),
                                    leftEvaluated.value(),
                                    leftEvaluated.range()),
                            new Input(
                                    InputName.ofRight(),
                                    rightEvaluated.value(),
                                    rightEvaluated.range())
                    )
            );
        });
    }

    public Evaluated evaluateComparisonFunctions(final Supplier<ComparisonFunction.ComparisonType> comparisonFunction,
                                                 final Supplier<Evaluated> left,
                                                 final Supplier<Evaluated> right,
                                                 final Supplier<Range> range) {
        Objects.requireNonNull(comparisonFunction);
        Objects.requireNonNull(left);
        Objects.requireNonNull(right);
        Objects.requireNonNull(range);
        return evaluate(() -> {
            final Evaluated leftEvaluated = left.get();
            final Evaluated rightEvaluated = right.get();
            final Value value = ComparisonFunction.of(comparisonFunction.get(), leftEvaluated.value(), rightEvaluated.value())
                    .evaluate(numericalContext);
            return new Evaluated(value, range.get(),
                    () -> List.of(
                            new Input(
                                    InputName.ofLeft(),
                                    leftEvaluated.value(),
                                    leftEvaluated.range()),
                            new Input(
                                    InputName.ofRight(),
                                    rightEvaluated.value(),
                                    rightEvaluated.range())
                    )
            );
        });
    }

    public Evaluated evaluateLogicalBooleanFunctions(final Supplier<LogicalBooleanFunction.Function> logicalBooleanFunction,
                                                     final Supplier<Evaluated> left,
                                                     final Supplier<Evaluated> right,
                                                     final Supplier<Range> range) {
        Objects.requireNonNull(logicalBooleanFunction);
        Objects.requireNonNull(left);
        Objects.requireNonNull(right);
        Objects.requireNonNull(range);
        return evaluate(() -> {
            final Evaluated leftEvaluated = left.get();
            final Evaluated rightEvaluated = right.get();
            final Value value = LogicalBooleanFunction.of(logicalBooleanFunction.get(), leftEvaluated.value(), rightEvaluated.value()).evaluate(numericalContext);
            return new Evaluated(value, range.get(),
                    () -> List.of(
                            new Input(
                                    InputName.ofLeft(),
                                    leftEvaluated.value(),
                                    leftEvaluated.range()),
                            new Input(
                                    InputName.ofRight(),
                                    rightEvaluated.value(),
                                    rightEvaluated.range())
                    )
            );
        });
    }

    public Evaluated evaluateLogicalComparisonFunctions(final Supplier<LogicalComparisonFunction.Function> logicalComparisonFunction,
                                                        final Supplier<Evaluated> comparison,
                                                        final Supplier<ValueProvider> onTrue,
                                                        final Supplier<ValueProvider> onFalse,
                                                        final Supplier<Range> range) {
        Objects.requireNonNull(logicalComparisonFunction);
        Objects.requireNonNull(comparison);
        Objects.requireNonNull(range);
        return evaluate(() -> {
            final Evaluated comparisonEvaluated = comparison.get();
            final Value value = LogicalComparisonFunction.of(logicalComparisonFunction.get(), comparisonEvaluated.value(),
                            onTrue.get(),
                            onFalse.get())
                    .evaluate(numericalContext);
            return new Evaluated(value, range.get(),
                    () -> List.of(
                            new Input(
                                    InputName.ofComparisonValue(),
                                    comparisonEvaluated.value(),
                                    comparisonEvaluated.range())
                    )
            );
        });
    }

    public Evaluated evaluateStateFunction(final Supplier<StateFunction.Function> stateFunction,
                                           final Supplier<Evaluated> state,
                                           final Supplier<Range> range) {
        Objects.requireNonNull(stateFunction);
        Objects.requireNonNull(state);
        Objects.requireNonNull(range);
        return evaluate(() -> {
            final Evaluated stateEvaluated = state.get();
            final Value value = StateFunction.of(stateFunction.get(), stateEvaluated.value()).evaluate(numericalContext);
            return new Evaluated(value, range.get(),
                    () -> List.of(
                            new Input(
                                    InputName.ofValue(),
                                    stateEvaluated.value(),
                                    stateEvaluated.range())
                    )
            );
        });
    }

    public Evaluated evaluateArgumentStructuredReference(final Supplier<Reference> reference,
                                                         final Supplier<Range> range) {
        Objects.requireNonNull(reference);
        Objects.requireNonNull(range);
        return evaluate(() -> {
            final StructuredReferencesFunction structuredReferencesFunction = new StructuredReferencesFunction(structuredData, reference.get());
            final Value value = structuredReferencesFunction.evaluate(numericalContext);
            return new Evaluated(value, range.get(),
                    () -> List.of(
                            new Input(
                                    InputName.ofStructuredReference(),
                                    structuredReferencesFunction.reference(),
                                    range.get().of(+3, -2)
                            )));
        });
    }

    public Evaluated evaluateArgument(final Supplier<Value> value,
                                      final Supplier<Range> range) {
        Objects.requireNonNull(value);
        Objects.requireNonNull(range);
        return evaluate(
                () -> new Evaluated(value.get(), range.get()));
    }

    private Evaluated evaluate(final Supplier<Evaluated> evaluated) {
        Objects.requireNonNull(evaluated);
        currentPartEvaluationId = currentPartEvaluationId.increment();
        final PartEvaluationId partEvaluationId = currentPartEvaluationId;
        this.partEvaluationCallbackListener.onBeforePartEvaluation(partEvaluationId);
        final Evaluated evaluatedResult = evaluated.get();
        this.partEvaluationCallbackListener.onAfterPartEvaluation(partEvaluationId, evaluatedResult);
        return evaluatedResult;
    }

    public List<IntermediateResult> intermediateResults() {
        return this.partEvaluationCallbackListener.intermediateResults();
    }
}
