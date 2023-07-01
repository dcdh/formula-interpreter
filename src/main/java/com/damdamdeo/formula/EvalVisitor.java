package com.damdamdeo.formula;

import com.damdamdeo.formula.structuredreference.Reference;
import com.damdamdeo.formula.structuredreference.StructuredData;
import com.damdamdeo.formula.structuredreference.UnknownReferenceException;
import org.antlr.v4.runtime.tree.RuleNode;

import java.util.Objects;

public final class EvalVisitor extends FormulaBaseVisitor<Value> {
    private final ExecutionId executionId;
    private final ExecutionLogger executionLogger;
    private final StructuredData structuredData;
    private final NumericalContext numericalContext;

    private Value currentResult = Value.ofNotAvailable();

    public EvalVisitor(final ExecutionId executionId,
                       final ExecutionLogger executionLogger,
                       final StructuredData structuredData,
                       final NumericalContext numericalContext) {
        this.executionId = Objects.requireNonNull(executionId);
        this.executionLogger = Objects.requireNonNull(executionLogger);
        this.structuredData = Objects.requireNonNull(structuredData);
        this.numericalContext = Objects.requireNonNull(numericalContext);
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

    @Override
    public Value visitArithmeticFunctionsOperatorLeftOpRight(final FormulaParser.ArithmeticFunctionsOperatorLeftOpRightContext ctx) {
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
            final Operator operator;
            switch (ctx.operator.getType()) {
                case FormulaParser.ADD:
                    operator = Operator.ADD;
                    break;
                case FormulaParser.SUB:
                    operator = Operator.SUB;
                    break;
                case FormulaParser.DIV:
                    operator = Operator.DIV;
                    break;
                case FormulaParser.MUL:
                    operator = Operator.MUL;
                    break;
                default:
                    throw new IllegalStateException("Should not be here");
            }
            result = operator.execute(left, right, numericalContext);
        }
        executionLogger.log(AntlrExecution.Builder.newBuilder()
                .executionId(executionId)
                .using(ctx)
                .appendInput(new InputName("left"), left)
                .appendInput(new InputName("right"), right)
                .result(result)
                .build());
        return result;
    }

