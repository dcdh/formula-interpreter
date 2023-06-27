package com.damdamdeo.formula;

import com.damdamdeo.formula.result.*;
import com.damdamdeo.formula.structuredreference.Reference;
import com.damdamdeo.formula.structuredreference.StructuredData;
import com.damdamdeo.formula.structuredreference.UnknownReferenceException;
import com.damdamdeo.formula.structuredreference.Value;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class EvalVisitor extends FormulaBaseVisitor<Result> {

    private Result result = new VoidResult();

    private final List<MatchedToken> matchedTokens = new ArrayList<>();

    private final StructuredData structuredData;
    private final NumericalContext numericalContext;

    public EvalVisitor(final StructuredData structuredData,
                       final NumericalContext numericalContext) {
        this.structuredData = Objects.requireNonNull(structuredData);
        this.numericalContext = Objects.requireNonNull(numericalContext);
    }

    @Override
    public Result visitExpr(final FormulaParser.ExprContext ctx) {
        appendMatchedToken(ctx);
        return super.visitExpr(ctx);
    }

    private Result compute(final FormulaParser.OperatorContext op, final Result left, final Result right) {
        final Result result;
        if (left.isUnknown() || right.isUnknown()) {
            result = new UnknownReferenceResult();
        } else if (left.isError() || right.isError()) {
            result = new ErrorResult();
        } else if (!left.isNumeric() || !right.isNumeric()) {
            result = new ErrorResult();
        } else {
            final Operator operator;
            if (op.ADD() != null) {
                operator = Operator.ADD;
            } else if (op.SUB() != null) {
                operator = Operator.SUB;
            } else if (op.DIV() != null) {
                operator = Operator.DIV;
            } else if (op.MUL() != null) {
                operator = Operator.MUL;
            } else {
                throw new IllegalStateException("Should not be here");
            }
            final Value value = operator.execute(left.value(), right.value(), numericalContext);
            result = new ValueResult(value);
        }
        return result;
    }

    @Override
    public Result visitOperationsLeftOpRight(final FormulaParser.OperationsLeftOpRightContext ctx) {
        appendMatchedToken(ctx);
        final Result left = this.visit(ctx.left);
        final Result right = this.visit(ctx.right);
        final Result result = compute(ctx.op, left, right);
        this.result = result;
        return result;
    }

    private Result compare(final FormulaParser.ComparatorContext co, final Result left, final Result right) {
        final Result result;
        if (left.isUnknown() || right.isUnknown()) {
            result = new UnknownReferenceResult();
        } else if (left.isError() || right.isError()) {
            result = new ErrorResult();
        } else if (!left.isNumeric() || !right.isNumeric()) {
            result = new ErrorResult();
        } else {
            final Comparator comparator;
            if (co.GT() != null) {
                comparator = Comparator.GT;
            } else if (co.GTE() != null) {
                comparator = Comparator.GTE;
            } else if (co.EQ() != null) {
                comparator = Comparator.EQ;
            } else if (co.LT() != null) {
                comparator = Comparator.LT;
            } else if (co.LTE() != null) {
                comparator = Comparator.LTE;
            } else {
                throw new IllegalStateException("Should not be here");
            }
            final Value value = comparator.execute(left.value(), right.value(), numericalContext);
            result = new ValueResult(value);
        }
        return result;
    }

    @Override
    public Result visitComparatorsLeftCoRight(final FormulaParser.ComparatorsLeftCoRightContext ctx) {
        appendMatchedToken(ctx);
        final Result left = this.visit(ctx.left);
        final Result right = this.visit(ctx.right);
        final Result result = compare(ctx.co, left, right);
        this.result = result;
        return result;
    }

    @Override
    public Result visitStructuredReference(final FormulaParser.StructuredReferenceContext ctx) {
        appendMatchedToken(ctx);
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
    public Result visitVal(final FormulaParser.ValContext ctx) {
        appendMatchedToken(ctx);
        final Result result = new ValueResult(ctx.VALUE().getText());
        this.result = result;
        return result;
    }

    public Result result() {
        return result;
    }

    public List<MatchedToken> matchedTokens() {
        return matchedTokens.stream().distinct().toList();
    }

    void appendMatchedToken(final ParserRuleContext ctx) {
        matchedTokens.add(new MatchedToken(ctx.getText(),
                ctx.getStart().getLine(),
                ctx.getStart().getStartIndex(),
                ctx.getStop().getStopIndex()));
    }
}
