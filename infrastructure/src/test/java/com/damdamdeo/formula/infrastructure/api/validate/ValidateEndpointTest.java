package com.damdamdeo.formula.infrastructure.api.validate;

import com.damdamdeo.formula.domain.Formula;
import com.damdamdeo.formula.domain.ValidationException;
import com.damdamdeo.formula.domain.spi.Validator;
import com.damdamdeo.formula.domain.usecase.provider.ValidatorTestResolver;
import com.damdamdeo.formula.infrastructure.antlr.AntlrSyntaxError;
import com.damdamdeo.formula.infrastructure.antlr.AntlrSyntaxErrorException;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.MediaType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.doReturn;

@ExtendWith(ValidateEndpointTest.AntlrValidatorTestResolver.class)
@QuarkusTest
public class ValidateEndpointTest {

    @InjectMock
    Validator<AntlrSyntaxError> validator;

    @ValidatorTestResolver.ValidTest
    public void shouldReturnEmptyWhenFormulaIsValid(final Formula givenFormula,
                                                    final ValidatorTestResolver.GivenResponse<AntlrSyntaxError> givenResponse) {
        // Given
        doReturn(givenResponse.response()).when(validator).validate(givenFormula);

        // When && Then
        given()
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept("application/vnd.formula-validator-v1+json")
                .multiPart("formula", givenFormula.formula())
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

    @ValidatorTestResolver.InValidTest
    void shouldReturnDomainSyntaxErrorWhenFormulaIsInvalid(final Formula givenFormula,
                                                           final ValidatorTestResolver.GivenResponse<AntlrSyntaxError> givenResponse) {
        // Given
        doReturn(givenResponse.response()).when(validator).validate(givenFormula);

        // When && Then
        given()
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept("application/vnd.formula-validator-v1+json")
                .multiPart("formula", givenFormula.formula())
                .when()
                .post("/validate")
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_OK)
                .contentType("application/vnd.formula-validator-v1+json")
                .body("valid", is(false))
                .body("line", is(1))
                .body("charPositionInLine", is(1))
                .body("msg", is("mismatched input '<EOF>' expecting {'ADD', 'SUB', " +
                        "'DIV', 'MUL', 'GT', 'GTE', 'EQ', 'NEQ', 'LT', 'LTE', 'AND', 'OR', 'IF', 'IFERROR', 'ISNUM', 'ISLOGICAL', " +
                        "'ISTEXT', 'ISBLANK', 'ISNA', 'ISERROR', 'IFNA', TRUE, FALSE, STRUCTURED_REFERENCE, VALUE, NUMERIC}"));
    }

    @ValidatorTestResolver.DomainValidationExceptionTest
    void shouldFailWhenAnExceptionIsThrown(final Formula givenFormula,
                                           final ValidatorTestResolver.GivenResponse<AntlrSyntaxError> givenResponse) {
        // Given
        doReturn(givenResponse.response()).when(validator).validate(givenFormula);

        // When && Then
        given()
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept("application/vnd.formula-validator-v1+json")
                .multiPart("formula", givenFormula.formula())
                .when()
                .post("/validate")
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                .contentType("application/vnd.validation-unexpected-exception-v1+json")
                .body("message", is("Syntax error at line '1' at positionedAt '1' with message " +
                        "'mismatched input '<EOF>' expecting {'ADD', 'SUB', 'DIV', 'MUL', 'GT', 'GTE', 'EQ', 'NEQ', " +
                        "'LT', 'LTE', 'AND', 'OR', 'IF', 'IFERROR', 'ISNUM', 'ISLOGICAL', 'ISTEXT', 'ISBLANK', 'ISNA', " +
                        "'ISERROR', 'IFNA', TRUE, FALSE, STRUCTURED_REFERENCE, VALUE, NUMERIC}'"));
    }

    // TODO FCK Move coté antlr !!!
    static class AntlrValidatorTestResolver extends ValidatorTestResolver<AntlrSyntaxError> {

        @Override
        protected AntlrSyntaxError invalid() {
            return new AntlrSyntaxError(1, 1, "mismatched input '<EOF>' expecting {'ADD', 'SUB', " +
                    "'DIV', 'MUL', 'GT', 'GTE', 'EQ', 'NEQ', 'LT', 'LTE', 'AND', 'OR', 'IF', 'IFERROR', 'ISNUM', 'ISLOGICAL', " +
                    "'ISTEXT', 'ISBLANK', 'ISNA', 'ISERROR', 'IFNA', TRUE, FALSE, STRUCTURED_REFERENCE, VALUE, NUMERIC}");
        }

        @Override
        protected ValidationException validationException() {
            return new ValidationException(new AntlrSyntaxErrorException(
                    new Formula("\""), invalid()
            ));
        }
    }
}
