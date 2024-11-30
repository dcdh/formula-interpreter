package com.damdamdeo.formula.infrastructure.evaluation.antlr;

import com.damdamdeo.formula.domain.evaluation.AntlrLoaded;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.Objects;

public record ParseTreeAntlrLoaded(ParseTree parseTree) implements AntlrLoaded {
    public ParseTreeAntlrLoaded {
        Objects.requireNonNull(parseTree);
    }
}
