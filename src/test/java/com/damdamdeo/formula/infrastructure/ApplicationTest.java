package com.damdamdeo.formula.infrastructure;

import com.damdamdeo.formula.domain.ExecutedAt;
import com.damdamdeo.formula.domain.ExecutedAtProvider;
import com.damdamdeo.formula.domain.ExecutionId;
import com.damdamdeo.formula.domain.ExecutionIdGenerator;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.time.ZonedDateTime;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.mockito.Mockito.doReturn;

@QuarkusTest
public class ApplicationTest {

    // only happy path here
    @InjectMock
    private ExecutedAtProvider executedAtProvider;

    @InjectMock
    private ExecutionIdGenerator executionIdGenerator;

    @Test
    public void shouldExecute() throws JSONException {
        // Given
        doReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:30+01:00[Europe/Paris]"))).when(executedAtProvider).now();
        doReturn(new ExecutionId(new UUID(0, 0))).when(executionIdGenerator).generate();
        //language=JSON
        final String request = """
                {
                    "formula":"true",
                    "structuredData": {}
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
                            "start": 0,
                            "end": 3,
                            "inputs": {
                            },
                            "result": "true"
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
    public void shouldSuggestCompletion() throws JSONException {
        // Given

        // When && Then
        //language=JSON
        final String expectedBody = """
                [
                    "("
                ]
                """;
        final String actualBody = given()
                .accept("application/vnd.suggest-completion-v1+json")
                .formParam("suggestedFormula", "IF")
                .when()
                .post("/suggestCompletion")
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_OK)
                .contentType("application/vnd.suggest-completion-v1+json;charset=UTF-8")
                .extract().response().body().asString();
        JSONAssert.assertEquals(expectedBody, actualBody, JSONCompareMode.STRICT);
    }

    @Test
    public void shouldValidate() throws JSONException {
        // Given

        // When && Then
        given()
                .accept("application/vnd.formula-validator-v1+json")
                .formParam("formula", "true")
                .when()
                .post("/validate")
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT)
                .contentType("application/vnd.formula-validator-v1+json");
    }

}
