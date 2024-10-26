package com.damdamdeo.formula.domain.evaluation;

import com.damdamdeo.formula.domain.*;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public final class Evaluator implements ExpressionVisitor {
    private final NumericalContext numericalContext;
    private final List<StructuredReference> structuredReferences;
    private final PartEvaluationListener partEvaluationListener;
    private PartEvaluationId currentPartEvaluationId;

    public Evaluator(final NumericalContext numericalContext,
                     final List<StructuredReference> structuredReferences,
                     final PartEvaluationListener partEvaluationListener) {
        this.numericalContext = Objects.requireNonNull(numericalContext);
        this.structuredReferences = Objects.requireNonNull(structuredReferences);
        this.currentPartEvaluationId = new PartEvaluationId(-1);
        this.partEvaluationListener = Objects.requireNonNull(partEvaluationListener);
    }

    @Override
    public Evaluated visit(final ArithmeticExpression arithmeticExpression) {
        return evaluate(() -> {
            final Evaluated left = arithmeticExpression.left().accept(this);
            final Evaluated right = arithmeticExpression.right().accept(this);
            final ArithmeticFunction arithmeticFunction = ArithmeticFunction.of(arithmeticExpression.arithmeticFunction(),
                    left.value(), right.value());
            final Value evaluated = arithmeticFunction.evaluate(numericalContext);
            final Range range = arithmeticExpression.range();
            return new Evaluated(
                    evaluated,
                    range,
                    () -> List.of(
                            new Input(
                                    InputName.ofLeft(),
                                    left.value(),
                                    left.range()),
                            new Input(
                                    InputName.ofRight(),
                                    right.value(),
                                    right.range())));
        });
    }

    @Override
    public Evaluated visit(final ComparisonExpression comparisonExpression) {
        return evaluate(() -> {
            final Evaluated left = comparisonExpression.left().accept(this);
            final Evaluated right = comparisonExpression.right().accept(this);
            final ComparisonFunction comparisonFunction = ComparisonFunction.of(comparisonExpression.comparisonFunction(),
                    left.value(), right.value());
            final Value evaluated = comparisonFunction.evaluate(numericalContext);
            final Range range = comparisonExpression.range();
            return new Evaluated(evaluated,
                    range,
                    () -> List.of(
                            new Input(
                                    InputName.ofLeft(),
                                    left.value(),
                                    left.range()),
                            new Input(
                                    InputName.ofRight(),
                                    right.value(),
                                    right.range())
                    )
            );
        });
    }

    @Override
    public Evaluated visit(final LogicalBooleanExpression logicalBooleanExpression) {
        return evaluate(() -> {
            final Evaluated left = logicalBooleanExpression.left().accept(this);
            final Evaluated right = logicalBooleanExpression.right().accept(this);
            final LogicalBooleanFunction logicalBooleanFunction = LogicalBooleanFunction.of(logicalBooleanExpression.logicalBooleanFunction(),
                    left.value(), right.value());
            final Value evaluated = logicalBooleanFunction.evaluate(numericalContext);
            final Range range = logicalBooleanExpression.range();
            return new Evaluated(evaluated,
                    range,
                    () -> List.of(
                            new Input(
                                    InputName.ofLeft(),
                                    left.value(),
                                    left.range()),
                            new Input(
                                    InputName.ofRight(),
                                    right.value(),
                                    right.range())
                    ));
        });
    }

    @Override
    public Evaluated visit(final LogicalComparisonExpression logicalComparisonExpression) {
        return evaluate(() -> {
            final Evaluated comparison = logicalComparisonExpression.comparison().accept(this);
            final Value evaluated = LogicalComparisonFunction.of(logicalComparisonExpression.logicalComparisonFunction(),
                            comparison.value(),
                            () -> logicalComparisonExpression.onTrue().accept(this).value(),
                            () -> logicalComparisonExpression.onFalse().accept(this).value())
                    .evaluate(numericalContext);
            final Range range = logicalComparisonExpression.range();
            return new Evaluated(evaluated,
                    range,
                    () -> List.of(
                            new Input(
                                    InputName.ofComparisonValue(),
                                    comparison.value(),
                                    comparison.range()))
            );
        });
    }

    @Override
    public Evaluated visit(final StateExpression stateExpression) {
        return evaluate(() -> {
            final Evaluated argument = stateExpression.argument().accept(this);
            final Value evaluated = StateFunction.of(stateExpression.stateFunction(), argument.value()).evaluate(numericalContext);
            final Range range = stateExpression.range();
            return new Evaluated(evaluated,
                    range,
                    () -> List.of(
                            new Input(
                                    InputName.ofValue(),
                                    argument.value(),
                                    argument.range()))
            );
        });
    }

    @Override
    public Evaluated visit(final StructuredReferencesExpression structuredReferencesExpression) {
        return evaluate(() -> {
            final Reference reference = structuredReferencesExpression.reference();
            final StructuredReferencesFunction structuredReferencesFunction = new StructuredReferencesFunction(structuredReferences, reference);
            final Value evaluated = structuredReferencesFunction.evaluate(numericalContext);
            final Range range = structuredReferencesExpression.range();
            return new Evaluated(evaluated,
                    range,
                    () -> List.of(
                            new Input(
                                    InputName.ofStructuredReference(),
                                    structuredReferencesFunction.reference(),
                                    range.of(+3, -2)))
            );
        });
    }

    @Override
    public Evaluated visit(final ArgumentExpression argumentExpression) {
        return evaluate(() -> {
            final Value value = argumentExpression.value();
            return new Evaluated(
                    value,
                    argumentExpression.range()
            );
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

    public List<IntermediateResult> intermediateResults() {
        return this.partEvaluationListener.intermediateResults();
    }
}
