package com.damdamdeo.formula.infrastructure;

import com.damdamdeo.formula.domain.provider.ListOfProcessedAtParameterResolver;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.MediaType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@QuarkusTest
@ExtendWith(ListOfProcessedAtParameterResolver.class)
public class ApplicationTest {
    @Test
    public void shouldEvaluate() {
        // Given
        //language=JSON
        final String request = """
                {
                    "formula":"true",
                    "structuredReferences": {},
                    "evaluateOn": "ANTLR",
                    "debugFeature": "ACTIVE"
                }
                """;

        // When && Then
        given()
                .contentType("application/vnd.formula-evaluate-v1+json")
                .accept("application/vnd.formula-evaluated-v1+json")
                .body(request)
                .when()
                .post("/evaluate")
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void shouldSuggestCompletion() {
        // Given

        // When && Then
        given()
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept("application/vnd.suggest-completion-v1+json")
                .multiPart("suggestedFormula", "IF")
                .when()
                .post("/suggestCompletion")
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_OK);
    }

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
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void shouldExecutionsBeInExpectedOrders() {
        // Given
        //language=JSON
        final String request = """
                {
                    "formula":"MUL([@[Sales Amount]],DIV([@[% Commission]],100))",
                    "structuredReferences": {
                        "Sales Amount": "260",
                        "% Commission": "10"
                    },
                    "evaluateOn": "ANTLR",
                    "debugFeature": "ACTIVE"
                }
                """;

        // When && Then
        given()
                .contentType("application/vnd.formula-evaluate-v1+json")
                .accept("application/vnd.formula-evaluated-v1+json")
                .body(request)
                .when()
                .post("/evaluate")
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_OK)
                .body("intermediateResults[0].positionedAt.start", is(0))
                .body("intermediateResults[0].positionedAt.end", is(48))
                .body("intermediateResults[1].positionedAt.start", is(4))
                .body("intermediateResults[1].positionedAt.end", is(20))
                .body("intermediateResults[2].positionedAt.start", is(22))
                .body("intermediateResults[2].positionedAt.end", is(47))
                .body("intermediateResults[3].positionedAt.start", is(26))
                .body("intermediateResults[3].positionedAt.end", is(42))
                .body("intermediateResults[4].positionedAt.start", is(44))
                .body("intermediateResults[4].positionedAt.end", is(46));
    }
}
