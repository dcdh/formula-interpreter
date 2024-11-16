package com.damdamdeo.formula.infrastructure.web.api;

import com.damdamdeo.formula.domain.Formula;
import com.damdamdeo.formula.domain.ValidationException;
import com.damdamdeo.formula.domain.usecase.ValidateCommand;
import com.damdamdeo.formula.domain.usecase.ValidateUseCase;
import com.damdamdeo.formula.infrastructure.parser.antlr.AntlrSyntaxError;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.core.MediaType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.doReturn;

@QuarkusTest
public class ValidatorEndpointTest {

    @InjectMock
    private ValidateUseCase<AntlrSyntaxError> validateUseCase;

    @Test
    public void shouldValidateBeValidReturnExpectedResponse() {
        // Given
        doReturn(
                Uni.createFrom().item(Optional::empty)
        )
                .when(validateUseCase).execute(new ValidateCommand(new Formula("true")));

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
                .contentType("application/vnd.formula-validator-v1+json")
                .body("valid", is(true))
                .body("line", nullValue())
                .body("charPositionInLine", nullValue())
                .body("msg", nullValue());
    }

    @Test
    public void shouldReturnSyntaxErrorException() {
        // Given
        doReturn(
                Uni.createFrom().item(Optional.of(new AntlrSyntaxError(0, 1, "msg")))
        )
                .when(validateUseCase).execute(new ValidateCommand(new Formula("true")));

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
                .contentType("application/vnd.formula-validator-v1+json")
                .body("valid", is(false))
                .body("line", is(1))
                .body("charPositionInLine", is(1))
                .body("msg", is("msg"));
    }

    @Test
    public void shouldHandleException() {
        // Given
        doReturn(
                Uni.createFrom().failure(new ValidationException(new Exception("unexpected \"exception\"")))
        )
                .when(validateUseCase).execute(new ValidateCommand(new Formula("true")));

        // When && Then
        given()
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept("application/vnd.formula-validator-v1+json")
                .multiPart("formula", "true")
                .when()
                .post("/validate")
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                .contentType("application/vnd.validation-unexpected-exception-v1+json")
                .body("message", is("unexpected \"exception\""));
    }
}
