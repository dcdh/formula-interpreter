package com.damdamdeo.formula;

import com.damdamdeo.formula.structuredreference.Reference;
import com.damdamdeo.formula.structuredreference.StructuredData;
import com.damdamdeo.formula.structuredreference.StructuredDatum;
import com.damdamdeo.formula.syntax.SyntaxErrorException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class LogicalFunctionsExpressionTest extends AbstractExpressionTest {

    @Nested
    public class LogicalAndOr {
        @ParameterizedTest
        @MethodSource("provideLogicalFunctionsWithExpectedValues")
        public void shouldExecuteLogicalFunctionsForStructuredReferenceLeftAndStructuredReferenceRight(
                final String leftValue,
                final String givenLogicalFunction,
                final String rightValue,
                final String expectedValue) throws SyntaxErrorException {
            // Given
            final String givenFormula = String.format("%s([@[North Sales Amount]],[@[South Sales Amount]])", givenLogicalFunction);
            final StructuredData givenStructuredData = new StructuredData(
                    List.of(
                            new StructuredDatum(new Reference("North Sales Amount"), leftValue),
                            new StructuredDatum(new Reference("South Sales Amount"), rightValue)
                    )
            );

            // When
            final ExecutionResult executionResult = executor.execute(formula4Test(givenFormula), givenStructuredData);

            // Then
            assertThat(executionResult.result()).isEqualTo(
                    new Value(expectedValue));
        }

        @ParameterizedTest
        @MethodSource("provideLogicalFunctionsWithExpectedValues")
        public void shouldExecuteLogicalFunctionsForStructuredReferenceLeftAndValueRight(final String leftValue,
                                                                                         final String givenLogicalFunction,
                                                                                         final String rightValue,
                                                                                         final String expectedValue) throws SyntaxErrorException {
            // Given
            final String givenFormula = String.format("%s([@[North Sales Amount]],%s)", givenLogicalFunction, rightValue);
            final StructuredData givenStructuredData = new StructuredData(
                    List.of(
                            new StructuredDatum(new Reference("North Sales Amount"), leftValue)
                    )
            );

            // When
            final ExecutionResult executionResult = executor.execute(formula4Test(givenFormula), givenStructuredData);

            // Then
            assertThat(executionResult.result()).isEqualTo(
                    new Value(expectedValue));
        }

        @ParameterizedTest
        @MethodSource("provideLogicalFunctionsWithExpectedValues")
        public void shouldExecuteLogicalFunctionsForValueLeftAndStructuredReferenceRight(final String leftValue,
                                                                                         final String givenLogicalFunction,
                                                                                         final String rightValue,
                                                                                         final String expectedValue) throws SyntaxErrorException {
            // Given
            final String givenFormula = String.format("%s(%s,[@[South Sales Amount]])", givenLogicalFunction, leftValue);
            final StructuredData givenStructuredData = new StructuredData(
                    List.of(
                            new StructuredDatum(new Reference("South Sales Amount"), rightValue)
                    )
            );

            // When
            final ExecutionResult executionResult = executor.execute(formula4Test(givenFormula), givenStructuredData);

            // Then
            assertThat(executionResult.result()).isEqualTo(
                    new Value(expectedValue));
        }

        @ParameterizedTest
        @MethodSource("provideLogicalFunctionsWithExpectedValues")
        public void shouldExecuteLogicalFunctionsForValueLeftAndValueRight(final String leftValue,
                                                                           final String givenLogicalFunction,
                                                                           final String rightValue,
                                                                           final String expectedValue) throws SyntaxErrorException {
            // Given
            final String givenFormula = String.format("%s(%s,%s)", givenLogicalFunction, leftValue, rightValue);
            final StructuredData givenStructuredData = new StructuredData(List.of());

            // When
            final ExecutionResult executionResult = executor.execute(formula4Test(givenFormula), givenStructuredData);

            // Then
            assertThat(executionResult.result()).isEqualTo(
                    new Value(expectedValue));
        }

        private static Stream<Arguments> provideLogicalFunctionsWithExpectedValues() {
            return Stream.of(
                    Arguments.of("0", "AND", "0", "false"),
                    Arguments.of("0", "AND", "1", "false"),
                    Arguments.of("1", "AND", "0", "false"),
                    Arguments.of("1", "AND", "1", "true"),
                    Arguments.of("1", "AND", "\"true\"", "true"),
                    Arguments.of("\"true\"", "AND", "1", "true"),
                    Arguments.of("\"true\"", "AND", "\"true\"", "true"),
                    Arguments.of("0", "AND", "\"true\"", "false"),
                    Arguments.of("\"true\"", "AND", "\"boom\"", "false"),
                    Arguments.of("true", "AND", "1", "true"),
                    Arguments.of("true", "AND", "true", "true"),
                    Arguments.of("0", "AND", "true", "false"),
                    Arguments.of("true", "AND", "\"boom\"", "false"),
                    Arguments.of("0", "OR", "0", "false"),
                    Arguments.of("0", "OR", "1", "true"),
                    Arguments.of("1", "OR", "0", "true"),
                    Arguments.of("1", "OR", "1", "true"),
                    Arguments.of("1", "OR", "\"true\"", "true"),
                    Arguments.of("\"true\"", "OR", "1", "true"),
                    Arguments.of("\"true\"", "OR", "\"true\"", "true"),
                    Arguments.of("0", "OR", "\"true\"", "true"),
                    Arguments.of("\"true\"", "OR", "\"boom\"", "true"),
                    Arguments.of("true", "OR", "1", "true"),
                    Arguments.of("true", "OR", "true", "true"),
                    Arguments.of("0", "OR", "true", "true"),
                    Arguments.of("true", "OR", "\"boom\"", "true")
            );
        }

        @ParameterizedTest
        @EnumSource(LogicalOperator.class)
        public void shouldBeUnknownWhenOneStructuredReferenceIsUnknown(final LogicalOperator givenLogicalFunction) throws
                SyntaxErrorException {
            // Given
            final String givenFormula = String.format("%s([@[North Sales Amount]],[@[South Sales Amount]])", givenLogicalFunction.name());
            final StructuredData givenStructuredData = new StructuredData(
                    List.of(
                            new StructuredDatum(new Reference("North Sales Amount"), "660")
                    )
            );

            // When
            final ExecutionResult executionResult = executor.execute(formula4Test(givenFormula), givenStructuredData);

            // Then
            assertThat(executionResult.result()).isEqualTo(
                    new Value("#REF!"));
        }

        @ParameterizedTest
        @EnumSource(LogicalOperator.class)
        public void shouldBeNotAvailableWhenLeftStructuredReferenceIsNull(final LogicalOperator givenLogicalFunction) throws
                SyntaxErrorException {
            // Given
            final String givenFormula = String.format("%s([@[North Sales Amount]],[@[South Sales Amount]])", givenLogicalFunction.name());
            final StructuredData givenStructuredData = new StructuredData(
                    List.of(
                            new StructuredDatum(new Reference("North Sales Amount"), null),
                            new StructuredDatum(new Reference("South Sales Amount"), "260")

                    )
            );

            // When
            final ExecutionResult executionResult = executor.execute(formula4Test(givenFormula), givenStructuredData);

            // Then
            assertThat(executionResult.result()).isEqualTo(
                    new Value("#NA!"));
        }

        @ParameterizedTest
        @EnumSource(LogicalOperator.class)
        public void shouldBeNotAvailableWhenRightStructuredReferenceIsNull(final LogicalOperator givenLogicalFunction) throws
                SyntaxErrorException {
            // Given
            final String givenFormula = String.format("%s([@[North Sales Amount]],[@[South Sales Amount]])", givenLogicalFunction.name());
            final StructuredData givenStructuredData = new StructuredData(
                    List.of(
                            new StructuredDatum(new Reference("North Sales Amount"), "660"),
                            new StructuredDatum(new Reference("South Sales Amount"), null)
                    )
            );

            // When
            final ExecutionResult executionResult = executor.execute(formula4Test(givenFormula), givenStructuredData);

            // Then
            assertThat(executionResult.result()).isEqualTo(
                    new Value("#NA!"));
        }

        @ParameterizedTest
        @MethodSource("provideLogicalFunctionsUsingComparisonsFunction")
        public void shouldUseComparisonsFunctions(final String givenFormula) throws SyntaxErrorException {
            // Given
            final StructuredData givenStructuredData = new StructuredData(
                    List.of(
                            new StructuredDatum(new Reference("North Sales Amount"), "660")
                    )
            );

            // When
            final ExecutionResult executionResult = executor.execute(formula4Test(givenFormula), givenStructuredData);

            // Then
            assertThat(executionResult.result()).isEqualTo(
                    new Value("true"));
        }

        private static Stream<Arguments> provideLogicalFunctionsUsingComparisonsFunction() {
            return Stream.of(
                    Arguments.of("""
                            OR(EQ("true","true"),EQ("true","true"))"""),
                    Arguments.of("""
                            AND(EQ("true","true"),EQ("true","true"))"""),
                    Arguments.of("""
                            OR(EQ(true,true),EQ(true,true))"""),
                    Arguments.of("""
                            AND(EQ(true,true),EQ(true,true))"""));
        }
    }

    @Nested
    public class LogicalIf {

        @ParameterizedTest
        @MethodSource("provideComparisons")
        public void shouldComputeIf(final String givenIfFormula, final String expectedResult) throws SyntaxErrorException {
            // Given
            final StructuredData givenStructuredData = new StructuredData(List.of());

            // When
            final ExecutionResult executionResult = executor.execute(formula4Test(givenIfFormula), givenStructuredData);

            // Then
            assertThat(executionResult.result()).isEqualTo(
                    new Value(expectedResult));
        }

        private static Stream<Arguments> provideComparisons() {
            return Stream.of(
                    Arguments.of("""
                            IF("true","true","false")""", "true"),
                    Arguments.of("""
                            IF("false","true","false")""", "false"),
                    Arguments.of("""
                            IF("1","true","false")""", "true"),
                    Arguments.of("""
                            IF("0","true","false")""", "false"),
                    Arguments.of("""
                            IF("false",ADD(1,1),ADD(2,2))""", "4"),
                    Arguments.of("""
                            IF("true",ADD(1,1),ADD(2,2))""", "2"),
                    Arguments.of("""
                            IF(EQ(1,1),ADD(1,1),ADD(2,2))""", "2"),
                    Arguments.of("""
                            IF(true,true,false)""", "true"),
                    Arguments.of("""
                            IF(false,true,false)""", "false"),
                    Arguments.of("""
                            IF(1,true,false)""", "true"),
                    Arguments.of("""
                            IF(0,true,false)""", "false"),
                    Arguments.of("""
                            IF(false,ADD(1,1),ADD(2,2))""", "4"),
                    Arguments.of("""
                            IF(true,ADD(1,1),ADD(2,2))""", "2")
            );
        }

        @Test
        public void shouldBeUnknownWhenStructuredReferenceIsUnknown() throws
                SyntaxErrorException {
            // Given
            final String givenFormula = """
                    IF([@[North Sales Amount]],"true","false")""";
            final StructuredData givenStructuredData = new StructuredData(List.of());

            // When
            final ExecutionResult executionResult = executor.execute(formula4Test(givenFormula), givenStructuredData);

            // Then
            assertThat(executionResult.result()).isEqualTo(
                    new Value("#REF!"));
        }

        @Test
        public void shouldBeNotAvailableWhenStructuredReferenceIsNull() throws
                SyntaxErrorException {
            // Given
            final String givenFormula = """
                    IF([@[North Sales Amount]],"true","false")""";
            final StructuredData givenStructuredData = new StructuredData(
                    List.of(
                            new StructuredDatum(new Reference("North Sales Amount"), null)
                    )
            );

            // When
            final ExecutionResult executionResult = executor.execute(formula4Test(givenFormula), givenStructuredData);

            // Then
            assertThat(executionResult.result()).isEqualTo(
                    new Value("#NA!"));
        }
    }

    @Nested
    public class LogicalIsNum {

        @ParameterizedTest
        @MethodSource("provideValues")
        public void shouldComputeIf(final String givenValue, final String expectedResult) throws SyntaxErrorException {
            // Given
            final String givenFormula = String.format("""
                    ISNUM("%s")
                    """, givenValue);
            final StructuredData givenStructuredData = new StructuredData(List.of());

            // When
            final ExecutionResult executionResult = executor.execute(formula4Test(givenFormula), givenStructuredData);

            // Then
            assertThat(executionResult.result()).isEqualTo(
                    new Value(expectedResult));
        }

        @ParameterizedTest
        @MethodSource("provideValues")
        public void shouldComputeIfOnStructuredReference(final String givenValue, final String expectedResult) throws SyntaxErrorException {
            // Given
            final String givenFormula = "ISNUM([@[% Commission]])";
            final StructuredData givenStructuredData = new StructuredData(List.of(
                    new StructuredDatum(new Reference("% Commission"), givenValue)
            ));

            // When
            final ExecutionResult executionResult = executor.execute(formula4Test(givenFormula), givenStructuredData);

            // Then
            assertThat(executionResult.result()).isEqualTo(
                    new Value(expectedResult));
        }

        private static Stream<Arguments> provideValues() {
            return Stream.of(
                    Arguments.of("123456", "true"),
                    Arguments.of("azerty", "false"));
        }

        @Test
        public void shouldBeUnknownWhenOneStructuredReferenceIsUnknown() throws SyntaxErrorException {
            // Given
            final String givenFormula = "ISNUM([@[% Commission]])";
            final StructuredData givenStructuredData = new StructuredData(List.of());

            // When
            final ExecutionResult executionResult = executor.execute(formula4Test(givenFormula), givenStructuredData);

            // Then
            assertThat(executionResult.result()).isEqualTo(
                    new Value("#REF!"));
        }

        @Test
        public void shouldBeNotAvailableWhenRightStructuredReferenceIsNull() throws SyntaxErrorException {
            // Given
            final String givenFormula = "ISNUM([@[% Commission]])";
            final StructuredData givenStructuredData = new StructuredData(
                    List.of(
                            new StructuredDatum(new Reference("% Commission"), null)
                    )
            );

            // When
            final ExecutionResult executionResult = executor.execute(formula4Test(givenFormula), givenStructuredData);

            // Then
            assertThat(executionResult.result()).isEqualTo(
                    new Value("#NA!"));
        }

    }
}
