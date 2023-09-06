package com.damdamdeo.formula.infrastructure.antlr;

import com.damdamdeo.formula.FormulaBaseVisitor;
import com.damdamdeo.formula.FormulaParser;
import com.damdamdeo.formula.domain.*;
import com.damdamdeo.formula.domain.spi.ValueProvider;
import org.antlr.v4.runtime.tree.RuleNode;

import java.util.Map;
import java.util.Objects;

public final class EvalVisitor extends FormulaBaseVisitor<Value> {
    private final ExecutionWrapper executionWrapper;
    private final StructuredData structuredData;
    private final NumericalContext numericalContext;
    private Value currentResult;

    public EvalVisitor(final ExecutionWrapper executionWrapper,
                       final StructuredData structuredData,
                       final NumericalContext numericalContext) {
        this.executionWrapper = Objects.requireNonNull(executionWrapper);
        this.structuredData = Objects.requireNonNull(structuredData);
        this.numericalContext = Objects.requireNonNull(numericalContext);
        this.currentResult = Value.ofNotAvailable();
    }

    @Override
    public Value visitChildren(final RuleNode node) {
        final Value value = super.visitChildren(node);
        this.currentResult = value;
        return value;
    }

    @Override
    protected Value defaultResult() {
        return currentResult;
    }

    @Override
    public Value visitArithmetic_functions(FormulaParser.Arithmetic_functionsContext ctx) {
        return executionWrapper.execute(() -> {
            final Value left = this.visit(ctx.left);
            final Value right = this.visit(ctx.right);
            final ArithmeticFunction arithmeticFunction = switch (ctx.function.getType()) {
                case FormulaParser.ADD -> ArithmeticFunction.ofAddition();
                case FormulaParser.SUB -> ArithmeticFunction.ofSubtraction();
                case FormulaParser.DIV -> ArithmeticFunction.ofDivision();
                case FormulaParser.MUL -> ArithmeticFunction.ofMultiplication();
                default -> throw new IllegalStateException("Should not be here");
            };
            final Value result = arithmeticFunction.execute(left, right, numericalContext);
            final Map<InputName, Input> inputs = Map.of(
                    InputName.ofLeft(), left,
                    InputName.ofRight(), right);
            return new ContextualResult(result, inputs,
                    new Position(
                            ctx.getStart().getStartIndex(),
                            ctx.getStop().getStopIndex())
            );
        });
    }

    @Override
    public Value visitComparison_functions(final FormulaParser.Comparison_functionsContext ctx) {
        return executionWrapper.execute(() -> {
            final Value left = this.visit(ctx.left);
            final Value right = this.visit(ctx.right);
            final ComparisonFunction comparisonFunction = switch (ctx.function.getType()) {
                case FormulaParser.EQ -> EqualityComparisonFunction.ofEqual();
                case FormulaParser.NEQ -> EqualityComparisonFunction.ofNotEqual();
                case FormulaParser.GT -> NumericalComparisonFunction.ofGreaterThan();
                case FormulaParser.GTE -> NumericalComparisonFunction.ofGreaterThanOrEqualTo();
                case FormulaParser.LT -> NumericalComparisonFunction.ofLessThan();
                case FormulaParser.LTE -> NumericalComparisonFunction.ofLessThanOrEqualTo();
                default -> throw new IllegalStateException("Should not be here");
            };
            final Value result = comparisonFunction.execute(left, right, numericalContext);
            final Map<InputName, Input> inputs = Map.of(
                    InputName.ofLeft(), left,
                    InputName.ofRight(), right);
            return new ContextualResult(result, inputs,
                    new Position(
                            ctx.getStart().getStartIndex(),
                            ctx.getStop().getStopIndex())
            );
        });
    }

    @Override
    public Value visitLogical_boolean_functions(final FormulaParser.Logical_boolean_functionsContext ctx) {
        return executionWrapper.execute(() -> {
            final Value left = this.visit(ctx.left);
            final Value right = this.visit(ctx.right);
            final LogicalBooleanFunction logicalBooleanFunction = switch (ctx.function.getType()) {
                case FormulaParser.AND -> LogicalBooleanFunction.ofAnd();
                case FormulaParser.OR -> LogicalBooleanFunction.ofOr();
                default -> throw new IllegalStateException("Should not be here");
            };
            final Value result = logicalBooleanFunction.execute(left, right);
            final Map<InputName, Input> inputs = Map.of(
                    InputName.ofLeft(), left,
                    InputName.ofRight(), right);
            return new ContextualResult(result, inputs,
                    new Position(
                            ctx.getStart().getStartIndex(),
                            ctx.getStop().getStopIndex())
            );
        });
    }

