package com.damdamdeo.formula.infrastructure.antlr;

import com.damdamdeo.formula.FormulaLexer;
import com.damdamdeo.formula.FormulaParser;
import com.damdamdeo.formula.domain.ExecutedAtEnd;
import com.damdamdeo.formula.domain.ExecutedAtStart;
import com.damdamdeo.formula.domain.Formula;
import com.damdamdeo.formula.domain.ParserExecutionProcessedIn;
import com.damdamdeo.formula.domain.spi.ExecutedAtProvider;
import io.smallrye.mutiny.Uni;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.Objects;

public final class DefaultAntlrParseTreeGenerator implements AntlrParseTreeGenerator {
    private final ExecutedAtProvider executedAtProvider;

    public DefaultAntlrParseTreeGenerator(final ExecutedAtProvider executedAtProvider) {
        this.executedAtProvider = Objects.requireNonNull(executedAtProvider);
    }

    @Override
    public Uni<GeneratorResult> generate(final Formula formula) {
        return Uni.createFrom().item(() -> {
            final ExecutedAtStart executedAtStart = executedAtProvider.now();
            final FormulaLexer lexer = new FormulaLexer(CharStreams.fromString(formula.formula()));
            final AntlrSyntaxErrorListener antlrSyntaxErrorListener = new AntlrSyntaxErrorListener();
            final FormulaParser parser = new FormulaParser(new CommonTokenStream(lexer));
            parser.removeErrorListeners();
            parser.addErrorListener(antlrSyntaxErrorListener);
            final ParseTree tree = parser.program();
            final ExecutedAtEnd executedAtEnd = executedAtProvider.now();
            return new GeneratorResult(formula, tree, antlrSyntaxErrorListener,
                    new ParserExecutionProcessedIn(
                            executedAtStart, executedAtEnd
                    ));
        });
    }
}
