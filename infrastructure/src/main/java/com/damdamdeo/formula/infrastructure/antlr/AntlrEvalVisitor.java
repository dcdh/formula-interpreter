package com.damdamdeo.formula.infrastructure.antlr;

import com.damdamdeo.formula.FormulaBaseVisitor;
import com.damdamdeo.formula.FormulaParser;
import com.damdamdeo.formula.domain.*;
import com.damdamdeo.formula.domain.spi.ValueProvider;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;

import java.util.Objects;

public final class AntlrEvalVisitor extends FormulaBaseVisitor<Result> {
    private final PartEvaluationCallback partEvaluationCallback;

    public AntlrEvalVisitor(final PartEvaluationCallback partEvaluationCallback) {
        this.partEvaluationCallback = Objects.requireNonNull(partEvaluationCallback);
        partEvaluationCallback.storeCurrentEvaluation(new Result());
    }

    @Override
    public Result visitChildren(final RuleNode node) {
        final Result value = super.visitChildren(node);
        this.partEvaluationCallback.storeCurrentEvaluation(value);
        return value;
    }

    @Override
    protected Result defaultResult() {
        return this.partEvaluationCallback.getCurrentEvaluation();
    }

    final class AntlrValueProvider implements ValueProvider {

        private final ParseTree tree;
        private Result result = null;

        AntlrValueProvider(final ParseTree tree) {
            this.tree = Objects.requireNonNull(tree);
        }

        @Override
        public Value provide() {
            result = visit(tree);
            return result.value();
        }

        public Result getResult() {
            Objects.requireNonNull(result, "Range must have been defined");
            return result;
        }
    }

    @Override
    public Result visitArithmetic_functions(FormulaParser.Arithmetic_functionsContext ctx) {
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
                () -> new Range(
                        ctx.getStart().getStartIndex(),
                        ctx.getStop().getStopIndex())
        );
    }

    @Override
    public Result visitComparison_functions(final FormulaParser.Comparison_functionsContext ctx) {
        return partEvaluationCallback.evaluateComparisonFunctions(
                () -> switch (ctx.function.getType()) {
                    case FormulaParser.EQ -> ComparisonFunction.ComparisonType.EQ;
                    case FormulaParser.NEQ -> ComparisonFunction.ComparisonType.NEQ;
                    case FormulaParser.GT -> ComparisonFunction.ComparisonType.GT;
                    case FormulaParser.GTE -> ComparisonFunction.ComparisonType.GTE;
                    case FormulaParser.LT -> ComparisonFunction.ComparisonType.LT;
                    case FormulaParser.LTE -> ComparisonFunction.ComparisonType.LTE;
                    default -> throw new IllegalStateException("Should not be here");
                },
                () -> this.visit(ctx.left),
                () -> this.visit(ctx.right),
                () -> new Range(
                        ctx.getStart().getStartIndex(),
                        ctx.getStop().getStopIndex())
        );
    }

    @Override
    public Result visitLogical_boolean_functions(final FormulaParser.Logical_boolean_functionsContext ctx) {
        return partEvaluationCallback.evaluateLogicalBooleanFunctions(
                () -> switch (ctx.function.getType()) {
                    case FormulaParser.AND -> LogicalBooleanFunction.Function.AND;
                    case FormulaParser.OR -> LogicalBooleanFunction.Function.OR;
                    default -> throw new IllegalStateException("Should not be here");
                },
                () -> this.visit(ctx.left),
                () -> this.visit(ctx.right),
                () -> new Range(
                        ctx.getStart().getStartIndex(),
                        ctx.getStop().getStopIndex())
        );
    }

    @Override
    public Result visitLogical_comparison_functions(final FormulaParser.Logical_comparison_functionsContext ctx) {
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
                () -> new Range(
                        ctx.getStart().getStartIndex(),
                        ctx.getStop().getStopIndex())
        );
    }

    @Override
    public Result visitState_functions(final FormulaParser.State_functionsContext ctx) {
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
                () -> new Range(
                        ctx.getStart().getStartIndex(),
                        ctx.getStop().getStopIndex())
        );
    }

    @Override
    public Result visitArgumentStructuredReference(final FormulaParser.ArgumentStructuredReferenceContext ctx) {
        return partEvaluationCallback.evaluateArgumentStructuredReference(
                () -> new Reference(ctx.STRUCTURED_REFERENCE().getText()
                        .substring(0, ctx.STRUCTURED_REFERENCE().getText().length() - 2)
                        .substring(3)),
                () -> new Range(
                        ctx.getStart().getStartIndex(),
                        ctx.getStop().getStopIndex())
        );
    }

    @Override
    public Result visitArgumentValue(final FormulaParser.ArgumentValueContext ctx) {
        return partEvaluationCallback.evaluateArgument(
                () -> Value.of(ctx.VALUE().getText()),
                () -> new Range(
                        ctx.getStart().getStartIndex(),
                        ctx.getStop().getStopIndex())
        );
    }

    @Override
    public Result visitArgumentNumeric(final FormulaParser.ArgumentNumericContext ctx) {
        return partEvaluationCallback.evaluateArgument(
                () -> Value.of(ctx.NUMERIC().getText()),
                () -> new Range(
                        ctx.getStart().getStartIndex(),
                        ctx.getStop().getStopIndex())
        );
    }

    @Override
    public Result visitArgumentBooleanTrue(final FormulaParser.ArgumentBooleanTrueContext ctx) {
        // Can be TRUE or 1 ... cannot return Value.ofTrue() because 1 will not be a numeric anymore
        return partEvaluationCallback.evaluateArgument(
                () -> Value.of(ctx.getText()),
                () -> new Range(
                        ctx.getStart().getStartIndex(),
                        ctx.getStop().getStopIndex())
        );
    }

    @Override
    public Result visitArgumentBooleanFalse(final FormulaParser.ArgumentBooleanFalseContext ctx) {
        // Can be FALSE or 0 ... cannot return Value.ofFalse() because 0 will not be a numeric anymore
        return partEvaluationCallback.evaluateArgument(
                () -> Value.of(ctx.getText()),
                () -> new Range(
                        ctx.getStart().getStartIndex(),
                        ctx.getStop().getStopIndex())
        );
    }
}
