package com.damdamdeo.formula.infrastructure.antlr;

import com.damdamdeo.formula.FormulaBaseVisitor;
import com.damdamdeo.formula.FormulaParser;
import com.damdamdeo.formula.domain.*;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.RuleNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public final class EvalVisitor extends FormulaBaseVisitor<Value> {
    private static final String INPUT_NAME_LEFT = "left";
    private static final String INPUT_NAME_RIGHT = "right";
    private static final String INPUT_NAME_COMPARISON_VALUE = "comparisonValue";
    private static final String INPUT_NAME_VALUE = "value";
    private static final String INPUT_NAME_STRUCTURED_REFERENCE = "structuredReference";
    private final ExecutedAtProvider executedAtProvider;
    private final StructuredData structuredData;
    private final NumericalContext numericalContext;
    private Value currentResult;
    private final Map<ExecutionId, Execution> executions;
    private final AtomicInteger currentExecutionId;

    public EvalVisitor(final ExecutedAtProvider executedAtProvider,
                       final StructuredData structuredData,
                       final NumericalContext numericalContext) {
        this.executedAtProvider = Objects.requireNonNull(executedAtProvider);
        this.structuredData = Objects.requireNonNull(structuredData);
        this.numericalContext = Objects.requireNonNull(numericalContext);
        this.currentResult = Value.ofNotAvailable();
        this.executions = new HashMap<>();
        this.currentExecutionId = new AtomicInteger(-1);
    }

    public List<Execution> executions() {
        return executions
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    @Override
    public Value visitChildren(final RuleNode node) {
        final Value value = super.visitChildren(node);
        this.currentResult = value;
        return value;
    }

    @Override
    protected Value defaultResult() {
        return currentResult;
    }

    public record ExecutionId(Integer id) implements Comparable<ExecutionId> {
        public ExecutionId(final AtomicInteger currentExecutionId) {
            this(currentExecutionId.addAndGet(1));
        }

        @Override
        public int compareTo(final ExecutionId executionId) {
            return id.compareTo(executionId.id());
        }
    }

    public record ExecutionResult(Value value, Map<InputName, Input> inputs) {
        public ExecutionResult(final Value value) {
            this(value, Map.of());
        }
    }

    @Override
    public Value visitArithmeticFunctionsOperatorLeftOpRight(final FormulaParser.ArithmeticFunctionsOperatorLeftOpRightContext ctx) {
        return execute(() -> {
            final Value left = this.visit(ctx.left);
            final Value right = this.visit(ctx.right);
            final Value result;
            if (left.isNotAvailable() || right.isNotAvailable()) {
                result = Value.ofNotAvailable();
            } else if (left.isUnknownRef() || right.isUnknownRef()) {
                result = Value.ofUnknownRef();
            } else if (left.isNotANumericalValue() || right.isNotANumericalValue()) {
                result = Value.ofNumericalValueExpected();
            } else if (left.isDivByZero() || right.isDivByZero()) {
                result = Value.ofDividedByZero();
            } else if (!left.isNumeric() || !right.isNumeric()) {
                result = Value.ofNumericalValueExpected();
            } else if (right.isNumeric() && right.isZero()) {
                result = Value.ofDividedByZero();
            } else {
                final Operator operator = switch (ctx.operator.getType()) {
                    case FormulaParser.ADD -> Operator.ADD;
                    case FormulaParser.SUB -> Operator.SUB;
                    case FormulaParser.DIV -> Operator.DIV;
                    case FormulaParser.MUL -> Operator.MUL;
                    default -> throw new IllegalStateException("Should not be here");
                };
                result = operator.execute(left, right, numericalContext);
            }
            final Map<InputName, Input> inputs = Map.of(
                    new InputName(INPUT_NAME_LEFT), left,
                    new InputName(INPUT_NAME_RIGHT), right);
            return new ExecutionResult(result, inputs);
        }, ctx);
    }

    @Override
    public Value visitComparisonFunctionsNumerical(final FormulaParser.ComparisonFunctionsNumericalContext ctx) {
        return execute(() -> {
            final Value left = this.visit(ctx.left);
            final Value right = this.visit(ctx.right);
            final Value result;
            if (left.isNotAvailable() || right.isNotAvailable()) {
                result = Value.ofNotAvailable();
            } else if (left.isUnknownRef() || right.isUnknownRef()) {
                result = Value.ofUnknownRef();
            } else if (left.isNotANumericalValue() || right.isNotANumericalValue()) {
                result = Value.ofNumericalValueExpected();
            } else if (left.isDivByZero() || right.isDivByZero()) {
                result = Value.ofDividedByZero();
            } else if (!left.isNumeric() || !right.isNumeric()) {
                result = Value.ofNumericalValueExpected();
            } else {
                final NumericalComparator numericalComparator = switch (ctx.numericalComparator.getType()) {
                    case FormulaParser.GT -> NumericalComparator.GT;
                    case FormulaParser.GTE -> NumericalComparator.GTE;
                    case FormulaParser.LT -> NumericalComparator.LT;
                    case FormulaParser.LTE -> NumericalComparator.LTE;
                    default -> throw new IllegalStateException("Should not be here");
                };
                result = numericalComparator.execute(left, right, numericalContext);
            }
            final Map<InputName, Input> inputs = Map.of(
                    new InputName(INPUT_NAME_LEFT), left,
                    new InputName(INPUT_NAME_RIGHT), right);
            return new ExecutionResult(result, inputs);
        }, ctx);
    }

    @Override
    public Value visitComparisonFunctionsEquality(final FormulaParser.ComparisonFunctionsEqualityContext ctx) {
        return execute(() -> {
            final Value left = this.visit(ctx.left);
            final Value right = this.visit(ctx.right);
            final Value result;
            if (left.isNotAvailable() || right.isNotAvailable()) {
                result = Value.ofNotAvailable();
            } else if (left.isUnknownRef() || right.isUnknownRef()) {
                result = Value.ofUnknownRef();
            } else if (left.isNotANumericalValue() || right.isNotANumericalValue()) {
                result = Value.ofNumericalValueExpected();
            } else if (left.isDivByZero() || right.isDivByZero()) {
                result = Value.ofDividedByZero();
            } else {
                final EqualityComparator equalityComparator = switch (ctx.equalityComparator.getType()) {
                    case FormulaParser.EQ -> EqualityComparator.EQ;
                    case FormulaParser.NEQ -> EqualityComparator.NEQ;
                    default -> throw new IllegalStateException("Should not be here");
                };
                result = equalityComparator.execute(left, right, numericalContext);
            }
            final Map<InputName, Input> inputs = Map.of(
                    new InputName(INPUT_NAME_LEFT), left,
                    new InputName(INPUT_NAME_RIGHT), right);
            return new ExecutionResult(result, inputs);
        }, ctx);
    }

    @Override
    public Value visitLogicalOperatorFunction(final FormulaParser.LogicalOperatorFunctionContext ctx) {
        return execute(() -> {
            final Value left = this.visit(ctx.left);
            final Value right = this.visit(ctx.right);
            final Value result;
            if (left.isNotAvailable() || right.isNotAvailable()) {
                result = Value.ofNotAvailable();
            } else if (left.isUnknownRef() || right.isUnknownRef()) {
                result = Value.ofUnknownRef();
            } else if (left.isNotANumericalValue() || right.isNotANumericalValue()) {
                result = Value.ofNumericalValueExpected();
            } else if (left.isDivByZero() || right.isDivByZero()) {
                result = Value.ofDividedByZero();
            } else {
                final LogicalOperator logicalOperator = switch (ctx.logicalOperator.getType()) {
                    case FormulaParser.AND -> LogicalOperator.AND;
                    case FormulaParser.OR -> LogicalOperator.OR;
                    default -> throw new IllegalStateException("Should not be here");
                };
                result = logicalOperator.execute(left, right);
            }
            final Map<InputName, Input> inputs = Map.of(
                    new InputName(INPUT_NAME_LEFT), left,
                    new InputName(INPUT_NAME_RIGHT), right);
            return new ExecutionResult(result, inputs);
        }, ctx);
    }

    @Override
    public Value visitIfFunction(final FormulaParser.IfFunctionContext ctx) {
        return execute(() -> {
            final Value comparisonValue = this.visit(ctx.comparison);
            final Value result;
            if (comparisonValue.isNotAvailable()) {
                result = Value.ofNotAvailable();
            } else if (comparisonValue.isUnknownRef()) {
                result = Value.ofUnknownRef();
            } else if (comparisonValue.isNotANumericalValue()) {
                result = Value.ofNumericalValueExpected();
            } else if (comparisonValue.isDivByZero()) {
                result = Value.ofDividedByZero();
            } else if (comparisonValue.isTrue()) {
                result = this.visit(ctx.whenTrue);
            } else if (comparisonValue.isFalse()) {
                result = this.visit(ctx.whenFalse);
            } else {
                throw new IllegalStateException("Should not be here");
            }
            final Map<InputName, Input> inputs = Map.of(
                    new InputName(INPUT_NAME_COMPARISON_VALUE), comparisonValue);
            return new ExecutionResult(result, inputs);
        }, ctx);
    }

    @Override
    public Value visitIfErrorFunction(final FormulaParser.IfErrorFunctionContext ctx) {
        return execute(() -> {
            final Value comparisonValue = this.visit(ctx.comparison);
            final Value result;
            if (comparisonValue.isError()) {
                result = this.visit(ctx.whenTrue);
            } else {
                result = this.visit(ctx.whenFalse);
            }
            final Map<InputName, Input> inputs = Map.of(
                    new InputName(INPUT_NAME_COMPARISON_VALUE), comparisonValue);
            return new ExecutionResult(result, inputs);
        }, ctx);
    }

    @Override
    public Value visitIfNaFunction(final FormulaParser.IfNaFunctionContext ctx) {
        return execute(() -> {
            final Value comparisonValue = this.visit(ctx.comparison);
            final Value result;
            if (comparisonValue.isNotAvailable()) {
                result = this.visit(ctx.whenTrue);
            } else {
                result = this.visit(ctx.whenFalse);
            }
            final Map<InputName, Input> inputs = Map.of(
                    new InputName(INPUT_NAME_COMPARISON_VALUE), comparisonValue);
            return new ExecutionResult(result, inputs);
        }, ctx);
    }

    @Override
    public Value visitIsFunction(final FormulaParser.IsFunctionContext ctx) {
        return execute(() -> {
            final Value value = this.visit(ctx.value);
            final Value result;
            if (value.isNotAvailable()) {
                result = Value.ofNotAvailable();
            } else if (value.isUnknownRef()) {
                result = Value.ofUnknownRef();
            } else if (value.isNotANumericalValue()) {
                result = Value.ofNumericalValueExpected();
            } else if (value.isDivByZero()) {
                result = Value.ofDividedByZero();
            } else {
                result = switch (ctx.isOperator.getType()) {
                    case FormulaParser.ISNUM -> value.isNumeric() ? Value.ofTrue() : Value.ofFalse();
                    case FormulaParser.ISTEXT -> value.isText() ? Value.ofTrue() : Value.ofFalse();
                    case FormulaParser.ISBLANK -> value.isBlank() ? Value.ofTrue() : Value.ofFalse();
                    case FormulaParser.ISLOGICAL -> value.isLogical() ? Value.ofTrue() : Value.ofFalse();
                    default -> throw new IllegalStateException("Should not be here");
                };
            }
            final Map<InputName, Input> inputs = Map.of(
                    new InputName(INPUT_NAME_VALUE), value);
            return new ExecutionResult(result, inputs);
        }, ctx);
    }

    @Override
    public Value visitIsNaFunction(final FormulaParser.IsNaFunctionContext ctx) {
        return execute(() -> {
            final Value value = this.visit(ctx.value);
            final Value result = value.isNotAvailable() ? Value.ofTrue() : Value.ofFalse();
            final Map<InputName, Input> inputs = Map.of(
                    new InputName(INPUT_NAME_VALUE), value);
            return new ExecutionResult(result, inputs);
        }, ctx);
    }

    @Override
    public Value visitIsErrorFunction(final FormulaParser.IsErrorFunctionContext ctx) {
        return execute(() -> {
            final Value value = this.visit(ctx.value);
            final Value result = value.isError() ? Value.ofTrue() : Value.ofFalse();
            final Map<InputName, Input> inputs = Map.of(
                    new InputName(INPUT_NAME_VALUE), value);
            return new ExecutionResult(result, inputs);
        }, ctx);
    }

    @Override
    public Value visitArgumentStructuredReference(final FormulaParser.ArgumentStructuredReferenceContext ctx) {
        return execute(() -> {
            Value result;
            final String reference = ctx.STRUCTURED_REFERENCE().getText()
                    .substring(0, ctx.STRUCTURED_REFERENCE().getText().length() - 2)
                    .substring(3);
            try {
                result = this.structuredData.getValueByReference(new Reference(reference));
            } catch (final UnknownReferenceException unknownReferenceException) {
                result = Value.ofUnknownRef();
            }
            final Map<InputName, Input> inputs = Map.of(
                    new InputName(INPUT_NAME_STRUCTURED_REFERENCE), new Reference(reference));
            return new ExecutionResult(result, inputs);
        }, ctx);
    }

    @Override
    public Value visitArgumentValue(final FormulaParser.ArgumentValueContext ctx) {
        return execute(() -> new ExecutionResult(Value.of(ctx.VALUE().getText())), ctx);
    }

    @Override
    public Value visitArgumentNumeric(final FormulaParser.ArgumentNumericContext ctx) {
        return execute(() -> new ExecutionResult(Value.of(ctx.NUMERIC().getText())), ctx);
    }

    @Override
    public Value visitArgumentBooleanTrue(final FormulaParser.ArgumentBooleanTrueContext ctx) {
        // Can be TRUE or 1 ... cannot return Value.ofTrue() because 1 will not be a numeric anymore
        return execute(() -> new ExecutionResult(Value.of(ctx.getText())), ctx);
    }

    @Override
    public Value visitArgumentBooleanFalse(final FormulaParser.ArgumentBooleanFalseContext ctx) {
        // Can be FALSE or 0 ... cannot return Value.ofFalse() because 0 will not be a numeric anymore
        return execute(() -> new ExecutionResult(Value.of(ctx.getText())), ctx);
    }

    private Value execute(final Callable<ExecutionResult> callable,
                          final ParserRuleContext parserRuleContext) {
        Objects.requireNonNull(callable);
        Objects.requireNonNull(parserRuleContext);
        try {
            final ExecutionId executionId = new ExecutionId(currentExecutionId);
            final ExecutedAtStart executedAtStart = executedAtProvider.now();
            final ExecutionResult result = callable.call();
            final ExecutedAtEnd executedAtEnd = executedAtProvider.now();
            executions.put(executionId, AntlrExecution.Builder.newBuilder()
                    .executedAtStart(executedAtStart)
                    .executedAtEnd(executedAtEnd)
                    .using(parserRuleContext)
                    .withInputs(result.inputs())
                    .result(result.value())
                    .build());
            return result.value();
        } catch (final Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
