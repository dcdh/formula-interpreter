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
    public Result visitArithmeticFunctionsOperatorLeftOpRight(final FormulaParser.ArithmeticFunctionsOperatorLeftOpRightContext ctx) {
        final Result left = this.visit(ctx.left);
        final Result right = this.visit(ctx.right);
        final Result result = compute(ctx.operator, left, right);
        this.result = result;
        return result;
    }

    private Result numericalCompare(final Token token, final Result left, final Result right) {
        final Result result;
        if (left.isUnknown() || right.isUnknown()) {
            result = new UnknownReferenceResult();
        } else if (left.isError() || right.isError()) {
            result = new ErrorResult();
        } else if (!left.isNumeric() || !right.isNumeric()) {
            result = new ErrorResult();
        } else {
            final NumericalComparator numericalComparator;
            switch (token.getType()) {
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
        return result;
    }

    private Result equalityCompare(final Token token, final Result left, final Result right) {
        if (left.isUnknown() || right.isUnknown()) {
            result = new UnknownReferenceResult();
        } else if (left.isError() || right.isError()) {
            result = new ErrorResult();
        } else {
            final EqualityComparator equalityComparator;
            switch (token.getType()) {
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
        return result;
    }

    @Override
    public Result visitComparisonFunctionsNumerical(final FormulaParser.ComparisonFunctionsNumericalContext ctx) {
        final Result left = this.visit(ctx.left);
        final Result right = this.visit(ctx.right);
        final Result result = numericalCompare(ctx.numericalComparator, left, right);
        this.result = result;
        return result;
    }

    @Override
    public Result visitComparisonFunctionsEquality(final FormulaParser.ComparisonFunctionsEqualityContext ctx) {
        final Result left = this.visit(ctx.left);
        final Result right = this.visit(ctx.right);
        final Result result = equalityCompare(ctx.equalityComparator, left, right);
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
