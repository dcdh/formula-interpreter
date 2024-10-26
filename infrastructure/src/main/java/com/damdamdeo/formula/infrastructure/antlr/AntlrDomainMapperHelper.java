package com.damdamdeo.formula.infrastructure.antlr;

import com.damdamdeo.formula.domain.PositionedAt;
import org.antlr.v4.runtime.ParserRuleContext;

// an helper ... shame on me
public final class AntlrDomainMapperHelper {
    private AntlrDomainMapperHelper() {
    }

    public static PositionedAt toPositionedAt(final ParserRuleContext ctx) {
        return new PositionedAt(
                ctx.getStart().getStartIndex(),
                ctx.getStop().getStopIndex());
    }

}
