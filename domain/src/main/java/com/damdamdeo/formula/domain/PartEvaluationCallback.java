package com.damdamdeo.formula.domain;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public final class PartEvaluationCallback {
    private final PartEvaluationCallbackListener partEvaluationCallbackListener;
    private PartEvaluationId currentPartEvaluationId;
    private final NumericalContext numericalContext;
    private final StructuredReferences structuredReferences;
    private Result currentResult = new Result();

    public PartEvaluationCallback(final PartEvaluationCallbackListener partEvaluationCallbackListener,
                                  final NumericalContext numericalContext,
                                  final StructuredReferences structuredReferences) {
        this.partEvaluationCallbackListener = Objects.requireNonNull(partEvaluationCallbackListener);
        this.currentPartEvaluationId = new PartEvaluationId(-1);
        this.numericalContext = Objects.requireNonNull(numericalContext);
        this.structuredReferences = Objects.requireNonNull(structuredReferences);
    }

    public void storeCurrentResult(final Result currentResult) {
        this.currentResult = Objects.requireNonNull(currentResult);
    }

    public Result getCurrentResult() {
        return currentResult;
    }

    public Result evaluateArithmeticFunctions(final Supplier<ArithmeticFunction> arithmeticFunction,
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
            final Value value = arithmeticFunction.get().evaluate(leftResult.value(), rightResult.value(), numericalContext);
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

    public Result evaluateComparisonFunctions(final Supplier<ComparisonFunction> comparisonFunction,
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
            final Value value = comparisonFunction.get().evaluate(leftResult.value(), rightResult.value(), numericalContext);
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

    public Result evaluateLogicalBooleanFunctions(final Supplier<LogicalBooleanFunction> logicalBooleanFunction,
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
            final Value value = logicalBooleanFunction.get().evaluate(leftResult.value(), rightResult.value());
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

    public Result evaluateLogicalComparisonFunctions(final Supplier<LogicalComparisonFunction> logicalComparisonFunction,
                                                     final Supplier<Result> comparison,
                                                     final Supplier<Range> range) {
        Objects.requireNonNull(logicalComparisonFunction);
        Objects.requireNonNull(comparison);
        Objects.requireNonNull(range);
        return evaluate(() -> {
            final Result comparisonResult = comparison.get();
            final Value value = logicalComparisonFunction.get().evaluate(comparisonResult.value());
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

    public Result evaluateStateFunction(final Supplier<StateFunction> stateFunction,
                                        final Supplier<Result> state,
                                        final Supplier<Range> range) {
        Objects.requireNonNull(stateFunction);
        Objects.requireNonNull(state);
        Objects.requireNonNull(range);
        return evaluate(() -> {
            final Result stateResult = state.get();
            final Value value = stateFunction.get().evaluate(stateResult.value()) ? Value.ofTrue() : Value.ofFalse();
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
            final Reference referenceResult = reference.get();
            final Range rangeReference = range.get();
            Value value;
            try {
                value = this.structuredReferences.getValueByReference(referenceResult);
            } catch (final UnknownReferenceException unknownReferenceException) {
                value = Value.ofUnknownRef();
            }
            return new Result(value,
                    List.of(
                            new Input(
                                    InputName.ofStructuredReference(),
                                    referenceResult,
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
