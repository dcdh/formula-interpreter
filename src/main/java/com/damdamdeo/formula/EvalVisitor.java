package com.damdamdeo.formula;

import com.damdamdeo.formula.result.*;
import com.damdamdeo.formula.structuredreference.Reference;
import com.damdamdeo.formula.structuredreference.StructuredData;
import com.damdamdeo.formula.structuredreference.UnknownReferenceException;
import com.damdamdeo.formula.structuredreference.Value;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class EvalVisitor extends FormulaBaseVisitor<Result> {

    private Result result = new VoidResult();

    private final List<MatchedToken> matchedTokens = new ArrayList<>();

    private final StructuredData structuredData;

    public EvalVisitor(final StructuredData structuredData) {
        this.structuredData = Objects.requireNonNull(structuredData);
    }

    @Override
    public Result visitStructuredReferenceExpr(final FormulaParser.StructuredReferenceExprContext ctx) {
        appendMatchedToken(ctx);
        try {
            final String reference = ctx.STRUCTURED_REFERENCE().getText()
                    .substring(0, ctx.STRUCTURED_REFERENCE().getText().length() - 2)
                    .substring(3);
            final Value value = this.structuredData.getValueByReference(new Reference(reference));
            this.result = new ValueResult(value);
        } catch (final UnknownReferenceException unknownReferenceException) {
            this.result = new UnknownReferenceResult();
        }
        return null;
    }

    @Override
    public Result visitValueExpr(final FormulaParser.ValueExprContext ctx) {
        appendMatchedToken(ctx);
        this.result = new ValueResult(ctx.VALUE().getText());
        return null;
    }

    public Result result() {
        return result;
    }

    public List<MatchedToken> matchedTokens() {
        return Collections.unmodifiableList(matchedTokens);
    }

    void appendMatchedToken(final FormulaParser.ExprContext ctx) {
        matchedTokens.add(new MatchedToken(ctx.getStart().getText(),
                ctx.getStart().getLine(),
                ctx.getStart().getStartIndex(),
                ctx.getStart().getStopIndex()));
    }
}
