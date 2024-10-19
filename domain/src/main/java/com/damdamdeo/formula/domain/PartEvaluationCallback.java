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
    private Result currentResult = new Result();

    public PartEvaluationCallback(final PartEvaluationCallbackListener partEvaluationCallbackListener,
                                  final NumericalContext numericalContext,
                                  final List<StructuredReference> structuredData) {
        this.partEvaluationCallbackListener = Objects.requireNonNull(partEvaluationCallbackListener);
        this.currentPartEvaluationId = new PartEvaluationId(-1);
        this.numericalContext = Objects.requireNonNull(numericalContext);
        this.structuredData = Objects.requireNonNull(structuredData);
    }

    public void storeCurrentEvaluation(final Result currentResult) {
        this.currentResult = Objects.requireNonNull(currentResult);
    }

    public Result getCurrentEvaluation() {
        return currentResult;
    }

    public Result evaluateArithmeticFunctions(final Supplier<ArithmeticFunction.Function> arithmeticFunction,
                                              final Supplier<Result> left,
                                              final Supplier<Result> right,
                                              final Supplier<Range> range) {
        Objects.requireNonNull(arithmeticFunction);
        Objects.requireNonNull(left);
        Objects.requireNonNull(right);
        Objects.requireNonNull(range);
        return evaluate(() -> {
            final Result leftResult = left.get();
            final Result rightResult = right.get();
            final Value value = ArithmeticFunction.of(arithmeticFunction.get(), leftResult.value(), rightResult.value()).evaluate(numericalContext);
            return new Result(value,
                    List.of(
                            new Input(
                                    InputName.ofLeft(),
                                    leftResult.value(),
                                    leftResult.range()),
                            new Input(
                                    InputName.ofRight(),
                                    rightResult.value(),
                                    rightResult.range())
                    ),
                    range.get()
            );
        });
    }

    public Result evaluateComparisonFunctions(final Supplier<ComparisonFunction.ComparisonType> comparisonFunction,
                                              final Supplier<Result> left,
                                              final Supplier<Result> right,
                                              final Supplier<Range> range) {
        Objects.requireNonNull(comparisonFunction);
        Objects.requireNonNull(left);
        Objects.requireNonNull(right);
        Objects.requireNonNull(range);
        return evaluate(() -> {
            final Result leftResult = left.get();
            final Result rightResult = right.get();
            final Value value = ComparisonFunction.of(comparisonFunction.get(), leftResult.value(), rightResult.value())
                    .evaluate(numericalContext);
            return new Result(value,
                    List.of(
                            new Input(
                                    InputName.ofLeft(),
                                    leftResult.value(),
                                    leftResult.range()),
                            new Input(
                                    InputName.ofRight(),
                                    rightResult.value(),
                                    rightResult.range())
                    ),
                    range.get()
            );
        });
    }

    public Result evaluateLogicalBooleanFunctions(final Supplier<LogicalBooleanFunction.Function> logicalBooleanFunction,
                                                  final Supplier<Result> left,
                                                  final Supplier<Result> right,
                                                  final Supplier<Range> range) {
        Objects.requireNonNull(logicalBooleanFunction);
        Objects.requireNonNull(left);
        Objects.requireNonNull(right);
        Objects.requireNonNull(range);
        return evaluate(() -> {
            final Result leftResult = left.get();
            final Result rightResult = right.get();
            final Value value = LogicalBooleanFunction.of(logicalBooleanFunction.get(), leftResult.value(), rightResult.value()).evaluate(numericalContext);
            return new Result(value,
                    List.of(
                            new Input(
                                    InputName.ofLeft(),
                                    leftResult.value(),
                                    leftResult.range()),
                            new Input(
                                    InputName.ofRight(),
                                    rightResult.value(),
                                    rightResult.range())
                    ),
                    range.get()
            );
        });
    }

    public Result evaluateLogicalComparisonFunctions(final Supplier<LogicalComparisonFunction.Function> logicalComparisonFunction,
                                                     final Supplier<Result> comparison,
                                                     final Supplier<ValueProvider> onTrue,
                                                     final Supplier<ValueProvider> onFalse,
                                                     final Supplier<Range> range) {
        Objects.requireNonNull(logicalComparisonFunction);
        Objects.requireNonNull(comparison);
        Objects.requireNonNull(range);
        return evaluate(() -> {
            final Result comparisonResult = comparison.get();
            final Value value = LogicalComparisonFunction.of(logicalComparisonFunction.get(), comparisonResult.value(),
                            onTrue.get(),
                            onFalse.get())
                    .evaluate(numericalContext);
            return new Result(value,
                    List.of(
                            new Input(
                                    InputName.ofComparisonValue(),
                                    comparisonResult.value(),
                                    comparisonResult.range())
                    ),
                    range.get()
            );
        });
    }

    public Result evaluateStateFunction(final Supplier<StateFunction.Function> stateFunction,
                                        final Supplier<Result> state,
                                        final Supplier<Range> range) {
        Objects.requireNonNull(stateFunction);
        Objects.requireNonNull(state);
        Objects.requireNonNull(range);
        return evaluate(() -> {
            final Result stateResult = state.get();
            final Value value = StateFunction.of(stateFunction.get(), stateResult.value()).evaluate(numericalContext);
            return new Result(value,
                    List.of(
                            new Input(
                                    InputName.ofValue(),
                                    stateResult.value(),
                                    stateResult.range())
                    ),
                    range.get()
            );
        });
    }

    public Result evaluateArgumentStructuredReference(final Supplier<Reference> reference,
                                                      final Supplier<Range> range) {
        Objects.requireNonNull(reference);
        Objects.requireNonNull(range);
        return evaluate(() -> {
            final StructuredReferencesFunction structuredReferencesFunction = new StructuredReferencesFunction(structuredData, reference.get());
            final Value value = structuredReferencesFunction.evaluate(numericalContext);
            final Range rangeReference = range.get();
            return new Result(value,
                    List.of(
                            new Input(
                                    InputName.ofStructuredReference(),
                                    structuredReferencesFunction.reference(),
                                    rangeReference.of(+3, -2)
                            )),
                    rangeReference);
        });
    }

    public Result evaluateArgument(final Supplier<Value> value,
                                   final Supplier<Range> range) {
        Objects.requireNonNull(value);
        Objects.requireNonNull(range);
        return evaluate(() -> new Result(value.get(), range.get()));
    }

    private Result evaluate(final Supplier<Result> supplier) {
        Objects.requireNonNull(supplier);
        currentPartEvaluationId = currentPartEvaluationId.increment();
        final PartEvaluationId partEvaluationId = currentPartEvaluationId;
        this.partEvaluationCallbackListener.onBeforePartEvaluation(partEvaluationId);
        final Result result = supplier.get();
        this.partEvaluationCallbackListener.onAfterPartEvaluation(partEvaluationId, result);
        return result;
    }

    // TODO should not return it here
    public List<IntermediateResult> intermediateResults() {
        return this.partEvaluationCallbackListener.intermediateResults();
    }
}
