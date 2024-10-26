package com.damdamdeo.formula.domain.evaluation;

import com.damdamdeo.formula.domain.Evaluated;

public interface ExpressionVisitor {
    Evaluated visit(ArithmeticExpression arithmeticExpression);

    Evaluated visit(ComparisonExpression comparisonExpression);

    Evaluated visit(LogicalBooleanExpression logicalBooleanExpression);

    Evaluated visit(LogicalComparisonExpression logicalComparisonExpression);

    Evaluated visit(StateExpression stateExpression);

    Evaluated visit(StructuredReferencesExpression structuredReferencesExpression);

    Evaluated visit(ArgumentExpression argumentExpression);
}
