package com.damdamdeo.formula.infrastructure.antlr;

import com.damdamdeo.formula.domain.*;
import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class LogicalFunctionsExpressionTest extends AbstractExecutionTest {

    @Nested
    public class AndOr {
        @ParameterizedTest
        @MethodSource("provideLogicalFunctionsWithExpectedValues")
        public void shouldExecuteLogicalFunctionsForStructuredReferenceLeftAndStructuredReferenceRight(
                final String leftValue,
                final String givenLogicalFunction,
                final String rightValue,
                final String expectedValue) {
            // Given
            final String givenFormula = String.format("%s([@[North Sales Amount]],[@[South Sales Amount]])", givenLogicalFunction);
            final StructuredData givenStructuredData = new StructuredData(
                    List.of(
                            new StructuredDatum(new Reference("North Sales Amount"), leftValue),
                            new StructuredDatum(new Reference("South Sales Amount"), rightValue)
                    )
            );

            // When
            final Uni<ExecutionResult> executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredData,
                    new NoOpExecutionWrapper());

            // Then
            assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                    assertThat(executionResultToAssert.result())
                            .isEqualTo(new Value(expectedValue))
            );
        }

        @ParameterizedTest
        @MethodSource("provideLogicalFunctionsWithExpectedValues")
        public void shouldExecuteLogicalFunctionsForStructuredReferenceLeftAndValueRight(final String leftValue,
                                                                                         final String givenLogicalFunction,
                                                                                         final String rightValue,
                                                                                         final String expectedValue) {
            // Given
            final String givenFormula = String.format("%s([@[North Sales Amount]],%s)", givenLogicalFunction, rightValue);
            final StructuredData givenStructuredData = new StructuredData(
                    List.of(
                            new StructuredDatum(new Reference("North Sales Amount"), leftValue)
                    )
            );

            // When
            final Uni<ExecutionResult> executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredData,
                    new NoOpExecutionWrapper());

            // Then
            assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                    assertThat(executionResultToAssert.result())
                            .isEqualTo(new Value(expectedValue))
            );
        }

        @ParameterizedTest
        @MethodSource("provideLogicalFunctionsWithExpectedValues")
        public void shouldExecuteLogicalFunctionsForValueLeftAndStructuredReferenceRight(final String leftValue,
                                                                                         final String givenLogicalFunction,
                                                                                         final String rightValue,
                                                                                         final String expectedValue) {
            // Given
            final String givenFormula = String.format("%s(%s,[@[South Sales Amount]])", givenLogicalFunction, leftValue);
            final StructuredData givenStructuredData = new StructuredData(
                    List.of(
                            new StructuredDatum(new Reference("South Sales Amount"), rightValue)
                    )
            );

            // When
            final Uni<ExecutionResult> executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredData,
                    new NoOpExecutionWrapper());

            // Then
            assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                    assertThat(executionResultToAssert.result())
                            .isEqualTo(new Value(expectedValue))
            );
        }

        @ParameterizedTest
        @MethodSource("provideLogicalFunctionsWithExpectedValues")
        public void shouldExecuteLogicalFunctionsForValueLeftAndValueRight(final String leftValue,
                                                                           final String givenLogicalFunction,
                                                                           final String rightValue,
                                                                           final String expectedValue) {
            // Given
            final String givenFormula = String.format("%s(%s,%s)", givenLogicalFunction, leftValue, rightValue);
            final StructuredData givenStructuredData = new StructuredData(List.of());

            // When
            final Uni<ExecutionResult> executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredData,
                    new NoOpExecutionWrapper());

            // Then
            assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                    assertThat(executionResultToAssert.result())
                            .isEqualTo(new Value(expectedValue))
            );
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
        public void shouldBeUnknownWhenOneStructuredReferenceIsUnknown(final LogicalOperator givenLogicalFunction) {
            // Given
            final String givenFormula = String.format("%s([@[North Sales Amount]],[@[South Sales Amount]])", givenLogicalFunction.name());
            final StructuredData givenStructuredData = new StructuredData(
                    List.of(
                            new StructuredDatum(new Reference("North Sales Amount"), "660")
                    )
            );

            // When
            final Uni<ExecutionResult> executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredData,
                    new NoOpExecutionWrapper());

            // Then
            assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                    assertThat(executionResultToAssert.result())
                            .isEqualTo(new Value("#REF!"))
            );
        }

        @ParameterizedTest
        @EnumSource(LogicalOperator.class)
        public void shouldBeNotAvailableWhenLeftStructuredReferenceIsNull(final LogicalOperator givenLogicalFunction) {
            // Given
            final String givenFormula = String.format("%s([@[North Sales Amount]],[@[South Sales Amount]])", givenLogicalFunction.name());
            final StructuredData givenStructuredData = new StructuredData(
                    List.of(
                            new StructuredDatum(new Reference("North Sales Amount"), (String) null),
                            new StructuredDatum(new Reference("South Sales Amount"), "260")

                    )
            );

            // When
            final Uni<ExecutionResult> executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredData,
                    new NoOpExecutionWrapper());

            // Then
            assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                    assertThat(executionResultToAssert.result())
                            .isEqualTo(new Value("#NA!"))
            );
        }

        @ParameterizedTest
        @EnumSource(LogicalOperator.class)
        public void shouldBeNotAvailableWhenRightStructuredReferenceIsNull(final LogicalOperator givenLogicalFunction) {
            // Given
            final String givenFormula = String.format("%s([@[North Sales Amount]],[@[South Sales Amount]])", givenLogicalFunction.name());
            final StructuredData givenStructuredData = new StructuredData(
                    List.of(
                            new StructuredDatum(new Reference("North Sales Amount"), "660"),
                            new StructuredDatum(new Reference("South Sales Amount"), (String) null)
                    )
            );

            // When
            final Uni<ExecutionResult> executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredData,
                    new NoOpExecutionWrapper());

            // Then
            assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                    assertThat(executionResultToAssert.result())
                            .isEqualTo(new Value("#NA!"))
            );
        }

        @ParameterizedTest
        @MethodSource("provideLogicalFunctionsUsingComparisonsFunction")
        public void shouldUseComparisonsFunctions(final String givenFormula) {
            // Given
            final StructuredData givenStructuredData = new StructuredData(
                    List.of(
                            new StructuredDatum(new Reference("North Sales Amount"), "660")
                    )
            );

            // When
            final Uni<ExecutionResult> executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredData,
                    new NoOpExecutionWrapper());

            // Then
            assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                    assertThat(executionResultToAssert.result())
                            .isEqualTo(new Value("true"))
            );
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

        @Test
        public void shouldLogExecution() {
            // Given
            final String givenFormula = "AND(0,0)";
            final StructuredData givenStructuredData = new StructuredData(List.of());
            when(executedAtProvider.now())
                    .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")))
                    .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")))
                    .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:02+01:00[Europe/Paris]")))
                    .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")))
                    .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:04+01:00[Europe/Paris]")))
                    .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:05+01:00[Europe/Paris]")))
                    .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:06+01:00[Europe/Paris]")))
                    .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:07+01:00[Europe/Paris]")));

            // When
            final Uni<ExecutionResult> executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredData,
                    new LoggingExecutionWrapper(executedAtProvider));

            // Then
            assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                    assertThat(executionResultToAssert.elementExecutions()).containsExactly(
                            new ElementExecution(
                                    Value.of("false"),
                                    new Position(0, 7),
                                    Map.of(
                                            new InputName("left"), Value.of("0"),
                                            new InputName("right"), Value.of("0")),
                                    new ExecutionProcessedIn(
                                            new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")),
                                            new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:06+01:00[Europe/Paris]")))),
                            new ElementExecution(
                                    Value.of("0"),
                                    new Position(4, 4),
                                    Map.of(),
                                    new ExecutionProcessedIn(
                                            new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:02+01:00[Europe/Paris]")),
                                            new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")))),
                            new ElementExecution(
                                    Value.of("0"),
                                    new Position(6, 6),
                                    Map.of(),
                                    new ExecutionProcessedIn(
                                            new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:04+01:00[Europe/Paris]")),
                                            new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:05+01:00[Europe/Paris]"))))
                    )
            );
        }
    }

    @Nested
    public class LogicalIf {

        @ParameterizedTest
        @MethodSource("provideComparisons")
        public void shouldComputeIf(final String givenIfFormula, final String expectedValue) {
            // Given
            final StructuredData givenStructuredData = new StructuredData(List.of());

            // When
            final Uni<ExecutionResult> executionResult = antlrExecutor.execute(formula4Test(givenIfFormula), givenStructuredData,
                    new NoOpExecutionWrapper());

            // Then
            assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                    assertThat(executionResultToAssert.result())
                            .isEqualTo(new Value(expectedValue))
            );
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
        public void shouldBeUnknownWhenStructuredReferenceIsUnknown() {
            // Given
            final String givenFormula = """
                    IF([@[North Sales Amount]],"true","false")""";
            final StructuredData givenStructuredData = new StructuredData(List.of());

            // When
            final Uni<ExecutionResult> executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredData,
                    new NoOpExecutionWrapper());

            // Then
            assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                    assertThat(executionResultToAssert.result())
                            .isEqualTo(new Value("#REF!"))
            );
        }

        @Test
        public void shouldBeNotAvailableWhenStructuredReferenceIsNull() {
            // Given
            final String givenFormula = """
                    IF([@[North Sales Amount]],"true","false")""";
            final StructuredData givenStructuredData = new StructuredData(
                    List.of(
                            new StructuredDatum(new Reference("North Sales Amount"), (String) null)
                    )
            );

            // When
            final Uni<ExecutionResult> executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredData,
                    new NoOpExecutionWrapper());

            // Then
            assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                    assertThat(executionResultToAssert.result())
                            .isEqualTo(new Value("#NA!"))
            );
        }

        @Test
        public void shouldLogExecution() {
            // Given
            final String givenFormula = "IF(\"true\",\"true\",\"false\")";
            final StructuredData givenStructuredData = new StructuredData(List.of());
            when(executedAtProvider.now())
                    .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")))
                    .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")))
                    .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:02+01:00[Europe/Paris]")))
                    .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")))
                    .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:04+01:00[Europe/Paris]")))
                    .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:05+01:00[Europe/Paris]")))
                    .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:06+01:00[Europe/Paris]")))
                    .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:07+01:00[Europe/Paris]")));

            // When
            final Uni<ExecutionResult> executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredData,
                    new LoggingExecutionWrapper(executedAtProvider));

            // Then
            assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                    assertThat(executionResultToAssert.elementExecutions()).containsExactly(
                            new ElementExecution(
                                    Value.of("true"),
                                    new Position(0, 24),
                                    Map.of(
                                            new InputName("comparisonValue"), Value.of("true")),
                                    new ExecutionProcessedIn(
                                            new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")),
                                            new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:06+01:00[Europe/Paris]")))),
                            new ElementExecution(
                                    Value.of("true"),
                                    new Position(3, 8),
                                    Map.of(),
                                    new ExecutionProcessedIn(
                                            new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:02+01:00[Europe/Paris]")),
                                            new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")))),
                            new ElementExecution(
                                    Value.of("true"),
                                    new Position(10, 15),
                                    Map.of(),
                                    new ExecutionProcessedIn(
                                            new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:04+01:00[Europe/Paris]")),
                                            new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:05+01:00[Europe/Paris]"))))
                    )
            );
        }
    }

    @Nested
    class LogicalIfError {

        @ParameterizedTest
        @MethodSource("provideComparisons")
        public void shouldComputeIf(final String givenIfFormula, final String expectedValue) {
            // Given
            final StructuredData givenStructuredData = new StructuredData(List.of());

            // When
            final Uni<ExecutionResult> executionResult = antlrExecutor.execute(formula4Test(givenIfFormula), givenStructuredData,
                    new NoOpExecutionWrapper());

            // Then
            assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                    assertThat(executionResultToAssert.result())
                            .isEqualTo(new Value(expectedValue))
            );
        }

        private static Stream<Arguments> provideComparisons() {
            return Stream.of(
                    Arguments.of("""
                            IFERROR("true","true","false")""", "false"),
                    Arguments.of("""
                            IFERROR("false","true","false")""", "false"),
                    Arguments.of("""
                            IFERROR("1","true","false")""", "false"),
                    Arguments.of("""
                            IFERROR("0","true","false")""", "false"),
                    Arguments.of("""
                            IFERROR("false",ADD(1,1),ADD(2,2))""", "4"),
                    Arguments.of("""
                            IFERROR("true",ADD(1,1),ADD(2,2))""", "4"),
                    Arguments.of("""
                            IFERROR(EQ(1,1),ADD(1,1),ADD(2,2))""", "4"),
                    Arguments.of("""
                            IFERROR(true,true,false)""", "false"),
                    Arguments.of("""
                            IFERROR(false,true,false)""", "false"),
                    Arguments.of("""
                            IFERROR(1,true,false)""", "false"),
                    Arguments.of("""
                            IFERROR(0,true,false)""", "false"),
                    Arguments.of("""
                            IFERROR(false,ADD(1,1),ADD(2,2))""", "4"),
                    Arguments.of("""
                            IFERROR(true,ADD(1,1),ADD(2,2))""", "4"),
                    Arguments.of("""
                            IFERROR("#NA!",true,false)""", "true"),
                    Arguments.of("""
                            IFERROR("#REF!",true,false)""", "true"),
                    Arguments.of("""
                            IFERROR("#NUM!",true,false)""", "true"),
                    Arguments.of("""
                            IFERROR("#DIV/0!",true,false)""", "true")
            );
        }

        @Test
        public void shouldLogExecution() {
            // Given
            final String givenFormula = "IFERROR(\"true\",\"true\",\"false\")";
            final StructuredData givenStructuredData = new StructuredData(List.of());
            when(executedAtProvider.now())
                    .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")))
                    .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")))
                    .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:02+01:00[Europe/Paris]")))
                    .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")))
                    .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:04+01:00[Europe/Paris]")))
                    .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:05+01:00[Europe/Paris]")))
                    .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:06+01:00[Europe/Paris]")))
                    .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:07+01:00[Europe/Paris]")));

            // When
            final Uni<ExecutionResult> executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredData,
                    new LoggingExecutionWrapper(executedAtProvider));

            // Then
            assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                    assertThat(executionResultToAssert.elementExecutions()).containsExactly(
                            new ElementExecution(
                                    Value.of("false"),
                                    new Position(0, 29),
                                    Map.of(
                                            new InputName("comparisonValue"), Value.of("true")
                                    ),
                                    new ExecutionProcessedIn(
                                            new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")),
                                            new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:06+01:00[Europe/Paris]")))),
                            new ElementExecution(
                                    Value.of("true"),
                                    new Position(8, 13),
                                    Map.of(),
                                    new ExecutionProcessedIn(
                                            new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:02+01:00[Europe/Paris]")),
                                            new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")))),
                            new ElementExecution(
                                    Value.of("false"),
                                    new Position(22, 28),
                                    Map.of(),
                                    new ExecutionProcessedIn(
                                            new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:04+01:00[Europe/Paris]")),
                                            new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:05+01:00[Europe/Paris]"))))
                    )
            );
        }
    }

    @Nested
    class LogicalIfNa {

        @ParameterizedTest
        @MethodSource("provideComparisons")
        public void shouldComputeIf(final String givenIfFormula, final String expectedValue) {
            // Given
            final StructuredData givenStructuredData = new StructuredData(List.of());

            // When
            final Uni<ExecutionResult> executionResult = antlrExecutor.execute(formula4Test(givenIfFormula), givenStructuredData,
                    new NoOpExecutionWrapper());

            // Then
            assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                    assertThat(executionResultToAssert.result())
                            .isEqualTo(new Value(expectedValue))
            );
        }

        private static Stream<Arguments> provideComparisons() {
            return Stream.of(
                    Arguments.of("""
                            IFNA("true","true","false")""", "false"),
                    Arguments.of("""
                            IFNA("false","true","false")""", "false"),
                    Arguments.of("""
                            IFNA("1","true","false")""", "false"),
                    Arguments.of("""
                            IFNA("0","true","false")""", "false"),
                    Arguments.of("""
                            IFNA("false",ADD(1,1),ADD(2,2))""", "4"),
                    Arguments.of("""
                            IFNA("true",ADD(1,1),ADD(2,2))""", "4"),
                    Arguments.of("""
                            IFNA(EQ(1,1),ADD(1,1),ADD(2,2))""", "4"),
                    Arguments.of("""
                            IFNA(true,true,false)""", "false"),
                    Arguments.of("""
                            IFNA(false,true,false)""", "false"),
                    Arguments.of("""
                            IFNA(1,true,false)""", "false"),
                    Arguments.of("""
                            IFNA(0,true,false)""", "false"),
                    Arguments.of("""
                            IFNA(false,ADD(1,1),ADD(2,2))""", "4"),
                    Arguments.of("""
                            IFNA(true,ADD(1,1),ADD(2,2))""", "4"),
                    Arguments.of("""
                            IFNA("#NA!",true,false)""", "true"),
                    Arguments.of("""
                            IFNA("#REF!",true,false)""", "false"),
                    Arguments.of("""
                            IFNA("#NUM!",true,false)""", "false"),
                    Arguments.of("""
                            IFNA("#DIV/0!",true,false)""", "false")
            );
        }

        @Test
        public void shouldLogExecution() {
            // Given
            final String givenFormula = "IFNA(\"#REF!\",\"true\",\"false\")";
            final StructuredData givenStructuredData = new StructuredData(List.of());
            when(executedAtProvider.now())
                    .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")))
                    .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")))
                    .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:02+01:00[Europe/Paris]")))
                    .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")))
                    .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:04+01:00[Europe/Paris]")))
                    .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:05+01:00[Europe/Paris]")))
                    .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:06+01:00[Europe/Paris]")))
                    .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:07+01:00[Europe/Paris]")));

            // When
            final Uni<ExecutionResult> executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredData,
                    new LoggingExecutionWrapper(executedAtProvider));

            // Then
            assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                    assertThat(executionResultToAssert.elementExecutions()).containsExactly(
                            new ElementExecution(
                                    Value.of("false"),
                                    new Position(0, 27),
                                    Map.of(
                                            new InputName("comparisonValue"), Value.of("#REF!")
                                    ),
                                    new ExecutionProcessedIn(
                                            new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")),
                                            new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:06+01:00[Europe/Paris]")))),
                            new ElementExecution(
                                    Value.of("#REF!"),
                                    new Position(5, 11),
                                    Map.of(),
                                    new ExecutionProcessedIn(
                                            new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:02+01:00[Europe/Paris]")),
                                            new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")))),
                            new ElementExecution(
                                    Value.of("false"),
                                    new Position(20, 26),
                                    Map.of(),
                                    new ExecutionProcessedIn(
                                            new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:04+01:00[Europe/Paris]")),
                                            new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:05+01:00[Europe/Paris]"))))
                    )
            );
        }
    }

    @Nested
    public class IsFunction {

        @ParameterizedTest
        @MethodSource("provideValues")
        public void shouldCheck(final String givenFunction, final String givenValue, final String expectedValue) {
            // Given
            final String givenFormula = String.format("""
                    %s("%s")
                    """, givenFunction, givenValue);
            final StructuredData givenStructuredData = new StructuredData(List.of());

            // When
            final Uni<ExecutionResult> executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredData,
                    new NoOpExecutionWrapper());

            // Then
            assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                    assertThat(executionResultToAssert.result())
                            .isEqualTo(new Value(expectedValue))
            );
        }

        @ParameterizedTest
        @MethodSource("provideValues")
        public void shouldComputeUsingOnStructuredReference(final String givenFunction, final String givenValue, final String expectedValue) {
            // Given
            final String givenFormula = String.format("%s([@[%% Commission]])", givenFunction);
            final StructuredData givenStructuredData = new StructuredData(List.of(
                    new StructuredDatum(new Reference("% Commission"), givenValue)
            ));

            // When
            final Uni<ExecutionResult> executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredData,
                    new NoOpExecutionWrapper());

            // Then
            assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                    assertThat(executionResultToAssert.result())
                            .isEqualTo(new Value(expectedValue))
            );
        }

        private static Stream<Arguments> provideValues() {
            return Stream.of(
                    Arguments.of("ISNUM", "123456", "true"),
                    Arguments.of("ISNUM", "azerty", "false"),
                    Arguments.of("ISTEXT", "azerty", "true"),
                    Arguments.of("ISTEXT", "123456789", "false"),
                    Arguments.of("ISBLANK", "azerty", "false"),
                    Arguments.of("ISBLANK", "", "true"),
                    Arguments.of("ISBLANK", "123456789", "false"),
                    Arguments.of("ISLOGICAL", "true", "true"),
                    Arguments.of("ISLOGICAL", "1", "true"),
                    Arguments.of("ISLOGICAL", "false", "true"),
                    Arguments.of("ISLOGICAL", "0", "true"),
                    Arguments.of("ISLOGICAL", "azerty", "false"),
                    Arguments.of("ISLOGICAL", "", "false"),
                    Arguments.of("ISLOGICAL", "123456789", "false")
            );
        }

        @ParameterizedTest
        @MethodSource("provideFunctions")
        public void shouldBeUnknownWhenOneStructuredReferenceIsUnknown(final String givenFunction) {
            // Given
            final String givenFormula = String.format("%s([@[%% Commission]])", givenFunction);
            final StructuredData givenStructuredData = new StructuredData(List.of());

            // When
            final Uni<ExecutionResult> executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredData,
                    new NoOpExecutionWrapper());

            // Then
            assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                    assertThat(executionResultToAssert.result())
                            .isEqualTo(new Value("#REF!"))
            );
        }

        @ParameterizedTest
        @MethodSource("provideFunctions")
        public void shouldBeNotAvailableWhenRightStructuredReferenceIsNull(final String givenFunction) {
            // Given
            final String givenFormula = String.format("%s([@[%% Commission]])", givenFunction);
            final StructuredData givenStructuredData = new StructuredData(
                    List.of(
                            new StructuredDatum(new Reference("% Commission"), (String) null)
                    )
            );

            // When
            final Uni<ExecutionResult> executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredData,
                    new NoOpExecutionWrapper());

            // Then
            assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                    assertThat(executionResultToAssert.result())
                            .isEqualTo(new Value("#NA!"))
            );
        }

        private static Stream<Arguments> provideFunctions() {
            return Stream.of(
                    Arguments.of("ISNUM"),
                    Arguments.of("ISTEXT"),
                    Arguments.of("ISBLANK"),
                    Arguments.of("ISLOGICAL")
            );
        }

        @Test
        public void shouldLogExecution() {
            // Given
            final String givenFormula = "ISNUM(\"123456\")";
            final StructuredData givenStructuredData = new StructuredData(List.of());
            when(executedAtProvider.now())
                    .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")))
                    .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")))
                    .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:02+01:00[Europe/Paris]")))
                    .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")))
                    .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:04+01:00[Europe/Paris]")))
                    .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:05+01:00[Europe/Paris]")));

            // When
            final Uni<ExecutionResult> executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredData,
                    new LoggingExecutionWrapper(executedAtProvider));

            // Then
            assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                    assertThat(executionResultToAssert.elementExecutions()).containsExactly(
                            new ElementExecution(
                                    Value.of("true"),
                                    new Position(0, 14),
                                    Map.of(
                                            new InputName("value"), Value.of("123456")
                                    ),
                                    new ExecutionProcessedIn(
                                            new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")),
                                            new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:04+01:00[Europe/Paris]")))),
                            new ElementExecution(
                                    Value.of("123456"),
                                    new Position(6, 13),
                                    Map.of(),
                                    new ExecutionProcessedIn(
                                            new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:02+01:00[Europe/Paris]")),
                                            new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]"))))
                    )
            );
        }
    }

    @Nested
    class IsNa {

        @ParameterizedTest
        @MethodSource("provideValues")
        public void shouldCheck(final String givenValue, final String expectedValue) {
            // Given
            final String givenFormula = String.format("""
                    ISNA("%s")
                    """, givenValue);
            final StructuredData givenStructuredData = new StructuredData(List.of());

            // When
            final Uni<ExecutionResult> executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredData,
                    new NoOpExecutionWrapper());

            // Then
            assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                    assertThat(executionResultToAssert.result())
                            .isEqualTo(new Value(expectedValue))
            );
        }

        private static Stream<Arguments> provideValues() {
            return Stream.of(
                    Arguments.of("123456", "false"),
                    Arguments.of("azerty", "false"),
                    Arguments.of("#NA!", "true")
            );
        }

        @Test
        public void shouldBeFalseWhenOneStructuredReferenceIsUnknown() {
            // Given
            final String givenFormula = "ISNA([@[% Commission]])";
            final StructuredData givenStructuredData = new StructuredData(List.of());

            // When
            final Uni<ExecutionResult> executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredData,
                    new NoOpExecutionWrapper());

            // Then
            assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                    assertThat(executionResultToAssert.result())
                            .isEqualTo(new Value("false"))
            );
        }

        @Test
        public void shouldBeTrueWhenRightStructuredReferenceIsNull() {
            // Given
            final String givenFormula = "ISNA([@[% Commission]])";
            final StructuredData givenStructuredData = new StructuredData(
                    List.of(
                            new StructuredDatum(new Reference("% Commission"), (String) null)
                    )
            );

            // When
            final Uni<ExecutionResult> executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredData,
                    new NoOpExecutionWrapper());

            // Then
            assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                    assertThat(executionResultToAssert.result())
                            .isEqualTo(new Value("true"))
            );
        }

        @Test
        public void shouldLogExecution() {
            // Given
            final String givenFormula = "ISNA(123456)";
            final StructuredData givenStructuredData = new StructuredData(List.of());
            when(executedAtProvider.now())
                    .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")))
                    .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")))
                    .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:02+01:00[Europe/Paris]")))
                    .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")))
                    .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:04+01:00[Europe/Paris]")))
                    .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:05+01:00[Europe/Paris]")));

            // When
            final Uni<ExecutionResult> executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredData,
                    new LoggingExecutionWrapper(executedAtProvider));

            // Then
            assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                    assertThat(executionResultToAssert.elementExecutions()).containsExactly(
                            new ElementExecution(
                                    Value.of("false"),
                                    new Position(0, 11),
                                    Map.of(
                                            new InputName("value"), Value.of("123456")
                                    ),
                                    new ExecutionProcessedIn(
                                            new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")),
                                            new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:04+01:00[Europe/Paris]")))),
                            new ElementExecution(
                                    Value.of("123456"),
                                    new Position(5, 10),
                                    Map.of(),
                                    new ExecutionProcessedIn(
                                            new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:02+01:00[Europe/Paris]")),
                                            new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]"))))
                    )
            );
        }
    }

    @Nested
    public class IsError {

        @ParameterizedTest
        @MethodSource("provideValues")
        public void shouldCheck(final String givenValue, final String expectedValue) {
            // Given
            final String givenFormula = String.format("""
                    ISERROR("%s")
                    """, givenValue);
            final StructuredData givenStructuredData = new StructuredData(List.of());

            // When
            final Uni<ExecutionResult> executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredData,
                    new NoOpExecutionWrapper());

            // Then
            assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                    assertThat(executionResultToAssert.result())
                            .isEqualTo(new Value(expectedValue))
            );
        }

        private static Stream<Arguments> provideValues() {
            return Stream.of(
                    Arguments.of("123456", "false"),
                    Arguments.of("azerty", "false"),
                    Arguments.of("#NA!", "true"),
                    Arguments.of("#REF!", "true"),
                    Arguments.of("#NUM!", "true"),
                    Arguments.of("#DIV/0!", "true")
            );
        }

        @Test
        public void shouldLogExecution() {
            // Given
            final String givenFormula = "ISERROR(123456)";
            final StructuredData givenStructuredData = new StructuredData(List.of());
            when(executedAtProvider.now())
                    .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")))
                    .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")))
                    .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:02+01:00[Europe/Paris]")))
                    .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")))
                    .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:04+01:00[Europe/Paris]")))
                    .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:05+01:00[Europe/Paris]")));

            // When
            final Uni<ExecutionResult> executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredData,
                    new LoggingExecutionWrapper(executedAtProvider));

            // Then
            assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                    assertThat(executionResultToAssert.elementExecutions()).containsExactly(
                            new ElementExecution(
                                    Value.of("false"),
                                    new Position(0, 14),
                                    Map.of(
                                            new InputName("value"), Value.of("123456")
                                    ),
                                    new ExecutionProcessedIn(
                                            new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")),
                                            new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:04+01:00[Europe/Paris]")))),
                            new ElementExecution(
                                    Value.of("123456"),
                                    new Position(8, 13),
                                    Map.of(),
                                    new ExecutionProcessedIn(
                                            new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:02+01:00[Europe/Paris]")),
                                            new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]"))))
                    )
            );
        }
    }
}
