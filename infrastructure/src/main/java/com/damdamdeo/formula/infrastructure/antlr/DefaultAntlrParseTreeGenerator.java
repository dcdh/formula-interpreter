package com.damdamdeo.formula.infrastructure.antlr;

import com.damdamdeo.formula.FormulaLexer;
import com.damdamdeo.formula.FormulaParser;
import com.damdamdeo.formula.domain.EvaluatedAtEnd;
import com.damdamdeo.formula.domain.EvaluatedAtStart;
import com.damdamdeo.formula.domain.Formula;
import com.damdamdeo.formula.domain.ParserEvaluationProcessedIn;
import com.damdamdeo.formula.domain.spi.EvaluatedAtProvider;
import io.smallrye.mutiny.Uni;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.Objects;

public final class DefaultAntlrParseTreeGenerator implements AntlrParseTreeGenerator {
    private final EvaluatedAtProvider evaluatedAtProvider;

    public DefaultAntlrParseTreeGenerator(final EvaluatedAtProvider evaluatedAtProvider) {
        this.evaluatedAtProvider = Objects.requireNonNull(evaluatedAtProvider);
    }

    @Override
    public Uni<GeneratorResult> generate(final Formula formula) {
        return Uni.createFrom().item(() -> {
            final EvaluatedAtStart evaluatedAtStart = evaluatedAtProvider.now();
            final FormulaLexer lexer = new FormulaLexer(CharStreams.fromString(formula.formula()));
            final AntlrSyntaxErrorListener antlrSyntaxErrorListener = new AntlrSyntaxErrorListener();
            final FormulaParser parser = new FormulaParser(new CommonTokenStream(lexer));
            parser.removeErrorListeners();
            parser.addErrorListener(antlrSyntaxErrorListener);
            final ParseTree tree = parser.program();
            final EvaluatedAtEnd evaluatedAtEnd = evaluatedAtProvider.now();
            return new GeneratorResult(formula, tree, antlrSyntaxErrorListener,
                    new ParserEvaluationProcessedIn(
                            evaluatedAtStart, evaluatedAtEnd
                    ));
        });
    }
}
