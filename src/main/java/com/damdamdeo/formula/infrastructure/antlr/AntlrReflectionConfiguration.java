package com.damdamdeo.formula.infrastructure.antlr;

import com.damdamdeo.formula.FormulaLexer;
import com.damdamdeo.formula.FormulaParser;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection(targets = {FormulaLexer.class, FormulaParser.class})
public class AntlrReflectionConfiguration {
}
