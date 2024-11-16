package com.damdamdeo.formula.infrastructure.parser.antlr;

import com.damdamdeo.formula.FormulaBaseVisitor;
import com.damdamdeo.formula.FormulaParser;
import com.damdamdeo.formula.domain.*;
import com.damdamdeo.formula.domain.spi.ValueProvider;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;

import java.util.Objects;

public final class AntlrEvalVisitor extends FormulaBaseVisitor<Evaluated> {
    private final PartEvaluationCallback partEvaluationCallback;

    public AntlrEvalVisitor(final PartEvaluationCallback partEvaluationCallback) {
        this.partEvaluationCallback = Objects.requireNonNull(partEvaluationCallback);
        partEvaluationCallback.storeCurrentEvaluation(new Evaluated());
    }

    @Override
    public Evaluated visitChildren(final RuleNode node) {
        final Evaluated value = super.visitChildren(node);
        this.partEvaluationCallback.storeCurrentEvaluation(value);
        return value;
    }

    @Override
    protected Evaluated defaultResult() {
        return this.partEvaluationCallback.getCurrentEvaluated();
    }

    final class AntlrValueProvider implements ValueProvider {

        private final ParseTree tree;
        private Evaluated evaluated = null;

        AntlrValueProvider(final ParseTree tree) {
            this.tree = Objects.requireNonNull(tree);
        }

        @Override
        public Value provide() {
            evaluated = visit(tree);
            return evaluated.value();
        }

        public Evaluated getResult() {
            Objects.requireNonNull(evaluated, "Evaluated must have been defined");
            return evaluated;
        }
    }

    @Override
    public Evaluated visitArithmetic_functions(FormulaParser.Arithmetic_functionsContext ctx) {
        return partEvaluationCallback.evaluateArithmeticFunctions(
                () -> switch (ctx.function.getType()) {
                    case FormulaParser.ADD -> ArithmeticFunction.Function.ADD;
                    case FormulaParser.SUB -> ArithmeticFunction.Function.SUB;
                    case FormulaParser.DIV -> ArithmeticFunction.Function.DIV;
                    case FormulaParser.MUL -> ArithmeticFunction.Function.MUL;
                    default -> throw new IllegalStateException("Should not be here");
                },
                () -> this.visit(ctx.left),
                () -> this.visit(ctx.right),
                () -> AntlrDomainMapperHelper.toPositionedAt(ctx)
        );
    }

    @Override
    public Evaluated visitComparison_functions(final FormulaParser.Comparison_functionsContext ctx) {
        return partEvaluationCallback.evaluateComparisonFunctions(
                () -> switch (ctx.function.getType()) {
                    case FormulaParser.EQ -> ComparisonFunction.Comparison.EQ;
                    case FormulaParser.NEQ -> ComparisonFunction.Comparison.NEQ;
                    case FormulaParser.GT -> ComparisonFunction.Comparison.GT;
                    case FormulaParser.GTE -> ComparisonFunction.Comparison.GTE;
                    case FormulaParser.LT -> ComparisonFunction.Comparison.LT;
                    case FormulaParser.LTE -> ComparisonFunction.Comparison.LTE;
                    default -> throw new IllegalStateException("Should not be here");
                },
                () -> this.visit(ctx.left),
                () -> this.visit(ctx.right),
                () -> AntlrDomainMapperHelper.toPositionedAt(ctx)
        );
    }

    @Override
    public Evaluated visitLogical_boolean_functions(final FormulaParser.Logical_boolean_functionsContext ctx) {
        return partEvaluationCallback.evaluateLogicalBooleanFunctions(
                () -> switch (ctx.function.getType()) {
                    case FormulaParser.AND -> LogicalBooleanFunction.Function.AND;
                    case FormulaParser.OR -> LogicalBooleanFunction.Function.OR;
                    default -> throw new IllegalStateException("Should not be here");
                },
                () -> this.visit(ctx.left),
                () -> this.visit(ctx.right),
                () -> AntlrDomainMapperHelper.toPositionedAt(ctx)
        );
    }

