package com.damdamdeo.formula;

import com.damdamdeo.formula.result.MatchedToken;
import com.damdamdeo.formula.result.Result;
import com.damdamdeo.formula.result.ValueResult;
import com.damdamdeo.formula.result.VoidResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class EvalVisitor extends FormulaBaseVisitor<Result> {

    private Result result = new VoidResult();

    private final List<MatchedToken> matchedTokens = new ArrayList<>();

    @Override
    public Result visitValueExpr(final FormulaParser.ValueExprContext ctx) {
        this.result = new ValueResult(ctx.VALUE().getText());
        matchedTokens.add(new MatchedToken(ctx.getStart().getText(),
                ctx.getStart().getLine(),
                ctx.getStart().getStartIndex(),
                ctx.getStart().getStopIndex()));
        return null;
    }

    public Result result() {
        return result;
    }

    public List<MatchedToken> matchedTokens() {
        return Collections.unmodifiableList(matchedTokens);
    }
}
