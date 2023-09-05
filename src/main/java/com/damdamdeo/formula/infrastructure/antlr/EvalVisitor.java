package com.damdamdeo.formula.infrastructure.antlr;

import com.damdamdeo.formula.FormulaBaseVisitor;
import com.damdamdeo.formula.FormulaParser;
import com.damdamdeo.formula.domain.*;
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
    public Value visitArithmeticFunctionsOperatorLeftOpRight(final FormulaParser.ArithmeticFunctionsOperatorLeftOpRightContext ctx) {
        return executionWrapper.execute(() -> {
            final Value left = this.visit(ctx.left);
            final Value right = this.visit(ctx.right);
            final Value result;
            if (left.isNotAvailable() || right.isNotAvailable()) {
                result = Value.ofNotAvailable();
            } else if (left.isUnknownRef() || right.isUnknownRef()) {
                result = Value.ofUnknownRef();
            } else if (left.isNotANumericalValue() || right.isNotANumericalValue()) {
                result = Value.ofNumericalValueExpected();
            } else if (left.isDivByZero() || right.isDivByZero()) {
                result = Value.ofDividedByZero();
            } else if (!left.isNumeric() || !right.isNumeric()) {
                result = Value.ofNumericalValueExpected();
            } else if (right.isNumeric() && right.isZero()) {
                result = Value.ofDividedByZero();
            } else {
                final Operator operator = switch (ctx.operator.getType()) {
                    case FormulaParser.ADD -> Operator.ADD;
                    case FormulaParser.SUB -> Operator.SUB;
                    case FormulaParser.DIV -> Operator.DIV;
                    case FormulaParser.MUL -> Operator.MUL;
                    default -> throw new IllegalStateException("Should not be here");
                };
                result = operator.execute(left, right, numericalContext);
            }
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
    public Value visitComparisonFunctionsNumerical(final FormulaParser.ComparisonFunctionsNumericalContext ctx) {
        return executionWrapper.execute(() -> {
            final Value left = this.visit(ctx.left);
            final Value right = this.visit(ctx.right);
            final Value result;
            if (left.isNotAvailable() || right.isNotAvailable()) {
                result = Value.ofNotAvailable();
            } else if (left.isUnknownRef() || right.isUnknownRef()) {
                result = Value.ofUnknownRef();
            } else if (left.isNotANumericalValue() || right.isNotANumericalValue()) {
                result = Value.ofNumericalValueExpected();
            } else if (left.isDivByZero() || right.isDivByZero()) {
                result = Value.ofDividedByZero();
            } else if (!left.isNumeric() || !right.isNumeric()) {
                result = Value.ofNumericalValueExpected();
            } else {
                final NumericalComparator numericalComparator = switch (ctx.numericalComparator.getType()) {
                    case FormulaParser.GT -> NumericalComparator.GT;
                    case FormulaParser.GTE -> NumericalComparator.GTE;
                    case FormulaParser.LT -> NumericalComparator.LT;
                    case FormulaParser.LTE -> NumericalComparator.LTE;
                    default -> throw new IllegalStateException("Should not be here");
                };
                result = numericalComparator.execute(left, right, numericalContext);
            }
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
    public Value visitComparisonFunctionsEquality(final FormulaParser.ComparisonFunctionsEqualityContext ctx) {
        return executionWrapper.execute(() -> {
            final Value left = this.visit(ctx.left);
            final Value right = this.visit(ctx.right);
            final Value result;
            if (left.isNotAvailable() || right.isNotAvailable()) {
                result = Value.ofNotAvailable();
            } else if (left.isUnknownRef() || right.isUnknownRef()) {
                result = Value.ofUnknownRef();
            } else if (left.isNotANumericalValue() || right.isNotANumericalValue()) {
                result = Value.ofNumericalValueExpected();
            } else if (left.isDivByZero() || right.isDivByZero()) {
                result = Value.ofDividedByZero();
            } else {
                final EqualityComparator equalityComparator = switch (ctx.equalityComparator.getType()) {
                    case FormulaParser.EQ -> EqualityComparator.EQ;
                    case FormulaParser.NEQ -> EqualityComparator.NEQ;
                    default -> throw new IllegalStateException("Should not be here");
                };
                result = equalityComparator.execute(left, right, numericalContext);
            }
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
    public Value visitLogicalOperatorFunction(final FormulaParser.LogicalOperatorFunctionContext ctx) {
        return executionWrapper.execute(() -> {
            final Value left = this.visit(ctx.left);
            final Value right = this.visit(ctx.right);
            final Value result;
            if (left.isNotAvailable() || right.isNotAvailable()) {
                result = Value.ofNotAvailable();
            } else if (left.isUnknownRef() || right.isUnknownRef()) {
                result = Value.ofUnknownRef();
            } else if (left.isNotANumericalValue() || right.isNotANumericalValue()) {
                result = Value.ofNumericalValueExpected();
            } else if (left.isDivByZero() || right.isDivByZero()) {
                result = Value.ofDividedByZero();
            } else {
                final LogicalOperator logicalOperator = switch (ctx.logicalOperator.getType()) {
                    case FormulaParser.AND -> LogicalOperator.AND;
                    case FormulaParser.OR -> LogicalOperator.OR;
                    default -> throw new IllegalStateException("Should not be here");
                };
                result = logicalOperator.execute(left, right);
            }
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
    public Value visitIfFunction(final FormulaParser.IfFunctionContext ctx) {
        return executionWrapper.execute(() -> {
            final Value comparisonValue = this.visit(ctx.comparison);
            final Value result;
            switch (ctx.ifOperator.getType()) {
                case FormulaParser.IF -> {
                    if (comparisonValue.isNotAvailable()) {
                        result = Value.ofNotAvailable();
                    } else if (comparisonValue.isUnknownRef()) {
                        result = Value.ofUnknownRef();
                    } else if (comparisonValue.isNotANumericalValue()) {
                        result = Value.ofNumericalValueExpected();
                    } else if (comparisonValue.isDivByZero()) {
                        result = Value.ofDividedByZero();
                    } else if (comparisonValue.isTrue()) {
                        result = this.visit(ctx.whenTrue);
                    } else if (comparisonValue.isFalse()) {
                        result = this.visit(ctx.whenFalse);
                    } else {
                        throw new IllegalStateException("Should not be here");
                    }
                }
                case FormulaParser.IFERROR -> {
                    if (comparisonValue.isError()) {
                        result = this.visit(ctx.whenTrue);
                    } else {
                        result = this.visit(ctx.whenFalse);
                    }
                }
                case FormulaParser.IFNA -> {
                    if (comparisonValue.isNotAvailable()) {
                        result = this.visit(ctx.whenTrue);
                    } else {
                        result = this.visit(ctx.whenFalse);
                    }
                }
                default -> throw new IllegalStateException("Should not be here");
            }
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
    public Value visitIsFunction(final FormulaParser.IsFunctionContext ctx) {
        return executionWrapper.execute(() -> {
            final Value value = this.visit(ctx.value);
            final Value result = switch (ctx.isOperator.getType()) {
                case FormulaParser.ISNA -> value.isNotAvailable() ? Value.ofTrue() : Value.ofFalse();
                case FormulaParser.ISERROR -> value.isError() ? Value.ofTrue() : Value.ofFalse();
                case FormulaParser.ISNUM -> value.isNumeric() ? Value.ofTrue() : Value.ofFalse();
                case FormulaParser.ISTEXT -> value.isText() ? Value.ofTrue() : Value.ofFalse();
                case FormulaParser.ISBLANK -> value.isBlank() ? Value.ofTrue() : Value.ofFalse();
                case FormulaParser.ISLOGICAL -> value.isLogical() ? Value.ofTrue() : Value.ofFalse();
                default -> throw new IllegalStateException("Should not be here");
            };
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