    @Override
    public Evaluated visitLogical_comparison_functions(final FormulaParser.Logical_comparison_functionsContext ctx) {
        return partEvaluationCallback.evaluateLogicalComparisonFunctions(
                () -> switch (ctx.function.getType()) {
                    case FormulaParser.IF -> LogicalComparisonFunction.Function.IF;
                    case FormulaParser.IFERROR -> LogicalComparisonFunction.Function.IF_ERROR;
                    case FormulaParser.IFNA -> LogicalComparisonFunction.Function.IF_NOT_AVAILABLE;
                    default -> throw new IllegalStateException("Should not be here");
                },
                () -> this.visit(ctx.comparison),
                () -> new AntlrValueProvider(ctx.whenTrue),
                () -> new AntlrValueProvider(ctx.whenFalse),
                () -> AntlrDomainMapperHelper.toPositionedAt(ctx)
        );
    }

    @Override
    public Evaluated visitState_functions(final FormulaParser.State_functionsContext ctx) {
        return partEvaluationCallback.evaluateStateFunction(
                () -> switch (ctx.function.getType()) {
                    case FormulaParser.ISNA -> StateFunction.Function.IS_NOT_AVAILABLE;
                    case FormulaParser.ISERROR -> StateFunction.Function.IS_ERROR;
                    case FormulaParser.ISNUM -> StateFunction.Function.IS_NUMERIC;
                    case FormulaParser.ISTEXT -> StateFunction.Function.IS_TEXT;
                    case FormulaParser.ISBLANK -> StateFunction.Function.IS_BLANK;
                    case FormulaParser.ISLOGICAL -> StateFunction.Function.IS_LOGICAL;
                    default -> throw new IllegalStateException("Should not be here");
                },
                () -> this.visit(ctx.value),
                () -> AntlrDomainMapperHelper.toPositionedAt(ctx)
        );
    }

    @Override
    public Evaluated visitArgumentStructuredReference(final FormulaParser.ArgumentStructuredReferenceContext ctx) {
        return partEvaluationCallback.evaluateArgumentStructuredReference(
                () -> new Reference(ctx.STRUCTURED_REFERENCE().getText()
                        .substring(0, ctx.STRUCTURED_REFERENCE().getText().length() - 2)
                        .substring(3)),
                () -> AntlrDomainMapperHelper.toPositionedAt(ctx)
        );
    }

    @Override
    public Evaluated visitArgumentValue(final FormulaParser.ArgumentValueContext ctx) {
        return partEvaluationCallback.evaluateArgument(
                () -> Value.of(ctx.VALUE().getText()),
                () -> AntlrDomainMapperHelper.toPositionedAt(ctx)
        );
    }

    @Override
    public Evaluated visitArgumentNumeric(final FormulaParser.ArgumentNumericContext ctx) {
        return partEvaluationCallback.evaluateArgument(
                () -> Value.of(ctx.NUMERIC().getText()),
                () -> AntlrDomainMapperHelper.toPositionedAt(ctx)
        );
    }

    @Override
    public Evaluated visitArgumentBooleanTrue(final FormulaParser.ArgumentBooleanTrueContext ctx) {
        // Can be TRUE or 1 ... cannot return Value.ofTrue() because 1 will not be a numeric anymore
        return partEvaluationCallback.evaluateArgument(
                () -> Value.of(ctx.getText()),
                () -> AntlrDomainMapperHelper.toPositionedAt(ctx)
        );
    }

    @Override
    public Evaluated visitArgumentBooleanFalse(final FormulaParser.ArgumentBooleanFalseContext ctx) {
        // Can be FALSE or 0 ... cannot return Value.ofFalse() because 0 will not be a numeric anymore
        return partEvaluationCallback.evaluateArgument(
                () -> Value.of(ctx.getText()),
                () -> AntlrDomainMapperHelper.toPositionedAt(ctx)
        );
    }

}
