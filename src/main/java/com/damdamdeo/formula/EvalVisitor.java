package com.damdamdeo.formula;

import com.damdamdeo.formula.structuredreference.Reference;
import com.damdamdeo.formula.structuredreference.StructuredData;
import com.damdamdeo.formula.structuredreference.UnknownReferenceException;

import java.util.Objects;

public final class EvalVisitor extends FormulaBaseVisitor<Value> {

    private Value result = Value.of();

    private final StructuredData structuredData;
    private final NumericalContext numericalContext;

    public EvalVisitor(final StructuredData structuredData,
                       final NumericalContext numericalContext) {
        this.structuredData = Objects.requireNonNull(structuredData);
        this.numericalContext = Objects.requireNonNull(numericalContext);
    }

    @Override
    public Value visitExpr(final FormulaParser.ExprContext ctx) {
        return super.visitExpr(ctx);
    }

    @Override
    public Value visitArithmeticFunctionsOperatorLeftOpRight(final FormulaParser.ArithmeticFunctionsOperatorLeftOpRightContext ctx) {
        final Value left = this.visit(ctx.left);
        final Value right = this.visit(ctx.right);
        final Value result;
        if (left.isUnknown() || right.isUnknown()) {
            result = Value.ofUnknown();
        } else if (left.isError() || right.isError()) {
            result = Value.ofError();
        } else if (!left.isNumeric() || !right.isNumeric()) {
            result = Value.ofError();
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
        this.result = result;
        return result;
    }

    @Override
    public Value visitComparisonFunctionsNumerical(final FormulaParser.ComparisonFunctionsNumericalContext ctx) {
        final Value left = this.visit(ctx.left);
        final Value right = this.visit(ctx.right);
        final Value result;
        if (left.isUnknown() || right.isUnknown()) {
            result = Value.ofUnknown();
        } else if (left.isError() || right.isError()) {
            result = Value.ofError();
        } else if (!left.isNumeric() || !right.isNumeric()) {
            result = Value.ofError();
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
        this.result = result;
        return result;
    }

    @Override
    public Value visitComparisonFunctionsEquality(final FormulaParser.ComparisonFunctionsEqualityContext ctx) {
        final Value left = this.visit(ctx.left);
        final Value right = this.visit(ctx.right);
        final Value result;
        if (left.isUnknown() || right.isUnknown()) {
            result = Value.ofUnknown();
        } else if (left.isError() || right.isError()) {
            result = Value.ofError();
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
        this.result = result;
        return result;
    }

    @Override
    public Value visitLogicalOperatorFunction(final FormulaParser.LogicalOperatorFunctionContext ctx) {
        final Value left = this.visit(ctx.left);
        final Value right = this.visit(ctx.right);
        final Value result;
        if (left.isUnknown() || right.isUnknown()) {
            result = Value.ofUnknown();
        } else if (left.isError() || right.isError()) {
            result = Value.ofError();
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
        this.result = result;
        return result;
    }

    @Override
    public Value visitIfFunction(final FormulaParser.IfFunctionContext ctx) {
        final Value comparaisonValue = this.visit(ctx.comparison);
        final Value result;
        if (comparaisonValue.isError()) {
            result = Value.ofError();
        } else if (comparaisonValue.isUnknown()) {
            result = Value.ofUnknown();
        } else if (comparaisonValue.isTrue()) {
            result = this.visit(ctx.whenTrue);
        } else if (comparaisonValue.isFalse()) {
            result = this.visit(ctx.whenFalse);
        } else {
            result = Value.ofError();
        }
        this.result = result;
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
            result = Value.ofUnknown();
        }
        this.result = result;
        return result;
    }

    @Override
    public Value visitArgumentValue(final FormulaParser.ArgumentValueContext ctx) {
        final Value result = Value.of(ctx.VALUE().getText());
        this.result = result;
        return result;
    }

    @Override
    public Value visitArgumentNumeric(final FormulaParser.ArgumentNumericContext ctx) {
        final Value result = Value.of(ctx.NUMERIC().getText());
        this.result = result;
        return result;
    }

    public Value result() {
        return result;
    }

}
