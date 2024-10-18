package com.damdamdeo.formula.domain;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public final class PartExecutionCallback {
    private final PartExecutionCallbackListener partExecutionCallbackListener;
    private final AtomicInteger currentExecutionId;
    private final NumericalContext numericalContext;
    private final StructuredReferences structuredReferences;
    private Result currentResult = new Result();

    public PartExecutionCallback(final PartExecutionCallbackListener partExecutionCallbackListener,
                                 final NumericalContext numericalContext,
                                 final StructuredReferences structuredReferences) {
        this.partExecutionCallbackListener = Objects.requireNonNull(partExecutionCallbackListener);
        this.currentExecutionId = new AtomicInteger(-1);
        this.numericalContext = Objects.requireNonNull(numericalContext);
        this.structuredReferences = Objects.requireNonNull(structuredReferences);
    }

    public void storeCurrentResult(final Result currentResult) {
        this.currentResult = Objects.requireNonNull(currentResult);
    }

    public Result getCurrentResult() {
        return currentResult;
    }

    public Result executeArithmeticFunctions(final Supplier<ArithmeticFunction> arithmeticFunction,
                                             final Supplier<Result> left,
                                             final Supplier<Result> right,
                                             final Supplier<Range> range) {
        Objects.requireNonNull(arithmeticFunction);
        Objects.requireNonNull(left);
        Objects.requireNonNull(right);
        Objects.requireNonNull(range);
        return execute(() -> {
            final Result leftResult = left.get();
            final Result rightResult = right.get();
            final Value value = arithmeticFunction.get().execute(leftResult.value(), rightResult.value(), numericalContext);
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

    public Result executeComparisonFunctions(final Supplier<ComparisonFunction> comparisonFunction,
                                             final Supplier<Result> left,
                                             final Supplier<Result> right,
                                             final Supplier<Range> range) {
        Objects.requireNonNull(comparisonFunction);
        Objects.requireNonNull(left);
        Objects.requireNonNull(right);
        Objects.requireNonNull(range);
        return execute(() -> {
            final Result leftResult = left.get();
            final Result rightResult = right.get();
            final Value value = comparisonFunction.get().execute(leftResult.value(), rightResult.value(), numericalContext);
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

    public Result executeLogicalBooleanFunctions(final Supplier<LogicalBooleanFunction> logicalBooleanFunction,
                                                 final Supplier<Result> left,
                                                 final Supplier<Result> right,
                                                 final Supplier<Range> range) {
        Objects.requireNonNull(logicalBooleanFunction);
        Objects.requireNonNull(left);
        Objects.requireNonNull(right);
        Objects.requireNonNull(range);
        return execute(() -> {
            final Result leftResult = left.get();
            final Result rightResult = right.get();
            final Value value = logicalBooleanFunction.get().execute(leftResult.value(), rightResult.value());
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

    public Result executeLogicalComparisonFunctions(final Supplier<LogicalComparisonFunction> logicalComparisonFunction,
                                                    final Supplier<Result> comparison,
                                                    final Supplier<Range> range) {
        Objects.requireNonNull(logicalComparisonFunction);
        Objects.requireNonNull(comparison);
        Objects.requireNonNull(range);
        return execute(() -> {
            final Result comparisonResult = comparison.get();
            final Value value = logicalComparisonFunction.get().execute(comparisonResult.value());
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

    public Result executeInformationFunction(final Supplier<InformationFunction> informationFunction,
                                             final Supplier<Result> information,
                                             final Supplier<Range> range) {
        Objects.requireNonNull(informationFunction);
        Objects.requireNonNull(information);
        Objects.requireNonNull(range);
        return execute(() -> {
            final Result informationResult = information.get();
            final Value value = informationFunction.get().execute(informationResult.value()) ? Value.ofTrue() : Value.ofFalse();
            return new Result(value,
                    List.of(
                            new Input(
                                    InputName.ofValue(),
                                    informationResult.value(),
                                    informationResult.range())
                    ),
                    range.get()
            );
        });
    }

    public Result executeArgumentStructuredReference(final Supplier<Reference> reference,
                                                     final Supplier<Range> range) {
        Objects.requireNonNull(reference);
        Objects.requireNonNull(range);
        return execute(() -> {
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

    public Result executeArgument(final Supplier<Value> value,
                                  final Supplier<Range> range) {
        Objects.requireNonNull(value);
        Objects.requireNonNull(range);
        return execute(() -> new Result(value.get(), range.get()));
    }

    private Result execute(final Supplier<Result> supplier) {
        Objects.requireNonNull(supplier);
        final ExecutionId executionId = new ExecutionId(currentExecutionId);
        this.partExecutionCallbackListener.onBeforeExecution(executionId);
        final Result result = supplier.get();
        this.partExecutionCallbackListener.onAfterExecution(executionId, result);
        return result;
    }

    // TODO should not return it here
    public List<IntermediateResult> intermediateResults() {
        return this.partExecutionCallbackListener.intermediateResults();
    }
}
