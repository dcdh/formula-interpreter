package com.damdamdeo.formula.infrastructure.api;

import com.damdamdeo.formula.domain.*;
import com.damdamdeo.formula.domain.usecase.ExecuteCommand;
import com.damdamdeo.formula.domain.usecase.ExecuteUseCase;
import com.damdamdeo.formula.infrastructure.antlr.AntlrSyntaxError;
import com.damdamdeo.formula.infrastructure.antlr.AntlrSyntaxErrorException;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Uni;
import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.time.ZonedDateTime;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doReturn;

@QuarkusTest
public class ExecutorEndpointTest {

    @InjectMock
    private ExecuteUseCase executeUseCase;
    @Test
    public void shouldExecute() throws JSONException {
        // Given
        doReturn(
                Uni.createFrom().item(new ExecutionResult(
                        new Result(new Value("true"), new Range(0, 10)),
                        new ParserExecutionProcessedIn(
                                new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:14:58+01:00[Europe/Paris]")),
                                new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:14:59+01:00[Europe/Paris]"))
                        ),
                        List.of(
                                new ElementExecution(
                                        Value.of("10"),
                                        new Range(4, 5),
                                        List.of(
                                                new Input(new InputName("reference"), new Value("ref"), new Range(2, 3))),
                                        new ExecutionProcessedIn(
                                                new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")),
                                                new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]"))))
                        ),
                        new ExecutionProcessedIn(
                                new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")),
                                new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:02+01:00[Europe/Paris]")))))
        )
                .when(executeUseCase)
                .execute(new ExecuteCommand(new Formula("true"),
                        new StructuredReferences(
                                List.of(
                                        new StructuredReference(new Reference("ref"), new Value("val")))
                        ),
                        DebugFeature.ACTIVE));
        //language=JSON
        final String request = """
                {
                    "formula":"true",
                    "structuredReferences": {
                        "ref": "val"
                    },
                    "debugFeature": "ACTIVE"
                }
                """;

        // When && Then
        //language=JSON
        final String expectedBody = """
                {
                     "result": "true",
                     "exactProcessedInNanos": 3000000000,
                     "parserExecutionProcessedIn": {
                         "executedAtStart": "2023-12-25T10:14:58+01:00",
                         "executedAtEnd": "2023-12-25T10:14:59+01:00",
                         "processedInNanos": 1000000000
                     },
                     "executionProcessedIn": {
                         "executedAtStart": "2023-12-25T10:15:00+01:00",
                         "executedAtEnd": "2023-12-25T10:15:02+01:00",
                         "processedInNanos": 2000000000
                     },
                     "elementExecutions": [
                         {
                             "executedAtStart": "2023-12-25T10:15:00+01:00",
                             "executedAtEnd": "2023-12-25T10:15:01+01:00",
                             "processedInNanos": 1000000000,
                             "range": {
                                 "start": 4,
                                 "end": 5
                             },
                             "inputs": [
                                 {
                                     "name": "reference",
                                     "value": "ref",
                                     "range": {
                                         "start": 2,
                                         "end": 3
                                     }
                                 }
                             ],
                             "result": "10"
                         }
                     ]
                }
                """;
        final String actualBody = given()
                .contentType("application/vnd.formula-execute-v1+json")
                .accept("application/vnd.formula-execution-v1+json")
                .body(request)
                .when()
                .post("/execute")
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_OK)
                .contentType("application/vnd.formula-execution-v1+json")
                .extract().response().body().asString();
        JSONAssert.assertEquals(expectedBody, actualBody, JSONCompareMode.STRICT);
    }

    @Test
    public void shouldHandleSyntaxErrorException() {
        // Given
        doReturn(
                Uni.createFrom().failure(
                        new ExecutionException(new AntlrSyntaxErrorException(new Formula("true"), new AntlrSyntaxError(0, 1, "custom \"msg\"")))
                )
        )
                .when(executeUseCase).execute(
                        new ExecuteCommand(
                                new Formula("true"),
                                new StructuredReferences(
                                        List.of(
                                                new StructuredReference(new Reference("ref"), new Value("val")))
                                ),
                                DebugFeature.ACTIVE));
        //language=JSON
        final String request = """
                {
                    "formula":"true",
                    "structuredReferences": {
                        "ref":"val"
                    },
                    "debugFeature": "ACTIVE"
                }
                """;

        // When && Then
        given()
                .contentType("application/vnd.formula-execute-v1+json")
                .accept("application/vnd.formula-execution-v1+json")
                .body(request)
                .when()
                .post("/execute")
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .contentType("application/vnd.execution-syntax-error-v1+json")
                .body("message", is("Syntax error at line '0' at range '1' with message 'custom \"msg\"'"));
    }

    @Test
    public void shouldHandleException() {
        // Given
        doReturn(
                Uni.createFrom().failure(new ExecutionException(new Exception("unexpected \"exception\"")))
        )
                .when(executeUseCase).execute(new ExecuteCommand(
                        new Formula("true"),
                        new StructuredReferences(
                                List.of(
                                        new StructuredReference(new Reference("ref"), new Value("val")))
                        ),
                        DebugFeature.ACTIVE));
        //language=JSON
        final String request = """
                {
                    "formula":"true",
                    "structuredReferences": {
                        "ref":"val"
                    },
                    "debugFeature": "ACTIVE"
                }
                """;

        // When && Then
        given()
                .contentType("application/vnd.formula-execute-v1+json")
                .accept("application/vnd.formula-execution-v1+json")
                .body(request)
                .when()
                .post("/execute")
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                .contentType("application/vnd.execution-unexpected-exception-v1+json")
                .body("message", is("unexpected \"exception\""));
    }
}
