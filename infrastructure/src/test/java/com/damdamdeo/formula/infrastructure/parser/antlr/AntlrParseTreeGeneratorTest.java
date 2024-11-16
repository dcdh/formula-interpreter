package com.damdamdeo.formula.infrastructure.parser.antlr;

import com.damdamdeo.formula.domain.Formula;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectSpy;
import jakarta.ws.rs.core.MediaType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@QuarkusTest
public class AntlrParseTreeGeneratorTest {

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
        verify(antlrParseTreeGenerator, times(3))
                .generate(new Formula("MUL([@[Sales Amount]],DIV([@[% Commission]],100))"));
    }

    @Test
    public void shouldUseCacheWhenExecutingFormula() {
        // FIXME If I use same formula than previous test likes "MUL([@[Sales Amount]],DIV([@[% Commission]],100))"
        // FIXME it will fail with no interaction :/ Maybe, a context is kept between test execution
        // Given
        //language=JSON
        final String givenRequest = """
                {
                    "formula":"MUL([@[Sales Amount]],DIV([@[% Commission]],200))",
                    "structuredReferences": {
                        "Sales Amount": "260",
                        "% Commission": "10"
                    },
                    "evaluateOn": "ANTLR",
                    "debugFeature": "ACTIVE"
                }
                """;

        // When
        for (int execution = 0; execution < 3; execution++) {
            given()
                    .contentType("application/vnd.formula-evaluate-v1+json")
                    .accept("application/vnd.formula-evaluated-v1+json")
                    .body(givenRequest)
                    .when()
                    .post("/evaluate")
                    .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_OK);
        }

        // Then
        verify(antlrParseTreeGenerator, times(1))
                .generate(new Formula("MUL([@[Sales Amount]],DIV([@[% Commission]],200))"));
    }
}
