package com.damdamdeo.formula;

import com.damdamdeo.formula.result.*;
import com.damdamdeo.formula.structuredreference.Reference;
import com.damdamdeo.formula.structuredreference.StructuredData;
import com.damdamdeo.formula.structuredreference.UnknownReferenceException;

import java.util.Objects;

public final class EvalVisitor extends FormulaBaseVisitor<Result> {

    private Result result = new VoidResult();

    private final StructuredData structuredData;
    private final NumericalContext numericalContext;

    public EvalVisitor(final StructuredData structuredData,
                       final NumericalContext numericalContext) {
        this.structuredData = Objects.requireNonNull(structuredData);
        this.numericalContext = Objects.requireNonNull(numericalContext);
    }

    @Override
    public Result visitExpr(final FormulaParser.ExprContext ctx) {
        return super.visitExpr(ctx);
    }

    @Override
    public Result visitArithmeticFunctionsOperatorLeftOpRight(final FormulaParser.ArithmeticFunctionsOperatorLeftOpRightContext ctx) {
        final Result left = this.visit(ctx.left);
        final Result right = this.visit(ctx.right);
        final Result result;
        if (left.isUnknown() || right.isUnknown()) {
            result = new UnknownReferenceResult();
        } else if (left.isError() || right.isError()) {
            result = new ErrorResult();
        } else if (!left.isNumeric() || !right.isNumeric()) {
            result = new ErrorResult();
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
            final Value value = operator.execute(left.value(), right.value(), numericalContext);
            result = new ValueResult(value);
        }
        this.result = result;
        return result;
    }

    @Override
    public Result visitComparisonFunctionsNumerical(final FormulaParser.ComparisonFunctionsNumericalContext ctx) {
        final Result left = this.visit(ctx.left);
        final Result right = this.visit(ctx.right);
        final Result result;
        if (left.isUnknown() || right.isUnknown()) {
            result = new UnknownReferenceResult();
        } else if (left.isError() || right.isError()) {
            result = new ErrorResult();
        } else if (!left.isNumeric() || !right.isNumeric()) {
            result = new ErrorResult();
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
            final Value value = numericalComparator.execute(left.value(), right.value(), numericalContext);
            result = new ValueResult(value);
        }
        this.result = result;
        return result;
    }

    @Override
    public Result visitComparisonFunctionsEquality(final FormulaParser.ComparisonFunctionsEqualityContext ctx) {
        final Result left = this.visit(ctx.left);
        final Result right = this.visit(ctx.right);
        final Result result;
        if (left.isUnknown() || right.isUnknown()) {
            result = new UnknownReferenceResult();
        } else if (left.isError() || right.isError()) {
            result = new ErrorResult();
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
            final Value value = equalityComparator.execute(left.value(), right.value(), numericalContext);
            result = new ValueResult(value);
        }
        this.result = result;
        return result;
    }

    @Override
    public Result visitLogicalOperatorFunction(final FormulaParser.LogicalOperatorFunctionContext ctx) {
        final Result left = this.visit(ctx.left);
        final Result right = this.visit(ctx.right);
        final Result result;
        if (left.isUnknown() || right.isUnknown()) {
            result = new UnknownReferenceResult();
        } else if (left.isError() || right.isError()) {
            result = new ErrorResult();
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
            final Value value = logicalOperator.execute(left.value(), right.value());
            result = new ValueResult(value);
        }
        this.result = result;
        return result;
    }

    @Override
    public Result visitArgumentStructuredReference(final FormulaParser.ArgumentStructuredReferenceContext ctx) {
        Result result;
        try {
            final String reference = ctx.STRUCTURED_REFERENCE().getText()
                    .substring(0, ctx.STRUCTURED_REFERENCE().getText().length() - 2)
                    .substring(3);
            final Value value = this.structuredData.getValueByReference(new Reference(reference));
            result = new ValueResult(value);
        } catch (final UnknownReferenceException unknownReferenceException) {
            result = new UnknownReferenceResult();
        }
        this.result = result;
        return result;
    }

    @Override
    public Result visitArgumentValue(final FormulaParser.ArgumentValueContext ctx) {
        final Result result = new ValueResult(ctx.VALUE().getText());
        this.result = result;
        return result;
    }

    @Override
    public Result visitArgumentNumeric(final FormulaParser.ArgumentNumericContext ctx) {
        final Result result = new ValueResult(ctx.NUMERIC().getText());
        this.result = result;
        return result;
    }

    public Result result() {
        return result;
    }

}