    @Override
    public Value visitLogical_comparison_functions(final FormulaParser.Logical_comparison_functionsContext ctx) {
        return executionWrapper.execute(() -> {
            final Value comparisonValue = this.visit(ctx.comparison);
            final ValueProvider onTrue = () -> this.visit(ctx.whenTrue);
            final ValueProvider onFalse = () -> this.visit(ctx.whenFalse);
            final LogicalComparisonFunction logicalComparisonFunction = switch (ctx.function.getType()) {
                case FormulaParser.IF -> LogicalComparisonFunction.ofIf(onTrue, onFalse);
                case FormulaParser.IFERROR -> LogicalComparisonFunction.ofIfError(onTrue, onFalse);
                case FormulaParser.IFNA -> LogicalComparisonFunction.ofIfNotAvailable(onTrue, onFalse);
                default -> throw new IllegalStateException("Should not be here");
            };
            final Value result = logicalComparisonFunction.execute(comparisonValue);
            final Map<InputName, Input> inputs = Map.of(
                    InputName.ofComparisonValue(), comparisonValue);
            return new ContextualResult(result, inputs,
                    new Position(
                            ctx.getStart().getStartIndex(),
                            ctx.getStop().getStopIndex())
            );
        });
    }

    @Override
    public Value visitInformation_functions(final FormulaParser.Information_functionsContext ctx) {
        return executionWrapper.execute(() -> {
            final Value value = this.visit(ctx.value);
            final InformationFunction informationFunction = switch (ctx.function.getType()) {
                case FormulaParser.ISNA -> InformationFunction.ofIsNotAvailable();
                case FormulaParser.ISERROR -> InformationFunction.ofIsError();
                case FormulaParser.ISNUM -> InformationFunction.ofIsNumeric();
                case FormulaParser.ISTEXT -> InformationFunction.ofIsText();
                case FormulaParser.ISBLANK -> InformationFunction.ofIsBlank();
                case FormulaParser.ISLOGICAL -> InformationFunction.ofIsLogical();
                default -> throw new IllegalStateException("Should not be here");
            };
            final Value result = informationFunction.execute(value) ? Value.ofTrue() : Value.ofFalse();
            final Map<InputName, Input> inputs = Map.of(
                    InputName.ofValue(), value);
            return new ContextualResult(result, inputs,
                    new Position(
                            ctx.getStart().getStartIndex(),
                            ctx.getStop().getStopIndex())
            );
        });
    }

    @Override
    public Value visitArgumentStructuredReference(final FormulaParser.ArgumentStructuredReferenceContext ctx) {
        return executionWrapper.execute(() -> {
            Value result;
            final String reference = ctx.STRUCTURED_REFERENCE().getText()
                    .substring(0, ctx.STRUCTURED_REFERENCE().getText().length() - 2)
                    .substring(3);
            try {
                result = this.structuredData.getValueByReference(new Reference(reference));
            } catch (final UnknownReferenceException unknownReferenceException) {
                result = Value.ofUnknownRef();
            }
            final Map<InputName, Input> inputs = Map.of(
                    InputName.ofStructuredReference(), new Reference(reference));
            return new ContextualResult(result, inputs,
                    new Position(
                            ctx.getStart().getStartIndex(),
                            ctx.getStop().getStopIndex())
            );
        });
    }

    @Override
    public Value visitArgumentValue(final FormulaParser.ArgumentValueContext ctx) {
        return executionWrapper.execute(() -> new ContextualResult(Value.of(ctx.VALUE().getText()),
                new Position(
                        ctx.getStart().getStartIndex(),
                        ctx.getStop().getStopIndex())
        ));
    }

    @Override
    public Value visitArgumentNumeric(final FormulaParser.ArgumentNumericContext ctx) {
        return executionWrapper.execute(() -> new ContextualResult(Value.of(ctx.NUMERIC().getText()),
                new Position(
                        ctx.getStart().getStartIndex(),
                        ctx.getStop().getStopIndex())
        ));
    }

    @Override
    public Value visitArgumentBooleanTrue(final FormulaParser.ArgumentBooleanTrueContext ctx) {
        // Can be TRUE or 1 ... cannot return Value.ofTrue() because 1 will not be a numeric anymore
        return executionWrapper.execute(() -> new ContextualResult(Value.of(ctx.getText()),
                new Position(
                        ctx.getStart().getStartIndex(),
                        ctx.getStop().getStopIndex())
        ));
    }

    @Override
    public Value visitArgumentBooleanFalse(final FormulaParser.ArgumentBooleanFalseContext ctx) {
        // Can be FALSE or 0 ... cannot return Value.ofFalse() because 0 will not be a numeric anymore
        return executionWrapper.execute(() -> new ContextualResult(
                Value.of(ctx.getText()),
                new Position(
                        ctx.getStart().getStartIndex(),
                        ctx.getStop().getStopIndex())
        ));
    }
}
