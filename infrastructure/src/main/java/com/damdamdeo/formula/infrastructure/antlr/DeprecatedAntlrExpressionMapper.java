package com.damdamdeo.formula.infrastructure.antlr;

import com.damdamdeo.formula.FormulaBaseListener;
import com.damdamdeo.formula.FormulaParser;
import com.damdamdeo.formula.domain.*;
import com.damdamdeo.formula.domain.evaluation.*;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

// Depth-first search
@Deprecated(forRemoval = true)
public final class DeprecatedAntlrExpressionMapper extends FormulaBaseListener {

    private final Stack<Expression> stackOfExpressions = new Stack<>();

    private Expression expressionResult;


    enum NodeType {
        FUNC_,
        ARG;
    }

    private record Node(List<Node> children) {
        // TODO il me faut le type
        public Node() {
            this(new ArrayList<>());
        }

        public void addChild(final Node child) {
            children.add(child);
        }

//        sinon je fais aussi un visitor pour me le transformer en expression ... ouais bah autant faire un visitor alors!
    }

    private Node root;

    @Override
    public void enterProgram(final FormulaParser.ProgramContext ctx) {
        stackOfExpressions.clear();
    }

    @Override
    public void exitProgram(final FormulaParser.ProgramContext ctx) {
        Validate.validState(stackOfExpressions.size() == 1,
                "One expression in stack is expected, got %d", stackOfExpressions.size());
        expressionResult = stackOfExpressions.pop();
    }

    @Override
    public void exitArgumentStructuredReference(final FormulaParser.ArgumentStructuredReferenceContext ctx) {
        final Expression expression = new StructuredReferencesExpression(
                new Reference(ctx.STRUCTURED_REFERENCE().getText()),
                AntlrDomainMapperHelper.toRange(ctx));
        stackOfExpressions.push(expression);
    }

    @Override
    public void exitArgumentValue(final FormulaParser.ArgumentValueContext ctx) {
        final Expression expression = new ArgumentExpression(
                new Value(ctx.VALUE().getText()),
                AntlrDomainMapperHelper.toRange(ctx));
        stackOfExpressions.push(expression);
    }

    @Override
    public void exitArgumentNumeric(final FormulaParser.ArgumentNumericContext ctx) {
        final Expression expression = new ArgumentExpression(
                new Value(ctx.NUMERIC().getText()),
                AntlrDomainMapperHelper.toRange(ctx));
        stackOfExpressions.push(expression);
    }

    @Override
    public void exitArgumentBooleanTrue(final FormulaParser.ArgumentBooleanTrueContext ctx) {
        // Can be TRUE or 1 ... cannot return Value.ofTrue() because 1 will not be a numeric anymore
        final Expression expression = new ArgumentExpression(
                new Value(ctx.getText()),
                AntlrDomainMapperHelper.toRange(ctx));
        stackOfExpressions.push(expression);
    }

    @Override
    public void exitArgumentBooleanFalse(final FormulaParser.ArgumentBooleanFalseContext ctx) {
        // Can be FALSE or 0 ... cannot return Value.ofFalse() because 0 will not be a numeric anymore
        final Expression expression = new ArgumentExpression(
                new Value(ctx.getText()),
                AntlrDomainMapperHelper.toRange(ctx));
        stackOfExpressions.push(expression);
    }

    @Override
    public void enterArithmetic_functions(final FormulaParser.Arithmetic_functionsContext ctx) {
        System.out.println(ctx);
    }

    @Override
    public void exitArithmetic_functions(final FormulaParser.Arithmetic_functionsContext ctx) {
        final Expression left = stackOfExpressions.pop();
        final Expression right = stackOfExpressions.pop();
        final ArithmeticFunction.Function function = switch (ctx.function.getType()) {
            case FormulaParser.ADD -> ArithmeticFunction.Function.ADD;
            case FormulaParser.SUB -> ArithmeticFunction.Function.SUB;
            case FormulaParser.DIV -> ArithmeticFunction.Function.DIV;
            case FormulaParser.MUL -> ArithmeticFunction.Function.MUL;
            default -> throw new IllegalStateException("Should not be here");
        };
        final Expression expression = new ArithmeticExpression(function,
                left, right, AntlrDomainMapperHelper.toRange(ctx));
        stackOfExpressions.push(expression);
    }

