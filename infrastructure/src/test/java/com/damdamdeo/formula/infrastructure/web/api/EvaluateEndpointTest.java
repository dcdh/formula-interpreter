package com.damdamdeo.formula.infrastructure.web.api;

import com.damdamdeo.formula.domain.*;
import com.damdamdeo.formula.domain.usecase.EvaluateCommand;
import com.damdamdeo.formula.domain.usecase.EvaluateUseCase;
import com.damdamdeo.formula.infrastructure.parser.antlr.AntlrSyntaxError;
import com.damdamdeo.formula.infrastructure.parser.antlr.AntlrSyntaxErrorException;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Uni;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doReturn;

@QuarkusTest
public class EvaluateEndpointTest {

    @InjectMock
    EvaluateUseCase evaluateUseCase;

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
//                Uni.createFrom().failure(new EvaluationException(new Exception("unexpected \"exception\"")))
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
//                .contentType("application/vnd.evaluation-unexpected-exception-v1+json")
//                .body("message", is("unexpected \"exception\""));
//    }
}