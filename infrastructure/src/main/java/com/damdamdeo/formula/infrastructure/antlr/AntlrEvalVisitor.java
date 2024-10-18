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
        partEvaluationCallback.storeCurrentResult(new Result());
    }

    @Override
    public Result visitChildren(final RuleNode node) {
        final Result value = super.visitChildren(node);
        this.partEvaluationCallback.storeCurrentResult(value);
        return value;
    }

    @Override
    protected Result defaultResult() {
        return this.partEvaluationCallback.getCurrentResult();
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
                    case FormulaParser.ADD -> ArithmeticFunction.ofAddition();
                    case FormulaParser.SUB -> ArithmeticFunction.ofSubtraction();
                    case FormulaParser.DIV -> ArithmeticFunction.ofDivision();
                    case FormulaParser.MUL -> ArithmeticFunction.ofMultiplication();
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
                    case FormulaParser.EQ -> EqualityComparisonFunction.ofEqual();
                    case FormulaParser.NEQ -> EqualityComparisonFunction.ofNotEqual();
                    case FormulaParser.GT -> NumericalComparisonFunction.ofGreaterThan();
                    case FormulaParser.GTE -> NumericalComparisonFunction.ofGreaterThanOrEqualTo();
                    case FormulaParser.LT -> NumericalComparisonFunction.ofLessThan();
                    case FormulaParser.LTE -> NumericalComparisonFunction.ofLessThanOrEqualTo();
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
                    case FormulaParser.AND -> LogicalBooleanFunction.ofAnd();
                    case FormulaParser.OR -> LogicalBooleanFunction.ofOr();
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
                () -> {
                    final AntlrValueProvider onTrue = new AntlrValueProvider(ctx.whenTrue);
                    final AntlrValueProvider onFalse = new AntlrValueProvider(ctx.whenFalse);
                    return switch (ctx.function.getType()) {
                        case FormulaParser.IF -> LogicalComparisonFunction.ofIf(onTrue, onFalse);
                        case FormulaParser.IFERROR -> LogicalComparisonFunction.ofIfError(onTrue, onFalse);
                        case FormulaParser.IFNA -> LogicalComparisonFunction.ofIfNotAvailable(onTrue, onFalse);
                        default -> throw new IllegalStateException("Should not be here");
                    };
                },
                () -> this.visit(ctx.comparison),
                () -> new Range(
                        ctx.getStart().getStartIndex(),
                        ctx.getStop().getStopIndex())
        );
    }

    @Override
    public Result visitInformation_functions(final FormulaParser.Information_functionsContext ctx) {
        return partEvaluationCallback.evaluateInformationFunction(
                () -> switch (ctx.function.getType()) {
                    case FormulaParser.ISNA -> InformationFunction.ofIsNotAvailable();
                    case FormulaParser.ISERROR -> InformationFunction.ofIsError();
                    case FormulaParser.ISNUM -> InformationFunction.ofIsNumeric();
                    case FormulaParser.ISTEXT -> InformationFunction.ofIsText();
                    case FormulaParser.ISBLANK -> InformationFunction.ofIsBlank();
                    case FormulaParser.ISLOGICAL -> InformationFunction.ofIsLogical();
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
