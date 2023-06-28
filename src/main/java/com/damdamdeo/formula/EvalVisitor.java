package com.damdamdeo.formula;

import com.damdamdeo.formula.result.*;
import com.damdamdeo.formula.structuredreference.Reference;
import com.damdamdeo.formula.structuredreference.StructuredData;
import com.damdamdeo.formula.structuredreference.UnknownReferenceException;
import org.antlr.v4.runtime.Token;

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

    private Result compute(final Token token, final Result left, final Result right) {
        final Result result;
        if (left.isUnknown() || right.isUnknown()) {
            result = new UnknownReferenceResult();
        } else if (left.isError() || right.isError()) {
            result = new ErrorResult();
        } else if (!left.isNumeric() || !right.isNumeric()) {
            result = new ErrorResult();
        } else {
            final Operator operator;
            switch(token.getType()) {
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
        return result;
    }

    @Override
    public Result visitOperationsLeftOpRight(final FormulaParser.OperationsLeftOpRightContext ctx) {
        final Result left = this.visit(ctx.left);
        final Result right = this.visit(ctx.right);
        final Result result = compute(ctx.operator, left, right);
        this.result = result;
        return result;
    }

    private Result compare(final Token token, final Result left, final Result right) {
        final Result result;
        if (left.isUnknown() || right.isUnknown()) {
            result = new UnknownReferenceResult();
        } else if (left.isError() || right.isError()) {
            result = new ErrorResult();
        } else if (!left.isNumeric() || !right.isNumeric()) {
            result = new ErrorResult();
        } else {
            final Comparator comparator;
            switch (token.getType()) {
                case FormulaParser.GT:
                    comparator = Comparator.GT;
                    break;
                case FormulaParser.GTE:
                    comparator = Comparator.GTE;
                    break;
                case FormulaParser.EQ:
                    comparator = Comparator.EQ;
                    break;
                case FormulaParser.LT:
                    comparator = Comparator.LT;
                    break;
                case FormulaParser.LTE:
                    comparator = Comparator.LTE;
                    break;
                default:
                    throw new IllegalStateException("Should not be here");
            }
            final Value value = comparator.execute(left.value(), right.value(), numericalContext);
            result = new ValueResult(value);
        }
        return result;
    }

    @Override
    public Result visitComparatorsLeftCoRight(final FormulaParser.ComparatorsLeftCoRightContext ctx) {
        final Result left = this.visit(ctx.left);
        final Result right = this.visit(ctx.right);
        final Result result = compare(ctx.comparator, left, right);
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

    public Result result() {
        return result;
    }

}
