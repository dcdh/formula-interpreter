package com.damdamdeo.formula.infrastructure.antlr;

import com.damdamdeo.formula.FormulaBaseVisitor;
import com.damdamdeo.formula.FormulaParser;
import com.damdamdeo.formula.domain.*;
import com.damdamdeo.formula.domain.spi.ValueProvider;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;

import java.util.List;
import java.util.Objects;

public final class EvalVisitor extends FormulaBaseVisitor<Result> {
    private final ExecutionWrapper executionWrapper;
    private final StructuredData structuredData;
    private final NumericalContext numericalContext;
    private Result currentResult;

    public EvalVisitor(final ExecutionWrapper executionWrapper,
                       final StructuredData structuredData,
                       final NumericalContext numericalContext) {
        this.executionWrapper = Objects.requireNonNull(executionWrapper);
        this.structuredData = Objects.requireNonNull(structuredData);
        this.numericalContext = Objects.requireNonNull(numericalContext);
        this.currentResult = new Result();
    }

    @Override
    public Result visitChildren(final RuleNode node) {
        final Result value = super.visitChildren(node);
        this.currentResult = value;
        return value;
    }

    @Override
    protected Result defaultResult() {
        return currentResult;
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
        return executionWrapper.execute(() -> {
            final Result leftResult = this.visit(ctx.left);
            final Result rightResult = this.visit(ctx.right);
            final ArithmeticFunction arithmeticFunction = switch (ctx.function.getType()) {
                case FormulaParser.ADD -> ArithmeticFunction.ofAddition();
                case FormulaParser.SUB -> ArithmeticFunction.ofSubtraction();
                case FormulaParser.DIV -> ArithmeticFunction.ofDivision();
                case FormulaParser.MUL -> ArithmeticFunction.ofMultiplication();
                default -> throw new IllegalStateException("Should not be here");
            };
            final Value value = arithmeticFunction.execute(leftResult.value(), rightResult.value(), numericalContext);
            return new Result(value,
                    List.of(
                            new Input(
                                    InputName.ofLeft(),
                                    leftResult.value(),
                                    leftResult.range()),
                            new Input(
                                    InputName.ofRight(),
                                    rightResult.value(),
                                    rightResult.range())
                    ),
                    new Range(
                            ctx.getStart().getStartIndex(),
                            ctx.getStop().getStopIndex())
            );
        });
    }

    @Override
    public Result visitComparison_functions(final FormulaParser.Comparison_functionsContext ctx) {
        return executionWrapper.execute(() -> {
            final Result leftResult = this.visit(ctx.left);
            final Result rightResult = this.visit(ctx.right);
            final ComparisonFunction comparisonFunction = switch (ctx.function.getType()) {
                case FormulaParser.EQ -> EqualityComparisonFunction.ofEqual();
                case FormulaParser.NEQ -> EqualityComparisonFunction.ofNotEqual();
                case FormulaParser.GT -> NumericalComparisonFunction.ofGreaterThan();
                case FormulaParser.GTE -> NumericalComparisonFunction.ofGreaterThanOrEqualTo();
                case FormulaParser.LT -> NumericalComparisonFunction.ofLessThan();
                case FormulaParser.LTE -> NumericalComparisonFunction.ofLessThanOrEqualTo();
                default -> throw new IllegalStateException("Should not be here");
            };
            final Value value = comparisonFunction.execute(leftResult.value(), rightResult.value(), numericalContext);
            return new Result(value,
                    List.of(
                            new Input(
                                    InputName.ofLeft(),
                                    leftResult.value(),
                                    leftResult.range()),
                            new Input(
                                    InputName.ofRight(),
                                    rightResult.value(),
                                    rightResult.range())
                    ),
                    new Range(
                            ctx.getStart().getStartIndex(),
                            ctx.getStop().getStopIndex())
            );
        });
    }

    @Override
    public Result visitLogical_boolean_functions(final FormulaParser.Logical_boolean_functionsContext ctx) {
        return executionWrapper.execute(() -> {
            final Result leftResult = this.visit(ctx.left);
            final Result rightResult = this.visit(ctx.right);
            final LogicalBooleanFunction logicalBooleanFunction = switch (ctx.function.getType()) {
                case FormulaParser.AND -> LogicalBooleanFunction.ofAnd();
                case FormulaParser.OR -> LogicalBooleanFunction.ofOr();
                default -> throw new IllegalStateException("Should not be here");
            };
            final Value value = logicalBooleanFunction.execute(leftResult.value(), rightResult.value());
            return new Result(value,
                    List.of(
                            new Input(
                                    InputName.ofLeft(),
                                    leftResult.value(),
                                    leftResult.range()),
                            new Input(
                                    InputName.ofRight(),
                                    rightResult.value(),
                                    rightResult.range())
                    ),
                    new Range(
                            ctx.getStart().getStartIndex(),
                            ctx.getStop().getStopIndex())
            );
        });
    }

