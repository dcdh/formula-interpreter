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

    @Override
    public Result visitAddArithmeticOperationsExpr(final FormulaParser.AddArithmeticOperationsExprContext ctx) {
        appendMatchedToken(ctx);
        return super.visitAddArithmeticOperationsExpr(ctx);
    }

    private Result computeAddOperation(final Result left, final Result right) {
        final Result result;
        if (left.isUnknown() || right.isUnknown()) {
            result = new UnknownReferenceResult();
        } else if (left.isError() || right.isError()) {
            result = new ErrorResult();
        } else if (!left.isNumeric() || !right.isNumeric()) {
            result = new ErrorResult();
        } else {
            final Value value = left.value().add(right.value(), numericalContext);
            result = new ValueResult(value);
        }
        return result;
    }

    @Override
    public Result visitStructuredReferenceAddStructuredReference(final FormulaParser.StructuredReferenceAddStructuredReferenceContext ctx) {
        appendMatchedToken(ctx);
        final Result left = this.visit(ctx.structured_reference_expr(0));
        final Result right = this.visit(ctx.structured_reference_expr(1));
        final Result result = computeAddOperation(left, right);
        this.result = result;
        return result;
    }

    @Override
    public Result visitStructuredReferenceAddValue(final FormulaParser.StructuredReferenceAddValueContext ctx) {
        appendMatchedToken(ctx);
        final Result left = this.visit(ctx.structured_reference_expr());
        final Result right = this.visit(ctx.value_expr());
        final Result result = computeAddOperation(left, right);
        this.result = result;
        return result;
    }

    @Override
    public Result visitValueAddStructuredReference(final FormulaParser.ValueAddStructuredReferenceContext ctx) {
        appendMatchedToken(ctx);
        final Result left = this.visit(ctx.value_expr());
        final Result right = this.visit(ctx.structured_reference_expr());
        final Result result = computeAddOperation(left, right);
        this.result = result;
        return result;
    }

    @Override
    public Result visitValueAddValue(final FormulaParser.ValueAddValueContext ctx) {
        appendMatchedToken(ctx);
        final Result left = this.visit(ctx.value_expr(0));
        final Result right = this.visit(ctx.value_expr(1));
        final Result result = computeAddOperation(left, right);
        this.result = result;
        return result;
    }

    @Override
    public Result visitStructuredReferenceExpr(final FormulaParser.StructuredReferenceExprContext ctx) {
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
    public Result visitValueExpr(final FormulaParser.ValueExprContext ctx) {
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
