package com.damdamdeo.formula.infrastructure.web.api;

import com.damdamdeo.formula.domain.usecase.EvaluateUseCase;
import com.damdamdeo.formula.infrastructure.evaluation.antlr.ParseTreeAntlrLoaded;
import com.damdamdeo.formula.infrastructure.evaluation.expression.DefaultAntlrMappingExpressionLoaded;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Nested;

//@ExtendWith(EvaluateEndpointTest.InfraEvaluateUseCaseTestResolver.class)
@QuarkusTest
public class EvaluateEndpointTest {
// ici je peux throw des exceptions directement parce que le test est deja fait en amont !!!
    @Inject
    EvaluateUseCase<ParseTreeAntlrLoaded, DefaultAntlrMappingExpressionLoaded> evaluateUseCase;

    @Nested
    class EvaluateUsingAntlr {

//        @EvaluateUseCaseTestResolver.DebugFeatureActiveTest
//        void shouldEvaluateWithDebugFeatureActive(final Formula givenFormula, final Evaluated givenEvaluated,
//                                                  final DebugFeature givenDebugFeature,
//                                                  final EvaluationResult expectedEvaluationResult) {
//
//        }
//
//        @EvaluateUseCaseTestResolver.DebugFeatureInactiveTest
//        void shouldEvaluateWithDebugFeatureInactive(final Formula givenFormula, final Evaluated givenEvaluated,
//                                                    final DebugFeature givenDebugFeature,
//                                                    final EvaluationResult expectedEvaluationResult) {
//        }
    }

    @Nested
    class EvaluateUsingAntlrMappingDomainEval {

    }

