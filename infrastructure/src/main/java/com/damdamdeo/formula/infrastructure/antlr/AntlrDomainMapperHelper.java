package com.damdamdeo.formula.infrastructure.antlr;

import com.damdamdeo.formula.domain.Range;
import org.antlr.v4.runtime.ParserRuleContext;

// an helper ... shame on me
public final class AntlrDomainMapperHelper {
    private AntlrDomainMapperHelper() {
    }

    public static Range toRange(final ParserRuleContext ctx) {
        return new Range(
                ctx.getStart().getStartIndex(),
                ctx.getStop().getStopIndex());
    }

}
