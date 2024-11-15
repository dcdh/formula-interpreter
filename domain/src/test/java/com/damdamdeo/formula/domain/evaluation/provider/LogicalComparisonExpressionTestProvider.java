package com.damdamdeo.formula.domain.evaluation.provider;

import com.damdamdeo.formula.domain.*;
import com.damdamdeo.formula.domain.evaluation.ArgumentExpression;
import com.damdamdeo.formula.domain.evaluation.LogicalComparisonExpression;
import com.damdamdeo.formula.domain.provider.LogicalComparisonFunctionTestProvider;
import org.apache.commons.lang3.Validate;
import org.junit.jupiter.params.provider.Arguments;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class LogicalComparisonExpressionTestProvider {

    private static Map<LogicalComparisonFunction.Function, Supplier<Stream<Arguments>>> ARGUMENTS_BY_LOGICAL_COMPARISON_FUNC = Map.of(
            LogicalComparisonFunction.Function.IF, LogicalComparisonFunctionTestProvider::provideIf,
            LogicalComparisonFunction.Function.IF_ERROR, LogicalComparisonFunctionTestProvider::provideIfError,
            LogicalComparisonFunction.Function.IF_NOT_AVAILABLE, LogicalComparisonFunctionTestProvider::provideIfNotAvailable
    );

    private static Map<Formula, List<IntermediateResult>> INTERMEDIATE_RESULTS_BY_FORMULA = new HashMap<>();

    static {
        INTERMEDIATE_RESULTS_BY_FORMULA.put(new Formula("IF(\"#NA!\",true,false)"), List.of(
                new IntermediateResult(
                        new Value("#NA!"),
                        new PositionedAt(4, 4),
                        List.of(
                                new Input(new InputName("comparisonValue"), new Value("#NA!"), new PositionedAt(1, 1))
                        ),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")))
                ),
                new IntermediateResult(
                        new Value("#NA!"),
                        new PositionedAt(1, 1),
                        List.of(),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:02+01:00[Europe/Paris]")))
                )
        ));
        INTERMEDIATE_RESULTS_BY_FORMULA.put(new Formula("IF(\"#REF!\",true,false)"), List.of(
                new IntermediateResult(
                        new Value("#REF!"),
                        new PositionedAt(4, 4),
                        List.of(
                                new Input(new InputName("comparisonValue"), new Value("#REF!"), new PositionedAt(1, 1))
                        ),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")))
                ),
                new IntermediateResult(
                        new Value("#REF!"),
                        new PositionedAt(1, 1),
                        List.of(),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:02+01:00[Europe/Paris]")))
                )
        ));
        INTERMEDIATE_RESULTS_BY_FORMULA.put(new Formula("IF(\"#NUM!\",true,false)"), List.of(
                new IntermediateResult(
                        new Value("#NUM!"),
                        new PositionedAt(4, 4),
                        List.of(
                                new Input(new InputName("comparisonValue"), new Value("#NUM!"), new PositionedAt(1, 1))
                        ),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")))
                ),
                new IntermediateResult(
                        new Value("#NUM!"),
                        new PositionedAt(1, 1),
                        List.of(),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:02+01:00[Europe/Paris]")))
                )
        ));
        INTERMEDIATE_RESULTS_BY_FORMULA.put(new Formula("IF(\"#DIV/0!\",true,false)"), List.of(
                new IntermediateResult(
                        new Value("#DIV/0!"),
                        new PositionedAt(4, 4),
                        List.of(
                                new Input(new InputName("comparisonValue"), new Value("#DIV/0!"), new PositionedAt(1, 1))
                        ),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")))
                ),
                new IntermediateResult(
                        new Value("#DIV/0!"),
                        new PositionedAt(1, 1),
                        List.of(),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:02+01:00[Europe/Paris]")))
                )
        ));
        INTERMEDIATE_RESULTS_BY_FORMULA.put(new Formula("IF(\"true\",true,false)"), List.of(
                new IntermediateResult(
                        new Value("true"),
                        new PositionedAt(4, 4),
                        List.of(
                                new Input(new InputName("comparisonValue"), new Value("true"), new PositionedAt(1, 1))
                        ),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:05+01:00[Europe/Paris]")))
                ),
                new IntermediateResult(
                        new Value("true"),
                        new PositionedAt(1, 1),
                        List.of(),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:02+01:00[Europe/Paris]")))
                ),
                new IntermediateResult(
                        new Value("true"),
                        new PositionedAt(2, 2),
                        List.of(),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:04+01:00[Europe/Paris]")))
                )
        ));
        INTERMEDIATE_RESULTS_BY_FORMULA.put(new Formula("IF(\"false\",true,false)"), List.of(
                new IntermediateResult(
                        new Value("false"),
                        new PositionedAt(4, 4),
                        List.of(
                                new Input(new InputName("comparisonValue"), new Value("false"), new PositionedAt(1, 1))
                        ),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:05+01:00[Europe/Paris]")))
                ),
                new IntermediateResult(
                        new Value("false"),
                        new PositionedAt(1, 1),
                        List.of(),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:02+01:00[Europe/Paris]")))
                ),
                new IntermediateResult(
                        new Value("false"),
                        new PositionedAt(3, 3),
                        List.of(),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:04+01:00[Europe/Paris]")))
                )
        ));
        INTERMEDIATE_RESULTS_BY_FORMULA.put(new Formula("IF(\"0\",true,false)"), List.of(
                new IntermediateResult(
                        new Value("false"),
                        new PositionedAt(4, 4),
                        List.of(
                                new Input(new InputName("comparisonValue"), new Value("0"), new PositionedAt(1, 1))
                        ),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:05+01:00[Europe/Paris]")))
                ),
                new IntermediateResult(
                        new Value("0"),
                        new PositionedAt(1, 1),
                        List.of(),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:02+01:00[Europe/Paris]")))
                ),
                new IntermediateResult(
                        new Value("false"),
                        new PositionedAt(3, 3),
                        List.of(),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:04+01:00[Europe/Paris]")))
                )
        ));
        INTERMEDIATE_RESULTS_BY_FORMULA.put(new Formula("IF(\"1\",true,false)"), List.of(
                new IntermediateResult(
                        new Value("true"),
                        new PositionedAt(4, 4),
                        List.of(
                                new Input(new InputName("comparisonValue"), new Value("1"), new PositionedAt(1, 1))
                        ),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:05+01:00[Europe/Paris]")))
                ),
                new IntermediateResult(
                        new Value("1"),
                        new PositionedAt(1, 1),
                        List.of(),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:02+01:00[Europe/Paris]")))
                ),
                new IntermediateResult(
                        new Value("true"),
                        new PositionedAt(2, 2),
                        List.of(),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:04+01:00[Europe/Paris]")))
                )
        ));


        INTERMEDIATE_RESULTS_BY_FORMULA.put(new Formula("IFERROR(\"#NA!\",true,false)"), List.of(
                new IntermediateResult(
                        new Value("true"),
                        new PositionedAt(4, 4),
                        List.of(
                                new Input(new InputName("comparisonValue"), new Value("#NA!"), new PositionedAt(1, 1))
                        ),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:05+01:00[Europe/Paris]")))
                ),
                new IntermediateResult(
                        new Value("#NA!"),
                        new PositionedAt(1, 1),
                        List.of(),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:02+01:00[Europe/Paris]")))
                ),
                new IntermediateResult(
                        new Value("true"),
                        new PositionedAt(2, 2),
                        List.of(),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:04+01:00[Europe/Paris]")))
                )
        ));
        INTERMEDIATE_RESULTS_BY_FORMULA.put(new Formula("IFERROR(\"#REF!\",true,false)"), List.of(
                new IntermediateResult(
                        new Value("true"),
                        new PositionedAt(4, 4),
                        List.of(
                                new Input(new InputName("comparisonValue"), new Value("#REF!"), new PositionedAt(1, 1))
                        ),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:05+01:00[Europe/Paris]")))
                ),
                new IntermediateResult(
                        new Value("#REF!"),
                        new PositionedAt(1, 1),
                        List.of(),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:02+01:00[Europe/Paris]")))
                ),
                new IntermediateResult(
                        new Value("true"),
                        new PositionedAt(2, 2),
                        List.of(),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:04+01:00[Europe/Paris]")))
                )
        ));
        INTERMEDIATE_RESULTS_BY_FORMULA.put(new Formula("IFERROR(\"#NUM!\",true,false)"), List.of(
                new IntermediateResult(
                        new Value("true"),
                        new PositionedAt(4, 4),
                        List.of(
                                new Input(new InputName("comparisonValue"), new Value("#NUM!"), new PositionedAt(1, 1))
                        ),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:05+01:00[Europe/Paris]")))
                ),
                new IntermediateResult(
                        new Value("#NUM!"),
                        new PositionedAt(1, 1),
                        List.of(),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:02+01:00[Europe/Paris]")))
                ),
                new IntermediateResult(
                        new Value("true"),
                        new PositionedAt(2, 2),
                        List.of(),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:04+01:00[Europe/Paris]")))
                )
        ));
        INTERMEDIATE_RESULTS_BY_FORMULA.put(new Formula("IFERROR(\"#DIV/0!\",true,false)"), List.of(
                new IntermediateResult(
                        new Value("true"),
                        new PositionedAt(4, 4),
                        List.of(
                                new Input(new InputName("comparisonValue"), new Value("#DIV/0!"), new PositionedAt(1, 1))
                        ),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:05+01:00[Europe/Paris]")))
                ),
                new IntermediateResult(
                        new Value("#DIV/0!"),
                        new PositionedAt(1, 1),
                        List.of(),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:02+01:00[Europe/Paris]")))
                ),
                new IntermediateResult(
                        new Value("true"),
                        new PositionedAt(2, 2),
                        List.of(),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:04+01:00[Europe/Paris]")))
                )
        ));
        INTERMEDIATE_RESULTS_BY_FORMULA.put(new Formula("IFERROR(\"true\",true,false)"), List.of(
                new IntermediateResult(
                        new Value("false"),
                        new PositionedAt(4, 4),
                        List.of(
                                new Input(new InputName("comparisonValue"), new Value("true"), new PositionedAt(1, 1))
                        ),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:05+01:00[Europe/Paris]")))
                ),
                new IntermediateResult(
                        new Value("true"),
                        new PositionedAt(1, 1),
                        List.of(),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:02+01:00[Europe/Paris]")))
                ),
                new IntermediateResult(
                        new Value("false"),
                        new PositionedAt(3, 3),
                        List.of(),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:04+01:00[Europe/Paris]")))
                )
        ));
        INTERMEDIATE_RESULTS_BY_FORMULA.put(new Formula("IFERROR(\"false\",true,false)"), List.of(
                new IntermediateResult(
                        new Value("false"),
                        new PositionedAt(4, 4),
                        List.of(
                                new Input(new InputName("comparisonValue"), new Value("false"), new PositionedAt(1, 1))
                        ),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:05+01:00[Europe/Paris]")))
                ),
                new IntermediateResult(
                        new Value("false"),
                        new PositionedAt(1, 1),
                        List.of(),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:02+01:00[Europe/Paris]")))
                ),
                new IntermediateResult(
                        new Value("false"),
                        new PositionedAt(3, 3),
                        List.of(),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:04+01:00[Europe/Paris]")))
                )
        ));
        INTERMEDIATE_RESULTS_BY_FORMULA.put(new Formula("IFERROR(\"0\",true,false)"), List.of(
                new IntermediateResult(
                        new Value("false"),
                        new PositionedAt(4, 4),
                        List.of(
                                new Input(new InputName("comparisonValue"), new Value("0"), new PositionedAt(1, 1))
                        ),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:05+01:00[Europe/Paris]")))
                ),
                new IntermediateResult(
                        new Value("0"),
                        new PositionedAt(1, 1),
                        List.of(),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:02+01:00[Europe/Paris]")))
                ),
                new IntermediateResult(
                        new Value("false"),
                        new PositionedAt(3, 3),
                        List.of(),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:04+01:00[Europe/Paris]")))
                )
        ));
        INTERMEDIATE_RESULTS_BY_FORMULA.put(new Formula("IFERROR(\"1\",true,false)"), List.of(
                new IntermediateResult(
                        new Value("false"),
                        new PositionedAt(4, 4),
                        List.of(
                                new Input(new InputName("comparisonValue"), new Value("1"), new PositionedAt(1, 1))
                        ),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:05+01:00[Europe/Paris]")))
                ),
                new IntermediateResult(
                        new Value("1"),
                        new PositionedAt(1, 1),
                        List.of(),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:02+01:00[Europe/Paris]")))
                ),
                new IntermediateResult(
                        new Value("false"),
                        new PositionedAt(3, 3),
                        List.of(),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:04+01:00[Europe/Paris]")))
                )
        ));
        INTERMEDIATE_RESULTS_BY_FORMULA.put(new Formula("IFERROR(\"660\",true,false)"), List.of(
                new IntermediateResult(
                        new Value("false"),
                        new PositionedAt(4, 4),
                        List.of(
                                new Input(new InputName("comparisonValue"), new Value("660"), new PositionedAt(1, 1))
                        ),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:05+01:00[Europe/Paris]")))
                ),
                new IntermediateResult(
                        new Value("660"),
                        new PositionedAt(1, 1),
                        List.of(),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:02+01:00[Europe/Paris]")))
                ),
                new IntermediateResult(
                        new Value("false"),
                        new PositionedAt(3, 3),
                        List.of(),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:04+01:00[Europe/Paris]")))
                )
        ));
        INTERMEDIATE_RESULTS_BY_FORMULA.put(new Formula("IFERROR(\"\",true,false)"), List.of(
                new IntermediateResult(
                        new Value("false"),
                        new PositionedAt(4, 4),
                        List.of(
                                new Input(new InputName("comparisonValue"), new Value(""), new PositionedAt(1, 1))
                        ),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:05+01:00[Europe/Paris]")))
                ),
                new IntermediateResult(
                        new Value(""),
                        new PositionedAt(1, 1),
                        List.of(),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:02+01:00[Europe/Paris]")))
                ),
                new IntermediateResult(
                        new Value("false"),
                        new PositionedAt(3, 3),
                        List.of(),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:04+01:00[Europe/Paris]")))
                )
        ));


        INTERMEDIATE_RESULTS_BY_FORMULA.put(new Formula("IFNA(\"#NA!\",true,false)"), List.of(
                new IntermediateResult(
                        new Value("true"),
                        new PositionedAt(4, 4),
                        List.of(
                                new Input(new InputName("comparisonValue"), new Value("#NA!"), new PositionedAt(1, 1))
                        ),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:05+01:00[Europe/Paris]")))
                ),
                new IntermediateResult(
                        new Value("#NA!"),
                        new PositionedAt(1, 1),
                        List.of(),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:02+01:00[Europe/Paris]")))
                ),
                new IntermediateResult(
                        new Value("true"),
                        new PositionedAt(2, 2),
                        List.of(),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:04+01:00[Europe/Paris]")))
                )
        ));
        INTERMEDIATE_RESULTS_BY_FORMULA.put(new Formula("IFNA(\"#REF!\",true,false)"), List.of(
                new IntermediateResult(
                        new Value("false"),
                        new PositionedAt(4, 4),
                        List.of(
                                new Input(new InputName("comparisonValue"), new Value("#REF!"), new PositionedAt(1, 1))
                        ),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:05+01:00[Europe/Paris]")))
                ),
                new IntermediateResult(
                        new Value("#REF!"),
                        new PositionedAt(1, 1),
                        List.of(),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:02+01:00[Europe/Paris]")))
                ),
                new IntermediateResult(
                        new Value("false"),
                        new PositionedAt(3, 3),
                        List.of(),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:04+01:00[Europe/Paris]")))
                )
        ));
        INTERMEDIATE_RESULTS_BY_FORMULA.put(new Formula("IFNA(\"#NUM!\",true,false)"), List.of(
                new IntermediateResult(
                        new Value("false"),
                        new PositionedAt(4, 4),
                        List.of(
                                new Input(new InputName("comparisonValue"), new Value("#NUM!"), new PositionedAt(1, 1))
                        ),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:05+01:00[Europe/Paris]")))
                ),
                new IntermediateResult(
                        new Value("#NUM!"),
                        new PositionedAt(1, 1),
                        List.of(),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:02+01:00[Europe/Paris]")))
                ),
                new IntermediateResult(
                        new Value("false"),
                        new PositionedAt(3, 3),
                        List.of(),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:04+01:00[Europe/Paris]")))
                )
        ));
        INTERMEDIATE_RESULTS_BY_FORMULA.put(new Formula("IFNA(\"#DIV/0!\",true,false)"), List.of(
                new IntermediateResult(
                        new Value("false"),
                        new PositionedAt(4, 4),
                        List.of(
                                new Input(new InputName("comparisonValue"), new Value("#DIV/0!"), new PositionedAt(1, 1))
                        ),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:05+01:00[Europe/Paris]")))
                ),
                new IntermediateResult(
                        new Value("#DIV/0!"),
                        new PositionedAt(1, 1),
                        List.of(),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:02+01:00[Europe/Paris]")))
                ),
                new IntermediateResult(
                        new Value("false"),
                        new PositionedAt(3, 3),
                        List.of(),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:04+01:00[Europe/Paris]")))
                )
        ));
        INTERMEDIATE_RESULTS_BY_FORMULA.put(new Formula("IFNA(\"true\",true,false)"), List.of(
                new IntermediateResult(
                        new Value("false"),
                        new PositionedAt(4, 4),
                        List.of(
                                new Input(new InputName("comparisonValue"), new Value("true"), new PositionedAt(1, 1))
                        ),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:05+01:00[Europe/Paris]")))
                ),
                new IntermediateResult(
                        new Value("true"),
                        new PositionedAt(1, 1),
                        List.of(),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:02+01:00[Europe/Paris]")))
                ),
                new IntermediateResult(
                        new Value("false"),
                        new PositionedAt(3, 3),
                        List.of(),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:04+01:00[Europe/Paris]")))
                )
        ));
        INTERMEDIATE_RESULTS_BY_FORMULA.put(new Formula("IFNA(\"false\",true,false)"), List.of(
                new IntermediateResult(
                        new Value("false"),
                        new PositionedAt(4, 4),
                        List.of(
                                new Input(new InputName("comparisonValue"), new Value("false"), new PositionedAt(1, 1))
                        ),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:05+01:00[Europe/Paris]")))
                ),
                new IntermediateResult(
                        new Value("false"),
                        new PositionedAt(1, 1),
                        List.of(),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:02+01:00[Europe/Paris]")))
                ),
                new IntermediateResult(
                        new Value("false"),
                        new PositionedAt(3, 3),
                        List.of(),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:04+01:00[Europe/Paris]")))
                )
        ));
        INTERMEDIATE_RESULTS_BY_FORMULA.put(new Formula("IFNA(\"0\",true,false)"), List.of(
                new IntermediateResult(
                        new Value("false"),
                        new PositionedAt(4, 4),
                        List.of(
                                new Input(new InputName("comparisonValue"), new Value("0"), new PositionedAt(1, 1))
                        ),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:05+01:00[Europe/Paris]")))
                ),
                new IntermediateResult(
                        new Value("0"),
                        new PositionedAt(1, 1),
                        List.of(),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:02+01:00[Europe/Paris]")))
                ),
                new IntermediateResult(
                        new Value("false"),
                        new PositionedAt(3, 3),
                        List.of(),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:04+01:00[Europe/Paris]")))
                )
        ));
        INTERMEDIATE_RESULTS_BY_FORMULA.put(new Formula("IFNA(\"1\",true,false)"), List.of(
                new IntermediateResult(
                        new Value("false"),
                        new PositionedAt(4, 4),
                        List.of(
                                new Input(new InputName("comparisonValue"), new Value("1"), new PositionedAt(1, 1))
                        ),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:05+01:00[Europe/Paris]")))
                ),
                new IntermediateResult(
                        new Value("1"),
                        new PositionedAt(1, 1),
                        List.of(),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:02+01:00[Europe/Paris]")))
                ),
                new IntermediateResult(
                        new Value("false"),
                        new PositionedAt(3, 3),
                        List.of(),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:04+01:00[Europe/Paris]")))
                )
        ));
        INTERMEDIATE_RESULTS_BY_FORMULA.put(new Formula("IFNA(\"660\",true,false)"), List.of(
                new IntermediateResult(
                        new Value("false"),
                        new PositionedAt(4, 4),
                        List.of(
                                new Input(new InputName("comparisonValue"), new Value("660"), new PositionedAt(1, 1))
                        ),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:05+01:00[Europe/Paris]")))
                ),
                new IntermediateResult(
                        new Value("660"),
                        new PositionedAt(1, 1),
                        List.of(),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:02+01:00[Europe/Paris]")))
                ),
                new IntermediateResult(
                        new Value("false"),
                        new PositionedAt(3, 3),
                        List.of(),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:04+01:00[Europe/Paris]")))
                )
        ));
        INTERMEDIATE_RESULTS_BY_FORMULA.put(new Formula("IFNA(\"\",true,false)"), List.of(
                new IntermediateResult(
                        new Value("false"),
                        new PositionedAt(4, 4),
                        List.of(
                                new Input(new InputName("comparisonValue"), new Value(""), new PositionedAt(1, 1))
                        ),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:05+01:00[Europe/Paris]")))
                ),
                new IntermediateResult(
                        new Value(""),
                        new PositionedAt(1, 1),
                        List.of(),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:02+01:00[Europe/Paris]")))
                ),
                new IntermediateResult(
                        new Value("false"),
                        new PositionedAt(3, 3),
                        List.of(),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:04+01:00[Europe/Paris]")))
                )
        ));
    }

    public static Stream<Arguments> provideLogicalComparisonFunctions() {
        return ARGUMENTS_BY_LOGICAL_COMPARISON_FUNC.entrySet()
                .stream()
                .flatMap(entry -> {
                    final LogicalComparisonFunction.Function function = entry.getKey();
                    return entry.getValue().get().map(arguments -> {
                        final Value value = (Value) arguments.get()[0];
                        final Value expected = (Value) arguments.get()[1];
                        return switch (function) {
                            case IF -> {
                                final Formula givenFormula = new Formula(String.format("IF(\"%s\",true,false)", value.value()));
                                final List<IntermediateResult> expectedIntermediateResults = INTERMEDIATE_RESULTS_BY_FORMULA.get(givenFormula);
                                Validate.notNull(expectedIntermediateResults, "null expectedIntermediateResults for given formula %s", givenFormula.formula());
                                yield Arguments.of(
                                        new Given(
                                                givenFormula,
                                                new LogicalComparisonExpression(
                                                        function, new ArgumentExpression(value, new PositionedAt(1, 1)),
                                                        new ArgumentExpression(Value.ofTrue(), new PositionedAt(2, 2)),
                                                        new ArgumentExpression(Value.ofFalse(), new PositionedAt(3, 3)),
                                                        new PositionedAt(4, 4)),
                                                List.of()
                                        ),
                                        new ExpectedEvaluation(
                                                new Evaluated(expected,
                                                        new PositionedAt(4, 4),
                                                        List.of(
                                                                new Input(new InputName("comparisonValue"), value, new PositionedAt(1, 1))
                                                        )),
                                                expectedIntermediateResults
                                        )
                                );
                            }
                            case IF_ERROR -> {
                                final Formula givenFormula = new Formula(String.format("IFERROR(\"%s\",true,false)", value.value()));
                                final List<IntermediateResult> expectedIntermediateResults = INTERMEDIATE_RESULTS_BY_FORMULA.get(givenFormula);
                                Validate.notNull(expectedIntermediateResults, "null expectedIntermediateResults for given formula %s", givenFormula.formula());
                                yield Arguments.of(
                                        new Given(
                                                givenFormula,
                                                new LogicalComparisonExpression(
                                                        function, new ArgumentExpression(value, new PositionedAt(1, 1)),
                                                        new ArgumentExpression(Value.ofTrue(), new PositionedAt(2, 2)),
                                                        new ArgumentExpression(Value.ofFalse(), new PositionedAt(3, 3)),
                                                        new PositionedAt(4, 4)),
                                                List.of()
                                        ),
                                        new ExpectedEvaluation(
                                                new Evaluated(expected,
                                                        new PositionedAt(4, 4),
                                                        List.of(
                                                                new Input(new InputName("comparisonValue"), value, new PositionedAt(1, 1))
                                                        )),
                                                expectedIntermediateResults
                                        )
                                );
                            }
                            case IF_NOT_AVAILABLE -> {
                                final Formula givenFormula = new Formula(String.format("IFNA(\"%s\",true,false)", value.value()));
                                final List<IntermediateResult> expectedIntermediateResults = INTERMEDIATE_RESULTS_BY_FORMULA.get(givenFormula);
                                Validate.notNull(expectedIntermediateResults, "null expectedIntermediateResults for given formula %s", givenFormula.formula());
                                yield Arguments.of(
                                        new Given(
                                                givenFormula,
                                                new LogicalComparisonExpression(
                                                        function, new ArgumentExpression(value, new PositionedAt(1, 1)),
                                                        new ArgumentExpression(Value.ofTrue(), new PositionedAt(2, 2)),
                                                        new ArgumentExpression(Value.ofFalse(), new PositionedAt(3, 3)),
                                                        new PositionedAt(4, 4)),
                                                List.of()
                                        ),
                                        new ExpectedEvaluation(
                                                new Evaluated(expected,
                                                        new PositionedAt(4, 4),
                                                        List.of(
                                                                new Input(new InputName("comparisonValue"), value, new PositionedAt(1, 1))
                                                        )),
                                                expectedIntermediateResults
                                        )
                                );
                            }
                        };
                    });
                });
//        return Stream.of(
//                        LogicalComparisonFunctionTestProvider.provideIf()
//                                .map(ifF -> Arguments.of(String.format("""
//                                        IF("%s",true,false)""", ((Value) ifF.get()[0]).value()), ifF.get()[1])),
//                        LogicalComparisonFunctionTestProvider.provideIfError()
//                                .map(ifError -> Arguments.of(String.format("""
//                                        IFERROR("%s",true,false)""", ((Value) ifError.get()[0]).value()), ifError.get()[1])),
//                        LogicalComparisonFunctionTestProvider.provideIfNotAvailable()
//                                .map(ifNotAvailable -> Arguments.of(String.format("""
//                                        IFNA("%s",true,false)""", ((Value) ifNotAvailable.get()[0]).value()), ifNotAvailable.get()[1])),
//                        // TODO
//                        Stream.of(Arguments.of("""
//                                        IF("false",ADD(1,1),ADD(2,2))""", "4"),
//                                Arguments.of("""
//                                        IFERROR("false",ADD(1,1),ADD(2,2))""", "4"),
//                                Arguments.of("""
//                                        IFNA("false",ADD(1,1),ADD(2,2))""", "4"))
//                )
//                .flatMap(Function.identity());
    }

}
