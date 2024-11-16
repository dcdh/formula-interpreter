package com.damdamdeo.formula.infrastructure.parser.antlr;

import com.damdamdeo.formula.FormulaLexer;
import com.damdamdeo.formula.FormulaParser;
import com.damdamdeo.formula.domain.*;
import com.damdamdeo.formula.domain.evaluation.Expression;
import com.damdamdeo.formula.domain.spi.EvaluatedAtProvider;
import io.smallrye.mutiny.Uni;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.Objects;

public final class AntlrParserProcessing implements ParserProcessing {
    private final EvaluatedAtProvider evaluatedAtProvider;

    public AntlrParserProcessing(final EvaluatedAtProvider evaluatedAtProvider) {
        this.evaluatedAtProvider = Objects.requireNonNull(evaluatedAtProvider);
    }

    @Override
    public Uni<ProcessingResult> process(final Formula formula) {
        return Uni.createFrom().item(() -> {
            final EvaluatedAtStart evaluatedAtStart = evaluatedAtProvider.now();
            final FormulaLexer lexer = new FormulaLexer(CharStreams.fromString(formula.formula()));
            final AntlrSyntaxErrorListener antlrSyntaxErrorListener = new AntlrSyntaxErrorListener();
            final FormulaParser parser = new FormulaParser(new CommonTokenStream(lexer));
            parser.removeErrorListeners();
            parser.addErrorListener(antlrSyntaxErrorListener);
            final ParseTree tree = parser.program();
//            final DeprecatedAntlrExpressionMapper mapper = new DeprecatedAntlrExpressionMapper();
//            final ParseTreeWalker walker = new ParseTreeWalker();
//            walker.walk(mapper, tree);
//            final Expression result = mapper.expressionResult();
            final AntlrExpressionMapperVisitor antlrExpressionMapperVisitor = new AntlrExpressionMapperVisitor();
            final Expression expression = antlrExpressionMapperVisitor.visit(tree);
            final EvaluatedAtEnd evaluatedAtEnd = evaluatedAtProvider.now();
            return new ProcessingResult(expression, new ParserEvaluationProcessedIn(evaluatedAtStart, evaluatedAtEnd));
        });
    }
}
