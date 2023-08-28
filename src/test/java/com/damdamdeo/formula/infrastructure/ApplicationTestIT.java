package com.damdamdeo.formula.infrastructure;

import io.quarkus.test.junit.QuarkusIntegrationTest;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@QuarkusIntegrationTest
public class ApplicationTestIT {

    @Test
    public void shouldExecute() {
        // Given
        //language=JSON
        final String request = """
                {
                    "formula":"true",
                    "structuredData": {},
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
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void shouldSuggestCompletion() {
        // Given

        // When && Then
        given()
                .accept("application/vnd.suggest-completion-v1+json")
                .formParam("suggestedFormula", "IF")
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
                .accept("application/vnd.formula-validator-v1+json")
                .formParam("formula", "true")
                .when()
                .post("/validate")
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT);
    }

    @Test
    public void shouldExecutionsBeInSameOrders() {
        // Given
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
