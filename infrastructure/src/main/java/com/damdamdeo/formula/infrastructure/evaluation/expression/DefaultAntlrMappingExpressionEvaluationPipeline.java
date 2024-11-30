package com.damdamdeo.formula.infrastructure.evaluation.expression;

import com.damdamdeo.formula.domain.*;
import com.damdamdeo.formula.domain.spi.EvaluationPipeline;
import com.damdamdeo.formula.infrastructure.parser.antlr.AntlrParseTreeGenerator;
import jakarta.enterprise.context.ApplicationScoped;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.List;
import java.util.Objects;

@ApplicationScoped
public final class DefaultAntlrMappingExpressionEvaluationPipeline implements EvaluationPipeline<DefaultAntlrMappingExpressionLoaded> {

    private final AntlrParseTreeGenerator antlrParseTreeGenerator;

    public DefaultAntlrMappingExpressionEvaluationPipeline(final AntlrParseTreeGenerator antlrParseTreeGenerator) {
        this.antlrParseTreeGenerator = Objects.requireNonNull(antlrParseTreeGenerator);
    }

    @Override
    public DefaultAntlrMappingExpressionLoaded load(final Formula formula) {
        final ParseTree parseTree = antlrParseTreeGenerator.generate(formula);
        final AntlrExpressionMapperVisitor antlrExpressionMapperVisitor = new AntlrExpressionMapperVisitor();
        final Expression expression = antlrExpressionMapperVisitor.visit(parseTree);
        return new DefaultAntlrMappingExpressionLoaded(expression);
    }

    @Override
    public Evaluated evaluate(final DefaultAntlrMappingExpressionLoaded loaded,
                              final PartEvaluationListener partEvaluationListener,
                              final List<StructuredReference> structuredReferences,
                              final NumericalContext numericalContext) {
        final DefaultExpressionVisitor defaultExpressionVisitor = new DefaultExpressionVisitor(numericalContext, structuredReferences, partEvaluationListener);
        return loaded.expression().accept(defaultExpressionVisitor);
    }
}