    @Override
    public Value visitComparisonFunctionsNumerical(final FormulaParser.ComparisonFunctionsNumericalContext ctx) {
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
            final NumericalComparator numericalComparator;
            switch (ctx.numericalComparator.getType()) {
                case FormulaParser.GT:
                    numericalComparator = NumericalComparator.GT;
                    break;
                case FormulaParser.GTE:
                    numericalComparator = NumericalComparator.GTE;
                    break;
                case FormulaParser.LT:
                    numericalComparator = NumericalComparator.LT;
                    break;
                case FormulaParser.LTE:
                    numericalComparator = NumericalComparator.LTE;
                    break;
                default:
                    throw new IllegalStateException("Should not be here");
            }
            result = numericalComparator.execute(left, right, numericalContext);
        }
        executionLogger.log(AntlrExecution.Builder.newBuilder()
                .executionId(executionId)
                .using(ctx)
                .appendInput(new InputName("left"), left)
                .appendInput(new InputName("right"), right)
                .result(result)
                .build());
        return result;
    }

    @Override
    public Value visitComparisonFunctionsEquality(final FormulaParser.ComparisonFunctionsEqualityContext ctx) {
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
            final EqualityComparator equalityComparator;
            switch (ctx.equalityComparator.getType()) {
                case FormulaParser.EQ:
                    equalityComparator = EqualityComparator.EQ;
                    break;
                case FormulaParser.NEQ:
                    equalityComparator = EqualityComparator.NEQ;
                    break;
                default:
                    throw new IllegalStateException("Should not be here");
            }
            result = equalityComparator.execute(left, right, numericalContext);
        }
        executionLogger.log(AntlrExecution.Builder.newBuilder()
                .executionId(executionId)
                .using(ctx)
                .appendInput(new InputName("left"), left)
                .appendInput(new InputName("right"), right)
                .result(result)
                .build());
        return result;
    }

    @Override
    public Value visitLogicalOperatorFunction(final FormulaParser.LogicalOperatorFunctionContext ctx) {
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
            final LogicalOperator logicalOperator;
            switch (ctx.logicalOperator.getType()) {
                case FormulaParser.AND:
                    logicalOperator = LogicalOperator.AND;
                    break;
                case FormulaParser.OR:
                    logicalOperator = LogicalOperator.OR;
                    break;
                default:
                    throw new IllegalStateException("Should not be here");
            }
            result = logicalOperator.execute(left, right);
        }
        executionLogger.log(AntlrExecution.Builder.newBuilder()
                .executionId(executionId)
                .using(ctx)
                .appendInput(new InputName("left"), left)
                .appendInput(new InputName("right"), right)
                .result(result)
                .build());
        return result;
    }

    @Override
    public Value visitIfFunction(final FormulaParser.IfFunctionContext ctx) {
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
        executionLogger.log(AntlrExecution.Builder.newBuilder()
                .executionId(executionId)
                .using(ctx)
                .appendInput(new InputName("comparisonValue"), comparisonValue)
                .result(result)
                .build());
        return result;
    }

    @Override
    public Value visitIfErrorFunction(final FormulaParser.IfErrorFunctionContext ctx) {
        final Value comparisonValue = this.visit(ctx.comparison);
        final Value result;
        if (comparisonValue.isError()) {
            result = this.visit(ctx.whenTrue);
        } else {
            result = this.visit(ctx.whenFalse);
        }
        executionLogger.log(AntlrExecution.Builder.newBuilder()
                .executionId(executionId)
                .using(ctx)
                .appendInput(new InputName("comparisonValue"), comparisonValue)
                .result(result)
                .build());
        return result;
    }

    @Override
    public Value visitIfNaFunction(final FormulaParser.IfNaFunctionContext ctx) {
        final Value comparisonValue = this.visit(ctx.comparison);
        final Value result;
        if (comparisonValue.isNotAvailable()) {
            result = this.visit(ctx.whenTrue);
        } else {
            result = this.visit(ctx.whenFalse);
        }
        executionLogger.log(AntlrExecution.Builder.newBuilder()
                .executionId(executionId)
                .using(ctx)
                .appendInput(new InputName("comparisonValue"), comparisonValue)
                .result(result)
                .build());
        return result;
    }

    @Override
    public Value visitIsFunction(final FormulaParser.IsFunctionContext ctx) {
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
            switch (ctx.isOperator.getType()) {
                case FormulaParser.ISNUM:
                    result = value.isNumeric() ? Value.ofTrue() : Value.ofFalse();
                    break;
                case FormulaParser.ISTEXT:
                    result = value.isText() ? Value.ofTrue() : Value.ofFalse();
                    break;
                case FormulaParser.ISBLANK:
                    result = value.isBlank() ? Value.ofTrue() : Value.ofFalse();
                    break;
                case FormulaParser.ISLOGICAL:
                    result = value.isLogical() ? Value.ofTrue() : Value.ofFalse();
                    break;
                default:
                    throw new IllegalStateException("Should not be here");
            }
        }
        executionLogger.log(AntlrExecution.Builder.newBuilder()
                .executionId(executionId)
                .using(ctx)
                .appendInput(new InputName("value"), value)
                .result(result)
                .build());
        return result;
    }

    @Override
    public Value visitIsNaFunction(final FormulaParser.IsNaFunctionContext ctx) {
        final Value value = this.visit(ctx.value);
        final Value result = value.isNotAvailable() ? Value.ofTrue() : Value.ofFalse();
        executionLogger.log(AntlrExecution.Builder.newBuilder()
                .executionId(executionId)
                .using(ctx)
                .appendInput(new InputName("value"), value)
                .result(result)
                .build());
        return result;
    }

    @Override
    public Value visitIsErrorFunction(final FormulaParser.IsErrorFunctionContext ctx) {
        final Value value = this.visit(ctx.value);
        final Value result = value.isError() ? Value.ofTrue() : Value.ofFalse();
        executionLogger.log(AntlrExecution.Builder.newBuilder()
                .executionId(executionId)
                .using(ctx)
                .appendInput(new InputName("value"), value)
                .result(result)
                .build());
        return result;
    }

    @Override
    public Value visitArgumentStructuredReference(final FormulaParser.ArgumentStructuredReferenceContext ctx) {
        Value result;
        final String reference = ctx.STRUCTURED_REFERENCE().getText()
                .substring(0, ctx.STRUCTURED_REFERENCE().getText().length() - 2)
                .substring(3);
        try {
            result = this.structuredData.getValueByReference(new Reference(reference));
        } catch (final UnknownReferenceException unknownReferenceException) {
            result = Value.ofUnknownRef();
        }
        executionLogger.log(AntlrExecution.Builder.newBuilder()
                .executionId(executionId)
                .using(ctx)
                .appendInput(new InputName("structuredReference"), new Reference(reference))
                .result(result)
                .build());
        return result;
    }

    @Override
    public Value visitArgumentValue(final FormulaParser.ArgumentValueContext ctx) {
        final Value result = Value.of(ctx.VALUE().getText());
        executionLogger.log(AntlrExecution.Builder.newBuilder()
                .executionId(executionId)
                .using(ctx)
                .result(result)
                .build());
        return result;
    }

    @Override
    public Value visitArgumentNumeric(final FormulaParser.ArgumentNumericContext ctx) {
        final Value result = Value.of(ctx.NUMERIC().getText());
        executionLogger.log(AntlrExecution.Builder.newBuilder()
                .executionId(executionId)
                .using(ctx)
                .result(result)
                .build());
        return result;
    }

    @Override
    public Value visitArgumentBooleanTrue(final FormulaParser.ArgumentBooleanTrueContext ctx) {
        // Can be TRUE or 1 ... cannot return Value.ofTrue() because 1 will not be a numeric anymore
        final Value result = Value.of(ctx.getText());
        executionLogger.log(AntlrExecution.Builder.newBuilder()
                .executionId(executionId)
                .using(ctx)
                .result(result)
                .build());
        return result;
    }

    @Override
    public Value visitArgumentBooleanFalse(final FormulaParser.ArgumentBooleanFalseContext ctx) {
        // Can be FALSE or 0 ... cannot return Value.ofFalse() because 0 will not be a numeric anymore
        final Value result = Value.of(ctx.getText());
        executionLogger.log(AntlrExecution.Builder.newBuilder()
                .executionId(executionId)
                .using(ctx)
                .result(result)
                .build());
        return result;
    }
}
