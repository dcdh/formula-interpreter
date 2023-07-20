package com.damdamdeo.formula.infrastructure.interfaces;

import com.damdamdeo.formula.domain.*;
import com.damdamdeo.formula.domain.usecase.ExecuteCommand;
import com.damdamdeo.formula.domain.usecase.ExecuteUseCase;
import com.damdamdeo.formula.infrastructure.antlr.AntlrExecution;
import com.damdamdeo.formula.infrastructure.antlr.AntlrSyntaxError;
import com.damdamdeo.formula.infrastructure.antlr.AntlrSyntaxErrorException;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@QuarkusTest
public class ExecutorEndpointTest {

    @InjectMock
    private ExecuteUseCase executeUseCase;

    @Test
    public void shouldExecute() throws JSONException {
        // Given
        doReturn(new ExecutionResult(new Value("true"), List.of(
                new AntlrExecution(new ExecutionId(new UUID(0, 0)), new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:30+01:00[Europe/Paris]")), 4, 5, Map.of(
                        new InputName("reference"), new Value("ref")
                ), Value.of("10"))
        ))).when(executeUseCase)
                .execute(new ExecuteCommand(new Formula("true"), new StructuredData(List.of(
                        new StructuredDatum(new Reference("ref"), new Value("val"))
                ))));
        //language=JSON
        final String request = """
                {
                    "formula":"true",
                    "structuredData": {
                        "ref": "val"
                    }
                }
                """;

        // When && Then
        //language=JSON
        final String expectedBody = """
                {
                    "result": "true",
                    "executions": [
                        {
                            "executionId": "00000000-0000-0000-0000-000000000000",
                            "executedAt": "2023-12-25T10:15:30+01:00",
                            "start": 4,
                            "end": 5,
                            "inputs": {
                                "reference": "ref"
                            },
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
    public void shouldHandleSyntaxErrorException() throws JSONException {
        // Given
        doThrow(new ExecutionException(new AntlrSyntaxErrorException(new Formula("true"), new AntlrSyntaxError(0, 1, "msg"))))
                .when(executeUseCase).execute(new ExecuteCommand(new Formula("true"), new StructuredData(List.of(
                        new StructuredDatum(new Reference("ref"), new Value("val"))
                ))));
        //language=JSON
        final String request = """
                {
                    "formula":"true",
                    "structuredData": {
                        "ref":"val"
                    }
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
                .body(is("Syntax error at line '0' at position '1' with message 'msg'"));
    }

    @Test
    public void shouldHandleException() {
        // Given
        doThrow(new ExecutionException(new Exception("unexpected exception")))
                .when(executeUseCase).execute(new ExecuteCommand(new Formula("true"), new StructuredData(List.of(
                        new StructuredDatum(new Reference("ref"), new Value("val"))
                ))));
        //language=JSON
        final String request = """
                {
                    "formula":"true",
                    "structuredData": {
                        "ref":"val"
                    }
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
                .contentType("application/vnd.execution-unexpected-exception-v1+text")
                .body(is("unexpected exception"));
    }
}
