package com.damdamdeo.formula.infrastructure;

import com.damdamdeo.formula.domain.ExecutedAt;
import com.damdamdeo.formula.domain.ExecutedAtProvider;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.time.ZonedDateTime;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

@QuarkusTest
public class ApplicationTest {

    // only happy path here
    @InjectMock
    private ExecutedAtProvider executedAtProvider;

    @Test
    public void shouldExecute() throws JSONException {
        // Given
        when(executedAtProvider.now())
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:02+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")));
        //language=JSON
        final String request = """
                {
                    "formula":"true",
                    "structuredData": {},
                    "debugFeature": "ACTIVE"
                }
                """;

        // When && Then
        //language=JSON
        final String expectedBody = """
                {
                     "executedAtStart": "2023-12-25T10:15:00+01:00",
                     "executedAtEnd": "2023-12-25T10:15:03+01:00",
                     "processedInNanos": 3000000000,
                     "result": "true",
                     "elementExecutions": [
                         {
                             "executedAtStart": "2023-12-25T10:15:01+01:00",
                             "executedAtEnd": "2023-12-25T10:15:02+01:00",
                             "processedInNanos": 1000000000,
                             "position": {
                                 "start": 0,
                                 "end": 3
                             },
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
    public void shouldValidate() {
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

    @Test
    public void shouldExecutionsBeInSameOrders() {
        // Given
        when(executedAtProvider.now())
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:02+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:04+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:05+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:06+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:07+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:08+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:09+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:10+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:11+01:00[Europe/Paris]")));
        //language=JSON
        final String request = """
                {
                    "formula":"MUL([@[Sales Amount]],DIV([@[% Commission]],100))",
                    "structuredData": {
                        "Sales Amount": "260",
                        "% Commission": "10"
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
                .statusCode(HttpStatus.SC_OK)
                .body("elementExecutions[0].position.start", is(0))
                .body("elementExecutions[0].position.end", is(48))
                .body("elementExecutions[1].position.start", is(4))
                .body("elementExecutions[1].position.end", is(20))
                .body("elementExecutions[2].position.start", is(22))
                .body("elementExecutions[2].position.end", is(47))
                .body("elementExecutions[3].position.start", is(26))
                .body("elementExecutions[3].position.end", is(42))
                .body("elementExecutions[4].position.start", is(44))
                .body("elementExecutions[4].position.end", is(46));
    }
}
