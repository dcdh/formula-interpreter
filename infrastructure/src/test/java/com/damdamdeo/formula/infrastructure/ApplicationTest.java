package com.damdamdeo.formula.infrastructure;

import com.damdamdeo.formula.domain.provider.ListOfEvaluatedAtParameterResolver;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.MediaType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.restassured.RestAssured.given;

// TODO je devrais utiliser un use case provider pour chacun ...
@QuarkusTest
@ExtendWith(ListOfEvaluatedAtParameterResolver.class)
class ApplicationTest {
    @Test
    void shouldEvaluateOnAntlr() {
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
    void shouldMapUsingAntlrAndNextEvaluateOnDomain() {
        // Given
        //language=JSON
        final String request = """
                {
                    "formula":"true",
                    "structuredReferences": {},
                    "evaluateOn": "ANTLR_MAPPING_DOMAIN_EVAL",
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
    void shouldSuggestCompletion() {
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
    void shouldValidate() {
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
}
