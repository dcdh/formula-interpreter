package com.damdamdeo.formula.infrastructure;

import io.quarkus.test.junit.QuarkusIntegrationTest;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusIntegrationTest
public class ApplicationTestIT {

    @Test
    public void shouldExecute() {
        // Given
        final String request = """
                {
                    "formula":"true",
                    "structuredData": {}
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

}
