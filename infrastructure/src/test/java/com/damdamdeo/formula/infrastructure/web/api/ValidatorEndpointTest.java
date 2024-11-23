package com.damdamdeo.formula.infrastructure.web.api;

import com.damdamdeo.formula.domain.Formula;
import com.damdamdeo.formula.domain.ValidationException;
import com.damdamdeo.formula.domain.usecase.ValidateCommand;
import com.damdamdeo.formula.domain.usecase.ValidateUseCase;
import com.damdamdeo.formula.domain.usecase.resolver.ValidatorParameterResolver;
import com.damdamdeo.formula.infrastructure.parser.antlr.AntlrSyntaxError;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.core.MediaType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.doReturn;

@QuarkusTest
@ExtendWith(ValidatorEndpointTest.AntlrValidatorParameterResolver.class)
public class ValidatorEndpointTest {
    @InjectMock
    private ValidateUseCase<AntlrSyntaxError> validateUseCase;

    @ValidatorParameterResolver.ValidTest
    public void shouldReturnEmptyWhenFormulaIsValid(final ValidatorParameterResolver.GivenHappyPath<AntlrSyntaxError> givenHappyPath) {
        // Given
        final Formula givenFormula = givenHappyPath.formula();
        doReturn(givenHappyPath.toUni()).when(validateUseCase).execute(new ValidateCommand(givenFormula));

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

    @ValidatorParameterResolver.InvalidTest
    public void shouldReturnDomainSyntaxErrorWhenFormulaIsInvalid(final ValidatorParameterResolver.GivenHappyPath<AntlrSyntaxError> givenHappyPath) {
        // Given
        final Formula givenFormula = givenHappyPath.formula();
        doReturn(givenHappyPath.toUni()).when(validateUseCase).execute(new ValidateCommand(givenFormula));

        // When && Then
        given()
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept("application/vnd.formula-validator-v1+json")
                .multiPart("formula", "IF")
                .when()
                .post("/validate")
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_OK)
                .contentType("application/vnd.formula-validator-v1+json")
                .body("valid", is(false))
                .body("line", is(0))
                .body("charPositionInLine", is(1))
                .body("msg", is("msg"));
    }

    @ValidatorParameterResolver.ValidationExceptionTest
    @Tag("Exception")
    public void shouldHandleException(final ValidatorParameterResolver.GivenFailing givenFailing) {
        // Given
        final Formula givenFormula = givenFailing.formula();
        doReturn(Uni.createFrom().failure(new ValidationException(givenFailing.cause())))
                .when(validateUseCase).execute(new ValidateCommand(givenFormula));

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
                .body("message", is("unexpected \"cause\""));
    }

    static class AntlrValidatorParameterResolver extends ValidatorParameterResolver<AntlrSyntaxError> {
        @Override
        protected AntlrSyntaxError givenInvalid() {
            return new AntlrSyntaxError(0, 1, "msg");
        }

        @Override
        protected List<GivenFailing> givenFailings() {
            return List.of(
                    new GivenFailing(
                            new Formula("true"),
                            new Exception("unexpected \"cause\""))
            );
        }
    }

}
