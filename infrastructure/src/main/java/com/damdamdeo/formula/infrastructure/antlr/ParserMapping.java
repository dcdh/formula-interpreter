package com.damdamdeo.formula.infrastructure.antlr;

import com.damdamdeo.formula.domain.Formula;
import com.damdamdeo.formula.domain.MappingResult;
import io.smallrye.mutiny.Uni;

public interface ParserMapping {
    Uni<MappingResult> map(final Formula formula);
}
