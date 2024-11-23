package com.damdamdeo.formula.infrastructure.evaluation.antlr;

import com.damdamdeo.formula.domain.*;
import com.damdamdeo.formula.domain.spi.EvaluationPipeline;
import com.damdamdeo.formula.infrastructure.parser.antlr.AntlrEvalVisitor;
import com.damdamdeo.formula.infrastructure.parser.antlr.AntlrParseTreeGenerator;
import com.damdamdeo.formula.infrastructure.parser.antlr.PartEvaluationCallback;
import jakarta.enterprise.context.ApplicationScoped;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.List;
import java.util.Objects;

@ApplicationScoped
public final class DefaultAntlrEvaluationPipeline implements EvaluationPipeline<DefaultAntlrLoaded> {

    private final AntlrParseTreeGenerator antlrParseTreeGenerator;

    public DefaultAntlrEvaluationPipeline(final AntlrParseTreeGenerator antlrParseTreeGenerator) {
        this.antlrParseTreeGenerator = Objects.requireNonNull(antlrParseTreeGenerator);
    }

    @Override
    public DefaultAntlrLoaded load(final Formula formula) {
        final ParseTree parseTree = antlrParseTreeGenerator.generate(formula);
        return new DefaultAntlrLoaded(parseTree);
    }

    @Override
    public Evaluated evaluate(final DefaultAntlrLoaded loaded,
                              final PartEvaluationListener partEvaluationListener,
                              final List<StructuredReference> structuredReferences,
                              final NumericalContext numericalContext) {
        final PartEvaluationCallback partEvaluationCallback = new PartEvaluationCallback(
                partEvaluationListener, numericalContext, structuredReferences);
        final AntlrEvalVisitor antlrEvalVisitor = new AntlrEvalVisitor(partEvaluationCallback);
        return antlrEvalVisitor.visit(loaded.parseTree());
    }
}
