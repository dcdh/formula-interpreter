package com.damdamdeo.formula.infrastructure.evaluation.expression;

import com.damdamdeo.formula.FormulaBaseVisitor;
import com.damdamdeo.formula.FormulaParser;
import com.damdamdeo.formula.domain.*;
import com.damdamdeo.formula.infrastructure.parser.antlr.AntlrDomainMapperHelper;
import org.antlr.v4.runtime.tree.RuleNode;

public final class AntlrExpressionMapperVisitor extends FormulaBaseVisitor<Expression> {
    private Expression currentExpression;

    @Override
    public Expression visitChildren(final RuleNode node) {
        final Expression expression = super.visitChildren(node);
        this.currentExpression = expression;
        return expression;
    }

    @Override
    protected Expression defaultResult() {
        return this.currentExpression;
    }

    @Override
    public Expression visitArithmetic_functions(final FormulaParser.Arithmetic_functionsContext ctx) {
        final ArithmeticFunction.Function function = switch (ctx.function.getType()) {
            case FormulaParser.ADD -> ArithmeticFunction.Function.ADD;
            case FormulaParser.SUB -> ArithmeticFunction.Function.SUB;
            case FormulaParser.DIV -> ArithmeticFunction.Function.DIV;
            case FormulaParser.MUL -> ArithmeticFunction.Function.MUL;
            default -> throw new IllegalStateException("Should not be here");
        };
        return new ArithmeticExpression(
                function,
                this.visit(ctx.left),
                this.visit(ctx.right),
                AntlrDomainMapperHelper.toPositionedAt(ctx));
    }

    @Override
    public Expression visitComparison_functions(final FormulaParser.Comparison_functionsContext ctx) {
        final ComparisonFunction.Comparison comparison = switch (ctx.function.getType()) {
            case FormulaParser.EQ -> ComparisonFunction.Comparison.EQ;
            case FormulaParser.NEQ -> ComparisonFunction.Comparison.NEQ;
            case FormulaParser.GT -> ComparisonFunction.Comparison.GT;
            case FormulaParser.GTE -> ComparisonFunction.Comparison.GTE;
            case FormulaParser.LT -> ComparisonFunction.Comparison.LT;
            case FormulaParser.LTE -> ComparisonFunction.Comparison.LTE;
            default -> throw new IllegalStateException("Should not be here");
        };
        return new ComparisonExpression(
                comparison,
                this.visit(ctx.left),
                this.visit(ctx.right),
                AntlrDomainMapperHelper.toPositionedAt(ctx));
    }

    @Override
    public Expression visitLogical_boolean_functions(final FormulaParser.Logical_boolean_functionsContext ctx) {
        final LogicalBooleanFunction.Function function = switch (ctx.function.getType()) {
            case FormulaParser.AND -> LogicalBooleanFunction.Function.AND;
            case FormulaParser.OR -> LogicalBooleanFunction.Function.OR;
            default -> throw new IllegalStateException("Should not be here");
        };
        return new LogicalBooleanExpression(
                function,
                this.visit(ctx.left),
                this.visit(ctx.right),
                AntlrDomainMapperHelper.toPositionedAt(ctx));
    }

    @Override
    public Expression visitLogical_comparison_functions(final FormulaParser.Logical_comparison_functionsContext ctx) {
        final LogicalComparisonFunction.Function function = switch (ctx.function.getType()) {
            case FormulaParser.IF -> LogicalComparisonFunction.Function.IF;
            case FormulaParser.IFERROR -> LogicalComparisonFunction.Function.IF_ERROR;
            case FormulaParser.IFNA -> LogicalComparisonFunction.Function.IF_NOT_AVAILABLE;
            default -> throw new IllegalStateException("Should not be here");
        };
        return new LogicalComparisonExpression(
                function,
                this.visit(ctx.comparison),
                this.visit(ctx.whenTrue),
                this.visit(ctx.whenFalse),
                AntlrDomainMapperHelper.toPositionedAt(ctx));
    }

    @Override
    public Expression visitState_functions(final FormulaParser.State_functionsContext ctx) {
        final StateFunction.Function function = switch (ctx.function.getType()) {
            case FormulaParser.ISNA -> StateFunction.Function.IS_NOT_AVAILABLE;
            case FormulaParser.ISERROR -> StateFunction.Function.IS_ERROR;
            case FormulaParser.ISNUM -> StateFunction.Function.IS_NUMERIC;
            case FormulaParser.ISTEXT -> StateFunction.Function.IS_TEXT;
            case FormulaParser.ISBLANK -> StateFunction.Function.IS_BLANK;
            case FormulaParser.ISLOGICAL -> StateFunction.Function.IS_LOGICAL;
            default -> throw new IllegalStateException("Should not be here");
        };
        return new StateExpression(
                function,
                new Reference(ctx.value.getText()),
                AntlrDomainMapperHelper.toPositionedAt(ctx));
    }

    @Override
    public Expression visitArgumentStructuredReference(final FormulaParser.ArgumentStructuredReferenceContext ctx) {
        return new ArgumentExpression(
                Argument.ofStructuredReference(new Reference(ctx.STRUCTURED_REFERENCE().getText())),
                AntlrDomainMapperHelper.toPositionedAt(ctx));
    }

    @Override
    public Expression visitArgumentText(final FormulaParser.ArgumentTextContext ctx) {
        return new ArgumentExpression(
                Argument.ofText(Value.ofText(ctx.TEXT().getText())),
                AntlrDomainMapperHelper.toPositionedAt(ctx));
    }

    @Override
    public Expression visitArgumentNumeric(final FormulaParser.ArgumentNumericContext ctx) {
        return new ArgumentExpression(
                Argument.ofNumeric(Value.ofNumeric(ctx.NUMERIC().getText())),
                AntlrDomainMapperHelper.toPositionedAt(ctx));
    }

    @Override
    public Expression visitArgumentBooleanTrue(final FormulaParser.ArgumentBooleanTrueContext ctx) {
        // Can be TRUE or 1 ... cannot return Value.ofTrue() because 1 will not be a numeric anymore
        return new ArgumentExpression(
                Argument.ofBoolean(Value.ofBoolean(ctx.getText())),
                AntlrDomainMapperHelper.toPositionedAt(ctx));
    }

    @Override
    public Expression visitArgumentBooleanFalse(final FormulaParser.ArgumentBooleanFalseContext ctx) {
        // Can be FALSE or 0 ... cannot return Value.ofFalse() because 0 will not be a numeric anymore
        return new ArgumentExpression(
                Argument.ofBoolean(Value.ofBoolean(ctx.getText())),
                AntlrDomainMapperHelper.toPositionedAt(ctx));
    }

}
