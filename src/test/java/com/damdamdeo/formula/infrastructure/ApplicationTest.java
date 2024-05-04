package com.damdamdeo.formula.infrastructure;

import com.damdamdeo.formula.domain.ExecutedAt;
import com.damdamdeo.formula.domain.spi.ExecutedAtProvider;
import com.damdamdeo.formula.domain.Formula;
import com.damdamdeo.formula.infrastructure.antlr.AntlrParseTreeGenerator;
import com.damdamdeo.formula.infrastructure.antlr.DefaultGenerator;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectSpy;
import jakarta.ws.rs.core.MediaType;
import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.time.ZonedDateTime;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@QuarkusTest
public class ApplicationTest {

    // only happy path here
    @Nested
    public class Execution {
        @InjectMock
        private ExecutedAtProvider executedAtProvider;

        @Test
        public void shouldExecute() throws JSONException {
            // Given
            when(executedAtProvider.now())
                    .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")))
                    .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")))
                    .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:02+01:00[Europe/Paris]")))
                    .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")))
                    .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:04+01:00[Europe/Paris]")))
                    .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:05+01:00[Europe/Paris]")));
            //language=JSON
            final String givenRequest = """
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
                         "executedAtEnd": "2023-12-25T10:15:05+01:00",
                         "exactProcessedInNanos": 2000000000,
                         "result": "true",
                         "parserExecutionProcessedIn": {
                             "executedAtStart": "2023-12-25T10:15:01+01:00",
                             "executedAtEnd": "2023-12-25T10:15:02+01:00",
                             "processedInNanos": 1000000000
                         },
                         "elementExecutions": [
                             {
                                 "executedAtStart": "2023-12-25T10:15:03+01:00",
                                 "executedAtEnd": "2023-12-25T10:15:04+01:00",
                                 "processedInNanos": 1000000000,
                                 "range": {
                                     "start": 0,
                                     "end": 3
                                 },
                                 "inputs": [
                                 ],
                                 "result": "true"
                             }
                         ]
                    }
                    """;
            final String actualBody = given()
                    .contentType("application/vnd.formula-execute-v1+json")
                    .accept("application/vnd.formula-execution-v1+json")
                    .body(givenRequest)
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
        public void shouldExecutionsBeInExpectedOrders() {
            // Given
            when(executedAtProvider.now())
                    .thenReturn(new ExecutedAt(ZonedDateTime.now()));

            //language=JSON
            final String givenRequest = """
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
                    .body(givenRequest)
                    .when()
                    .post("/execute")
                    .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_OK)
                    .body("elementExecutions[0].range.start", is(0))
                    .body("elementExecutions[0].range.end", is(48))
                    .body("elementExecutions[1].range.start", is(4))
                    .body("elementExecutions[1].range.end", is(20))
                    .body("elementExecutions[2].range.start", is(22))
                    .body("elementExecutions[2].range.end", is(47))
                    .body("elementExecutions[3].range.start", is(26))
                    .body("elementExecutions[3].range.end", is(42))
                    .body("elementExecutions[4].range.start", is(44))
                    .body("elementExecutions[4].range.end", is(46));
        }
    }

    @Nested
    public class Suggestion {
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
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .accept("application/vnd.suggest-completion-v1+json")
                    .multiPart("suggestedFormula", "IF")
                    .when()
                    .post("/suggestCompletion")
                    .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_OK)
                    .contentType("application/vnd.suggest-completion-v1+json;charset=UTF-8")
                    .extract().response().body().asString();
            JSONAssert.assertEquals(expectedBody, actualBody, JSONCompareMode.STRICT);
        }
    }

    @Nested
    public class Validation {
        @Test
        public void shouldValidate() {
            // Given

            // When && Then
            given()
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .accept("application/vnd.formula-validator-v1+json")
                    .multiPart("formula", "true")
                    .when()
                    .post("/validate")
                    .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_OK)
                    .contentType("application/vnd.formula-validator-v1+json");
        }

    }

    @Nested
    public class Caching {
        @InjectSpy
        @DefaultGenerator
        AntlrParseTreeGenerator antlrParseTreeGenerator;

        @Test
        public void shouldNotUseCacheWhenValidatingFormula() {
            // Given

            // When
            for (int validation = 0; validation < 3; validation++) {
                given()
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept("application/vnd.formula-validator-v1+json")
                        .multiPart("formula", "MUL([@[Sales Amount]],DIV([@[% Commission]],100))")
                        .when()
                        .post("/validate")
                        .then()
                        .log().all()
                        .statusCode(HttpStatus.SC_OK);
            }

            // Then
            Mockito.verify(antlrParseTreeGenerator, times(3))
                    .generate(new Formula("MUL([@[Sales Amount]],DIV([@[% Commission]],100))"));
        }

        @Test
        public void shouldUseCacheWhenExecutingFormula() {
            // Given
            //language=JSON
            final String givenRequest = """
                    {
                        "formula":"MUL([@[Sales Amount]],DIV([@[% Commission]],100))",
                        "structuredData": {
                            "Sales Amount": "260",
                            "% Commission": "10"
                        },
                        "debugFeature": "ACTIVE"
                    }
                    """;

            // When
            for (int execution = 0; execution < 3; execution++) {
                given()
                        .contentType("application/vnd.formula-execute-v1+json")
                        .accept("application/vnd.formula-execution-v1+json")
                        .body(givenRequest)
                        .when()
                        .post("/execute")
                        .then()
                        .log().all()
                        .statusCode(HttpStatus.SC_OK);
            }

            // Then
            Mockito.verify(antlrParseTreeGenerator, times(1))
                    .generate(new Formula("MUL([@[Sales Amount]],DIV([@[% Commission]],100))"));
        }
    }
}
