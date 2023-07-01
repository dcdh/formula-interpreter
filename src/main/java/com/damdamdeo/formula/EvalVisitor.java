package com.damdamdeo.formula;

import com.damdamdeo.formula.structuredreference.Reference;
import com.damdamdeo.formula.structuredreference.StructuredData;
import com.damdamdeo.formula.structuredreference.UnknownReferenceException;
import org.antlr.v4.runtime.tree.RuleNode;

import java.util.Objects;

public final class EvalVisitor extends FormulaBaseVisitor<Value> {
    private final StructuredData structuredData;
    private final NumericalContext numericalContext;

    private Value currentResult = Value.ofNotAvailable();

    public EvalVisitor(final StructuredData structuredData,
                       final NumericalContext numericalContext) {
        this.structuredData = Objects.requireNonNull(structuredData);
        this.numericalContext = Objects.requireNonNull(numericalContext);
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
            final Operator operator;
            switch (ctx.operator.getType()) {
                case FormulaParser.ADD:
                    operator = Operator.ADD;
                    break;
                case FormulaParser.SUB:
                    operator = Operator.SUB;
                    break;
                case FormulaParser.DIV:
                    operator = Operator.DIV;
                    break;
                case FormulaParser.MUL:
                    operator = Operator.MUL;
                    break;
                default:
                    throw new IllegalStateException("Should not be here");
            }
            result = operator.execute(left, right, numericalContext);
        }
        return result;
    }

    @Override
    public Value visitComparisonFunctionsNumerical(final FormulaParser.ComparisonFunctionsNumericalContext ctx) {
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
            final NumericalComparator numericalComparator;
            switch (ctx.numericalComparator.getType()) {
                case FormulaParser.GT:
                    numericalComparator = NumericalComparator.GT;
                    break;
                case FormulaParser.GTE:
                    numericalComparator = NumericalComparator.GTE;
                    break;
                case FormulaParser.LT:
                    numericalComparator = NumericalComparator.LT;
                    break;
                case FormulaParser.LTE:
                    numericalComparator = NumericalComparator.LTE;
                    break;
                default:
                    throw new IllegalStateException("Should not be here");
            }
            result = numericalComparator.execute(left, right, numericalContext);
        }
        return result;
    }

    @Override
    public Value visitComparisonFunctionsEquality(final FormulaParser.ComparisonFunctionsEqualityContext ctx) {
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
            final EqualityComparator equalityComparator;
            switch (ctx.equalityComparator.getType()) {
                case FormulaParser.EQ:
                    equalityComparator = EqualityComparator.EQ;
                    break;
                case FormulaParser.NEQ:
                    equalityComparator = EqualityComparator.NEQ;
                    break;
                default:
                    throw new IllegalStateException("Should not be here");
            }
            result = equalityComparator.execute(left, right, numericalContext);
        }
        return result;
    }

    @Override
    public Value visitLogicalOperatorFunction(final FormulaParser.LogicalOperatorFunctionContext ctx) {
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
            final LogicalOperator logicalOperator;
            switch (ctx.logicalOperator.getType()) {
                case FormulaParser.AND:
                    logicalOperator = LogicalOperator.AND;
                    break;
                case FormulaParser.OR:
                    logicalOperator = LogicalOperator.OR;
                    break;
                default:
                    throw new IllegalStateException("Should not be here");
            }
            result = logicalOperator.execute(left, right);
        }
        return result;
    }

    @Override
    public Value visitIfFunction(final FormulaParser.IfFunctionContext ctx) {
        final Value comparaisonValue = this.visit(ctx.comparison);
        final Value result;
        if (comparaisonValue.isNotAvailable()) {
            result = Value.ofNotAvailable();
        } else if (comparaisonValue.isUnknownRef()) {
            result = Value.ofUnknownRef();
        } else if (comparaisonValue.isNotANumericalValue()) {
            result = Value.ofNumericalValueExpected();
        } else if (comparaisonValue.isDivByZero()) {
            result = Value.ofDividedByZero();
        } else if (comparaisonValue.isTrue()) {
            result = this.visit(ctx.whenTrue);
        } else if (comparaisonValue.isFalse()) {
            result = this.visit(ctx.whenFalse);
        } else {
            result = Value.ofNumericalValueExpected();
        }
        return result;
    }

    @Override
    public Value visitIsNumericFunction(final FormulaParser.IsNumericFunctionContext ctx) {
        final Value value = this.visit(ctx.value);
        final Value result;
        if (value.isNotAvailable()) {
            result = Value.ofNotAvailable();
        } else if (value.isUnknownRef()) {
            result = Value.ofUnknownRef();
        } else if (value.isNotANumericalValue()) {
            result = Value.ofNumericalValueExpected();
        } else if (value.isDivByZero()) {
            result = Value.ofDividedByZero();
        } else {
            result = value.isNumeric() ? Value.ofTrue() : Value.ofFalse();
        }
        return result;
    }

    @Override
    public Value visitIsTextFunction(FormulaParser.IsTextFunctionContext ctx) {
        final Value value = this.visit(ctx.value);
        final Value result;
        if (value.isNotAvailable()) {
            result = Value.ofNotAvailable();
        } else if (value.isUnknownRef()) {
            result = Value.ofUnknownRef();
        } else if (value.isNotANumericalValue()) {
            result = Value.ofNumericalValueExpected();
        } else if (value.isDivByZero()) {
            result = Value.ofDividedByZero();
        } else {
            result = value.isText() ? Value.ofTrue() : Value.ofFalse();
        }
        return result;
    }

    @Override
    public Value visitArgumentStructuredReference(final FormulaParser.ArgumentStructuredReferenceContext ctx) {
        Value result;
        try {
            final String reference = ctx.STRUCTURED_REFERENCE().getText()
                    .substring(0, ctx.STRUCTURED_REFERENCE().getText().length() - 2)
                    .substring(3);
            result = this.structuredData.getValueByReference(new Reference(reference));
        } catch (final UnknownReferenceException unknownReferenceException) {
            result = Value.ofUnknownRef();
        }
        return result;
    }

    @Override
    public Value visitArgumentValue(final FormulaParser.ArgumentValueContext ctx) {
        return Value.of(ctx.VALUE().getText());
    }

    @Override
    public Value visitArgumentNumeric(final FormulaParser.ArgumentNumericContext ctx) {
        return Value.of(ctx.NUMERIC().getText());
    }

    @Override
    public Value visitArgumentBooleanTrue(final FormulaParser.ArgumentBooleanTrueContext ctx) {
        // Can be TRUE or 1 ... cannot return Value.ofTrue() because 1 will not be a numeric anymore
        return Value.of(ctx.getText());
    }

    @Override
    public Value visitArgumentBooleanFalse(final FormulaParser.ArgumentBooleanFalseContext ctx) {
        // Can be FALSE or 0 ... cannot return Value.ofFalse() because 0 will not be a numeric anymore
        return Value.of(ctx.getText());
    }
}
