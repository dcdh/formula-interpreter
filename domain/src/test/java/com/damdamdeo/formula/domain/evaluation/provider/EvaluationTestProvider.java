package com.damdamdeo.formula.domain.evaluation.provider;

import com.damdamdeo.formula.domain.ArithmeticFunction;
import com.damdamdeo.formula.domain.Formula;
import com.damdamdeo.formula.domain.PositionedAt;
import com.damdamdeo.formula.domain.Value;
import com.damdamdeo.formula.domain.evaluation.ArgumentExpression;
import com.damdamdeo.formula.domain.evaluation.ArithmeticExpression;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

public class EvaluationTestProvider {

    public static Stream<Arguments> provideExpressions() {
        return Stream.of(
                Arguments.of(
                        new Formula("ADD(10,MUL(20,30))"),
                        new ArithmeticExpression(ArithmeticFunction.Function.ADD,
                                new ArgumentExpression(new Value("10"), new PositionedAt(4, 5)),
                                new ArithmeticExpression(ArithmeticFunction.Function.MUL,
                                        new ArgumentExpression(new Value("20"), new PositionedAt(11, 12)),
                                        new ArgumentExpression(new Value("30"), new PositionedAt(14, 15)),
                                        new PositionedAt(7, 16)),
                                new PositionedAt(0, 17)),
                        new Value("610"))
        );
    }

}
