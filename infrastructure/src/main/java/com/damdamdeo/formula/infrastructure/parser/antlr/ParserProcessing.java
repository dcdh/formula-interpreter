package com.damdamdeo.formula.infrastructure.parser.antlr;

import com.damdamdeo.formula.domain.Formula;
import com.damdamdeo.formula.domain.ProcessingResult;
import io.smallrye.mutiny.Uni;

public interface ParserProcessing {
    Uni<ProcessingResult> process(final Formula formula);
}