    @Override
    public void exitComparison_functions(final FormulaParser.Comparison_functionsContext ctx) {
        final Expression left = stackOfExpressions.pop();
        final Expression right = stackOfExpressions.pop();
        final ComparisonFunction.Comparison comparaison = switch (ctx.function.getType()) {
            case FormulaParser.EQ -> ComparisonFunction.Comparison.EQ;
            case FormulaParser.NEQ -> ComparisonFunction.Comparison.NEQ;
            case FormulaParser.GT -> ComparisonFunction.Comparison.GT;
            case FormulaParser.GTE -> ComparisonFunction.Comparison.GTE;
            case FormulaParser.LT -> ComparisonFunction.Comparison.LT;
            case FormulaParser.LTE -> ComparisonFunction.Comparison.LTE;
            default -> throw new IllegalStateException("Should not be here");
        };
        final Expression expression = new ComparisonExpression(comparaison,
                left, right, AntlrDomainMapperHelper.toRange(ctx));
        stackOfExpressions.push(expression);
    }

    @Override
    public void exitLogical_boolean_functions(final FormulaParser.Logical_boolean_functionsContext ctx) {
        final Expression left = stackOfExpressions.pop();
        final Expression right = stackOfExpressions.pop();
        final LogicalBooleanFunction.Function function = switch (ctx.function.getType()) {
            case FormulaParser.AND -> LogicalBooleanFunction.Function.AND;
            case FormulaParser.OR -> LogicalBooleanFunction.Function.OR;
            default -> throw new IllegalStateException("Should not be here");
        };
        final Expression expression = new LogicalBooleanExpression(function,
                left, right, AntlrDomainMapperHelper.toRange(ctx));
        stackOfExpressions.push(expression);
    }

    @Override
    public void exitLogical_comparison_functions(final FormulaParser.Logical_comparison_functionsContext ctx) {
        final Expression comparison = stackOfExpressions.pop();
        final Expression onTrue = stackOfExpressions.pop();
        final Expression onFalse = stackOfExpressions.pop();
        final LogicalComparisonFunction.Function function = switch (ctx.function.getType()) {
            case FormulaParser.IF -> LogicalComparisonFunction.Function.IF;
            case FormulaParser.IFERROR -> LogicalComparisonFunction.Function.IF_ERROR;
            case FormulaParser.IFNA -> LogicalComparisonFunction.Function.IF_NOT_AVAILABLE;
            default -> throw new IllegalStateException("Should not be here");
        };
        final Expression expression = new LogicalComparisonExpression(function, comparison, onTrue, onFalse,
                AntlrDomainMapperHelper.toRange(ctx));
        stackOfExpressions.push(expression);
    }

    @Override
    public void exitState_functions(final FormulaParser.State_functionsContext ctx) {
        final Expression argument = stackOfExpressions.pop();
        final StateFunction.Function function = switch (ctx.function.getType()) {
            case FormulaParser.ISNA -> StateFunction.Function.IS_NOT_AVAILABLE;
            case FormulaParser.ISERROR -> StateFunction.Function.IS_ERROR;
            case FormulaParser.ISNUM -> StateFunction.Function.IS_NUMERIC;
            case FormulaParser.ISTEXT -> StateFunction.Function.IS_TEXT;
            case FormulaParser.ISBLANK -> StateFunction.Function.IS_BLANK;
            case FormulaParser.ISLOGICAL -> StateFunction.Function.IS_LOGICAL;
            default -> throw new IllegalStateException("Should not be here");
        };
        final Expression expression = new StateExpression(function, argument, AntlrDomainMapperHelper.toRange(ctx));
        stackOfExpressions.push(expression);
    }

    @Override
    public void visitErrorNode(final ErrorNode node) {
        // TODO ?
    }

    public Expression expressionResult() {
        return expressionResult;
    }
}