    @Override
    public Result visitLogical_comparison_functions(final FormulaParser.Logical_comparison_functionsContext ctx) {
        return executionWrapper.execute(() -> {
            final Result comparisonResult = this.visit(ctx.comparison);
            final AntlrValueProvider onTrue = new AntlrValueProvider(ctx.whenTrue);
            final AntlrValueProvider onFalse = new AntlrValueProvider(ctx.whenFalse);
            final LogicalComparisonFunction logicalComparisonFunction = switch (ctx.function.getType()) {
                case FormulaParser.IF -> LogicalComparisonFunction.ofIf(onTrue, onFalse);
                case FormulaParser.IFERROR -> LogicalComparisonFunction.ofIfError(onTrue, onFalse);
                case FormulaParser.IFNA -> LogicalComparisonFunction.ofIfNotAvailable(onTrue, onFalse);
                default -> throw new IllegalStateException("Should not be here");
            };
            final Value value = logicalComparisonFunction.execute(comparisonResult.value());
            return new Result(value,
                    List.of(
                            new Input(
                                    InputName.ofComparisonValue(),
                                    comparisonResult.value(),
                                    comparisonResult.range())
                    ),
                    new Range(
                            ctx.getStart().getStartIndex(),
                            ctx.getStop().getStopIndex())
            );
        });
    }

    @Override
    public Result visitInformation_functions(final FormulaParser.Information_functionsContext ctx) {
        return executionWrapper.execute(() -> {
            final Result value = this.visit(ctx.value);
            final InformationFunction informationFunction = switch (ctx.function.getType()) {
                case FormulaParser.ISNA -> InformationFunction.ofIsNotAvailable();
                case FormulaParser.ISERROR -> InformationFunction.ofIsError();
                case FormulaParser.ISNUM -> InformationFunction.ofIsNumeric();
                case FormulaParser.ISTEXT -> InformationFunction.ofIsText();
                case FormulaParser.ISBLANK -> InformationFunction.ofIsBlank();
                case FormulaParser.ISLOGICAL -> InformationFunction.ofIsLogical();
                default -> throw new IllegalStateException("Should not be here");
            };
            final Value result = informationFunction.execute(value.value()) ? Value.ofTrue() : Value.ofFalse();
            return new Result(result,
                    List.of(
                            new Input(
                                    InputName.ofValue(),
                                    value.value(),
                                    value.range())
                    ),
                    new Range(
                            ctx.getStart().getStartIndex(),
                            ctx.getStop().getStopIndex())
            );
        });
    }

    @Override
    public Result visitArgumentStructuredReference(final FormulaParser.ArgumentStructuredReferenceContext ctx) {
        return executionWrapper.execute(() -> {
            Value result;
            final Reference reference = new Reference(ctx.STRUCTURED_REFERENCE().getText()
                    .substring(0, ctx.STRUCTURED_REFERENCE().getText().length() - 2)
                    .substring(3));
            try {
                result = this.structuredData.getValueByReference(reference);
            } catch (final UnknownReferenceException unknownReferenceException) {
                result = Value.ofUnknownRef();
            }
            return new Result(result,
                    List.of(
                            new Input(
                                    InputName.ofStructuredReference(),
                                    reference,
                                    new Range(
                                            ctx.getStart().getStartIndex() + 3,
                                            ctx.getStop().getStopIndex() - 2))
                    ),
                    new Range(
                            ctx.getStart().getStartIndex(),
                            ctx.getStop().getStopIndex())
            );
        });
    }

    @Override
    public Result visitArgumentValue(final FormulaParser.ArgumentValueContext ctx) {
        return executionWrapper.execute(() -> new Result(Value.of(ctx.VALUE().getText()),
                new Range(
                        ctx.getStart().getStartIndex(),
                        ctx.getStop().getStopIndex())
        ));
    }

    @Override
    public Result visitArgumentNumeric(final FormulaParser.ArgumentNumericContext ctx) {
        return executionWrapper.execute(() -> new Result(Value.of(ctx.NUMERIC().getText()),
                new Range(
                        ctx.getStart().getStartIndex(),
                        ctx.getStop().getStopIndex())
        ));
    }

    @Override
    public Result visitArgumentBooleanTrue(final FormulaParser.ArgumentBooleanTrueContext ctx) {
        // Can be TRUE or 1 ... cannot return Value.ofTrue() because 1 will not be a numeric anymore
        return executionWrapper.execute(() -> new Result(Value.of(ctx.getText()),
                new Range(
                        ctx.getStart().getStartIndex(),
                        ctx.getStop().getStopIndex())
        ));
    }

    @Override
    public Result visitArgumentBooleanFalse(final FormulaParser.ArgumentBooleanFalseContext ctx) {
        // Can be FALSE or 0 ... cannot return Value.ofFalse() because 0 will not be a numeric anymore
        return executionWrapper.execute(() -> new Result(
                Value.of(ctx.getText()),
                new Range(
                        ctx.getStart().getStartIndex(),
                        ctx.getStop().getStopIndex())
        ));
    }
}