    //    @Test
//    public void shouldProcess() throws JSONException {
//        // Given
//        doReturn(
//                Uni.createFrom().item(new EvaluationResult(
//                        new Value("true"),
//                        new EvaluationLoadingProcessedIn(
//                                new ProcessedAt(ZonedDateTime.parse("2023-12-25T10:14:58+01:00[Europe/Paris]")),
//                                new ProcessedAt(ZonedDateTime.parse("2023-12-25T10:14:59+01:00[Europe/Paris]"))
//                        ),
//                        List.of(
//                                new IntermediateResult(
//                                        Value.of("10"),
//                                        new PositionedAt(4, 5),
//                                        List.of(
//                                                new Input(new InputName("reference"), new Value("ref"), new PositionedAt(2, 3))),
//                                        new EvaluationProcessedIn(
//                                                new ProcessedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")),
//                                                new ProcessedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]"))))
//                        ),
//                        new EvaluationProcessedIn(
//                                new ProcessedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")),
//                                new ProcessedAt(ZonedDateTime.parse("2023-12-25T10:15:02+01:00[Europe/Paris]")))))
//        )
//                .when(evaluateUseCase)
//                .execute(new EvaluateCommand(new Formula("true"),
//                        List.of(
//                                new StructuredReference(new Reference("ref"), new Value("val"))),
//                        DebugFeature.ACTIVE,
//                        EvaluateOn.ANTLR));
//        //language=JSON
//        final String request = """
//                {
//                    "formula":"true",
//                    "structuredReferences": {
//                        "ref": "val"
//                    },
//                    "evaluateOn": "ANTLR",
//                    "debugFeature": "ACTIVE"
//                }
//                """;
//
//        // When && Then
//        //language=JSON
//        final String expectedBody = """
//                {
//                     "result": "true",
//                     "exactProcessedInNanos": 3000000000,
//                     "evaluationLoadingProcessedIn": {
//                         "processedAtStart": "2023-12-25T10:14:58+01:00",
//                         "processedAtEnd": "2023-12-25T10:14:59+01:00",
//                         "processedInNanos": 1000000000
//                     },
//                     "evaluationProcessedIn": {
//                         "processedAtStart": "2023-12-25T10:15:00+01:00",
//                         "processedAtEnd": "2023-12-25T10:15:02+01:00",
//                         "processedInNanos": 2000000000
//                     },
//                     "intermediateResults": [
//                         {
//                             "processedAtStart": "2023-12-25T10:15:00+01:00",
//                             "processedAtEnd": "2023-12-25T10:15:01+01:00",
//                             "processedInNanos": 1000000000,
//                             "positionedAt": {
//                                 "start": 4,
//                                 "end": 5
//                             },
//                             "inputs": [
//                                 {
//                                     "name": "reference",
//                                     "value": "ref",
//                                     "positionedAt": {
//                                         "start": 2,
//                                         "end": 3
//                                     }
//                                 }
//                             ],
//                             "result": "10"
//                         }
//                     ]
//                }
//                """;
//        final String actualBody = given()
//                .contentType("application/vnd.formula-evaluate-v1+json")
//                .accept("application/vnd.formula-evaluated-v1+json")
//                .body(request)
//                .when()
//                .post("/evaluate")
//                .then()
//                .log().all()
//                .statusCode(HttpStatus.SC_OK)
//                .contentType("application/vnd.formula-evaluated-v1+json")
//                .extract().response().body().asString();
//        JSONAssert.assertEquals(expectedBody, actualBody, JSONCompareMode.STRICT);
//    }
//
//    @Test
//    public void shouldHandleSyntaxErrorException() {
//        // Given
//        doReturn(
//                Uni.createFrom().failure(
//                        new EvaluationException(new AntlrSyntaxErrorException(new Formula("true"), new AntlrSyntaxError(0, 1, "custom \"msg\"")))
//                )
//        )
//                .when(evaluateUseCase).execute(
//                        new EvaluateCommand(
//                                new Formula("true"),
//                                List.of(
//                                        new StructuredReference(new Reference("ref"), new Value("val"))),
//                                DebugFeature.ACTIVE,
//                                EvaluateOn.ANTLR));
//        //language=JSON
//        final String request = """
//                {
//                    "formula":"true",
//                    "structuredReferences": {
//                        "ref":"val"
//                    },
//                    "evaluateOn": "ANTLR",
//                    "debugFeature": "ACTIVE"
//                }
//                """;
//
//        // When && Then
//        given()
//                .contentType("application/vnd.formula-evaluate-v1+json")
//                .accept("application/vnd.formula-evaluated-v1+json")
//                .body(request)
//                .when()
//                .post("/evaluate")
//                .then()
//                .log().all()
//                .statusCode(HttpStatus.SC_BAD_REQUEST)
//                .contentType("application/vnd.evaluation-syntax-error-v1+json")
//                .body("message", is("Syntax error at line '0' at positionedAt '1' with message 'custom \"msg\"'"));
//    }
//
//    @Test
//    public void shouldHandleException() {
//        // Given
//        doReturn(
//                Uni.createFrom().failure(new EvaluationException(new Exception("unexpected \"cause\"")))
//        )
//                .when(evaluateUseCase).execute(new EvaluateCommand(
//                        new Formula("true"),
//                        List.of(
//                                new StructuredReference(new Reference("ref"), new Value("val"))),
//                        DebugFeature.ACTIVE,
//                        EvaluateOn.ANTLR));
//        //language=JSON
//        final String request = """
//                {
//                    "formula":"true",
//                    "structuredReferences": {
//                        "ref":"val"
//                    },
//                    "evaluateOn": "ANTLR",
//                    "debugFeature": "ACTIVE"
//                }
//                """;
//
//        // When && Then
//        given()
//                .contentType("application/vnd.formula-evaluate-v1+json")
//                .accept("application/vnd.formula-evaluated-v1+json")
//                .body(request)
//                .when()
//                .post("/evaluate")
//                .then()
//                .log().all()
//                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
//                .contentType("application/vnd.evaluation-unexpected-cause-v1+json")
//                .body("message", is("unexpected \"cause\""));
//    }
//    static class InfraEvaluateUseCaseTestResolver extends EvaluateUseCaseTestResolver {
//
//    }
}
