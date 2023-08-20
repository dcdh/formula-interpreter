package com.damdamdeo.formula.infrastructure.interfaces;

import com.damdamdeo.formula.domain.Formula;
import com.damdamdeo.formula.domain.ValidationException;
import com.damdamdeo.formula.domain.usecase.ValidateCommand;
import com.damdamdeo.formula.domain.usecase.ValidateUseCase;
import com.damdamdeo.formula.infrastructure.antlr.AntlrSyntaxError;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@QuarkusTest
public class ValidatorEndpointTest {

    @InjectMock
    private ValidateUseCase<AntlrSyntaxError> validateUseCase;

    @Test
    public void shouldValidate() {
        // Given
        doReturn(Optional.empty()).when(validateUseCase).execute(new ValidateCommand(new Formula("true")));

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
    public void shouldHandleSyntaxErrorException() throws JSONException {
        // Given
        doReturn(Optional.of(new AntlrSyntaxError(0, 1, "msg"))).when(validateUseCase).execute(new ValidateCommand(new Formula("true")));

        // When && Then
        //language=JSON
        final String expectedBody = """
                {
                    "line": 0,
                    "charPositionInLine": 1,
                    "msg": "msg"
                }
                """;
        final String actualBody = given()
                .accept("application/vnd.formula-validator-v1+json")
                .formParam("formula", "true")
                .when()
                .post("/validate")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .contentType("application/vnd.formula-validator-v1+json")
                .extract().response().body().asString();
        JSONAssert.assertEquals(expectedBody, actualBody, JSONCompareMode.STRICT);
    }

    @Test
    public void shouldHandleException() {
        // Given
        doThrow(new ValidationException(new Exception("unexpected \"exception\"")))
                .when(validateUseCase).execute(new ValidateCommand(new Formula("true")));

        // When && Then
        given()
                .accept("application/vnd.formula-validator-v1+json")
                .formParam("formula", "true")
                .when()
                .post("/validate")
                .then()
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                .contentType("application/vnd.validation-unexpected-exception-v1+json")
                .body("message", is("unexpected \"exception\""));
    }
}
